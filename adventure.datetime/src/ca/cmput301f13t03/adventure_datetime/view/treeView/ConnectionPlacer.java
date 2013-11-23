package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.graphics.Path;
import android.graphics.PathMeasure;

public final class ConnectionPlacer 
{
	private int m_horizontalOffset = 0;
	private int m_verticalOffset = 0;
	private int m_mapWidth = 0;
	private int m_mapHeight = 0;
	private int m_gridSize = 0;
	private boolean[][] m_map = null;

	public ConnectionPlacer(ArrayList<GridSegment> gridSegments, int gridSize)
	{
		// expand the gridSegments into a large square region. Fill in
		// any holes as just empty regions
		int leftMost = Integer.MAX_VALUE;
		int rightMost = Integer.MIN_VALUE;
		int topMost = Integer.MAX_VALUE;
		int bottomMost = Integer.MIN_VALUE;
		m_gridSize = gridSize;

		for(GridSegment seg : gridSegments)
		{
			int top = seg.y;
			int bottom = seg.y + seg.height;
			int left = seg.x;
			int right = seg.x + seg.width;

			leftMost = Math.min(left, leftMost);
			rightMost = Math.max(right, rightMost);
			topMost = Math.min(top, topMost);
			bottomMost = Math.max(bottom, bottomMost);
		}

		m_mapWidth = rightMost - leftMost;
		m_mapHeight = bottomMost - topMost;

		m_mapWidth /= gridSize;
		m_mapHeight /= gridSize;

		m_horizontalOffset = leftMost;
		m_verticalOffset = topMost;

		m_map = new boolean[m_mapWidth][m_mapHeight];
		FillMap(gridSegments);
	}

	private void FillMap(ArrayList<GridSegment> segments)
	{
		ClearMap();

		for(GridSegment seg : segments)
		{
			int gridWidth = seg.height / m_gridSize;
			int gridHeight = seg.width / m_gridSize;

			int baseX = seg.x - m_horizontalOffset;
			int baseY = seg.y - m_verticalOffset;
			int endX = baseX + gridWidth;
			int endY = baseY + gridHeight;

			for(int x = baseX ; x < endX ; ++x)
			{
				for(int y = baseY ; y < endY ; ++y)
				{
					if(!seg.IsEmpty(x, y))
					{
						m_map[x][y] = true;
					}
				}
			}
		}
	}

	private void ClearMap()
	{
		for(int x = 0 ; x < m_mapWidth ; ++x)
		{
			for(int y = 0 ; y < m_mapHeight ; ++y)
			{
				m_map[x][y] = false;
			}
		}
	}

	public void PlaceConnection(FragmentConnection connection, FragmentNode originFrag, FragmentNode targetFrag)
	{
		// Transform the x and y cords to local map space
		Location start = new Location(	originFrag.x - m_horizontalOffset, 
										originFrag.y - m_verticalOffset);
		Location end = new Location(	targetFrag.x - m_horizontalOffset, 
										targetFrag.y - m_verticalOffset);

		// Path values
		Path startPath = null;
		Path endPath = null;
		Path midPath = null;
		Path finalPath = null;

		// first draw a line in the direction of the target until a free point is reached
		// store this result in the start path
		startPath = GetPath(start, end, new PartialPathBuilder(start, end, m_map));

		// now draw a line from the target in the direction of the start
		// store this result in the end path
		endPath = GetPath(end, start, new PartialPathBuilder(end, start, m_map));

		// now path find from the start path to the end path
		midPath = ConstructMidPath(startPath, endPath, start, end);

		// finally join the 3 paths together
		finalPath = JoinPaths(startPath, midPath, endPath);
		
		// transform them to global space and assign it to the connection
		finalPath.offset(m_horizontalOffset, m_verticalOffset);
		connection.SetPath(finalPath);
	}
	
	private Path JoinPaths(Path start, Path mid, Path end)
	{
		mid.addPath(end);
		start.addPath(mid);
		return start;
	}
	
	private Path ConstructMidPath(Path startPath, Path endPath, Location start, Location end)
	{
		Location midStart = null;
		Location midEnd = null;

		PathMeasure pm = new PathMeasure(startPath, false);
		float cords[] = { 0 , 0 };

		if(pm.getPosTan(pm.getLength(), cords, null))
		{
			midStart = new Location(Math.round(cords[0]), Math.round(cords[1]));
		}
		else
		{
			midStart = new Location(start.x, start.y);
		}

		pm = new PathMeasure(endPath, false);
		if(pm.getPosTan(pm.getLength(), cords, null))
		{
			midEnd = new Location(Math.round(cords[0]), Math.round(cords[1]));
		}
		else
		{
			midEnd = new Location(end.x, end.y);
		}

		// now we find a path between them
		return GetPath(midStart, midEnd, new FullPathBuilder(midStart, midEnd));
	}

	// helper function, why does Java not have this???
	private int Clamp(int val, int min, int max)
	{
		return Math.min(Math.max(val, min), max);
	}

	private Path GetPath(Location start, Location target, IPathBuilderCallbacks builder)
	{
		Path result = new Path();
		result.moveTo(start.x, start.y);
		SortedLocationList openList = new SortedLocationList();
		Set<Location> closedList = new HashSet<Location>();
		int currentDepth = 0;

		LocationNode startPoint = new LocationNode(start, target, currentDepth, null);
		openList.add(startPoint);

		while(openList.size() > 0)
		{
			List<LocationNode> nextBatch = openList.GetAndRemoveNextLowestWeights();

			for(LocationNode currentLocation : nextBatch)
			{
				// only check it if we haven't checked it already
				if(!closedList.contains(currentLocation))
				{
					if(builder.TestForDestination(currentLocation.location))
					{
						return builder.BuildPath(currentLocation);
					}
					else
					{
						// no luck, this node is empty.
						// add it to the closedList
						closedList.add(currentLocation.location);

						int baseX = currentLocation.location.x;
						int baseY = currentLocation.location.y;

						// get adjacent locations
						int yUp = baseY - 1;
						int yDown = baseY + 1;
						int xRight = baseX + 1;
						int xLeft = baseX - 1;

						// clamp all values
						yUp = Clamp(yUp, 0, m_mapHeight - 1);
						yDown = Clamp(yDown, 0, m_mapHeight - 1);
						xRight = Clamp(xRight, 0, m_mapWidth - 1);
						xLeft = Clamp(xLeft, 0, m_mapWidth - 1);

						// construct adjacent nodes
						LocationNode up = new LocationNode(new Location(baseX, yUp), target, currentDepth, currentLocation);
						LocationNode down = new LocationNode(new Location(baseX, yDown), target, currentDepth, currentLocation);
						LocationNode left = new LocationNode(new Location(xLeft, baseY), target, currentDepth, currentLocation);
						LocationNode right = new LocationNode(new Location(xRight, baseY), target, currentDepth, currentLocation);

						// add all adjacent nodes to this list
						openList.add(up);
						openList.add(down);
						openList.add(left);
						openList.add(right);

						// now lets keep searching!
					}
				}
			}

			++currentDepth;
		}

		// if we've made it this far then no path could be found as one does not exist
		// so just return a default path
		return builder.BuildDefaultPath();
	}

	private final class Location implements Comparable<Location>
	{
		public Location(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public int x = 0;
		public int y = 0;

		public int compareTo(Location other) 
		{
			// bleh, I don't like this
			if(this.x > other.x)
			{
				return 1;
			}
			else if(this.x < other.x)
			{
				return -1;
			}
			else
			{
				if(this.y > other.y)
				{
					return 1;
				}
				else if(this.y < other.y)
				{
					return -1;
				}
				else
				{
					// they are equal!
					return 0;
				}
			}
		}
	}

	// no I am not using tuple for this.
	// Item1 Item2 is a terrible interface
	private final class LocationNode
	{
		public LocationNode(Location src, Location target, int expansionLevel, LocationNode previousNode)
		{
			location = src;
			// not using Math.pow cuz it is slow for squaring stuff
			weight = (target.x - src.x)*(target.x - src.x) +
					(target.y - src.y)*(target.y - src.y);
			weight += expansionLevel;

			this.prev = previousNode;
		}

		public Location location;
		public int weight;
		public LocationNode prev;

		// used in path creation
		public float dx;
		public float dy;
	}

	private final class SortedLocationList extends LinkedList<LocationNode>
	{
		private static final long serialVersionUID = -1181516011560588762L;

		@Override
		public boolean add(LocationNode object) 
		{
			Iterator<LocationNode> itr = this.listIterator();
			int insertLocation = 0;

			while(itr.hasNext())
			{
				if(itr.next().weight > object.weight)
				{
					// found it!
					break;
				}
				++insertLocation;
			}

			super.add(insertLocation, object);

			return true; // cuz the java spec says that add must always return true for a linked list
		}

		public List<LocationNode> GetAndRemoveNextLowestWeights()
		{
			List<LocationNode> lowest = new ArrayList<LocationNode>();

			LocationNode absoluteLowest = this.getFirst();

			Iterator<LocationNode> itr = this.listIterator();

			while(itr.hasNext())
			{
				LocationNode current = itr.next();
				// sorted list so no need to use less than
				if(current.weight == absoluteLowest.weight)
				{
					lowest.add(current);
				}
				else
				{
					// then we have all we need
					break;
				}
			}

			// now remove those we have collected
			this.removeAll(lowest);

			return lowest;
		}
	}

	private interface IPathBuilderCallbacks
	{
		boolean TestForDestination(Location loc);
		Path BuildPath(LocationNode endNode);
		Path BuildDefaultPath();
	}

	private final class FullPathBuilder implements IPathBuilderCallbacks
	{
		Location m_target = null;
		Location m_start = null;

		public FullPathBuilder(Location start, Location target)
		{
			this.m_start = start;
			this.m_target = target;
		}

		public boolean TestForDestination(Location loc) 
		{
			return this.m_target.compareTo(loc) == 0;
		}

		@Override
		public Path BuildPath(LocationNode endNode) 
		{
			List<LocationNode> pathNodes = new ArrayList<LocationNode>();

			// Backtrack through the nodes until we are back at the beginning (ie prev == null)
			LocationNode current = endNode;

			while(current.prev != null)
			{
				pathNodes.add(current);
				current = current.prev;
			}

			return ConstructFromNodes(pathNodes);
		}

		private Path ConstructFromNodes(List<LocationNode> nodes)
		{
			Path result = new Path();

			// Code courtesy of stack overflow
			// http://stackoverflow.com/questions/8287949/android-how-to-draw-a-smooth-line-following-your-finger/8289516#8289516
			// --Thanks to : johncarl
			if(nodes.size() > 1)
			{
				for(int i = nodes.size() - 2; i < nodes.size(); i++)
				{
					if(i >= 0)
					{
						LocationNode point = nodes.get(i);

						if(i == 0)
						{
							LocationNode next = nodes.get(i + 1);
							point.dx = ((next.location.x - point.location.x) / 3);
							point.dy = ((next.location.y - point.location.y) / 3);
						}
						else if(i == nodes.size() - 1)
						{
							LocationNode prev = nodes.get(i - 1);
							point.dx = ((point.location.x - prev.location.x) / 3);
							point.dy = ((point.location.y - prev.location.y) / 3);
						}
						else
						{
							LocationNode next = nodes.get(i + 1);
							LocationNode prev = nodes.get(i - 1);
							point.dx = ((next.location.x - prev.location.x) / 3);
							point.dy = ((next.location.y - prev.location.y) / 3);
						}
					}
				}
			}

			boolean first = true;
			for(int i = 0; i < nodes.size(); i++){
				LocationNode point = nodes.get(i);
				if(first)
				{
					first = false;
					result.moveTo(	point.location.x, 
									point.location.y);
				}
				else
				{
					LocationNode prev = nodes.get(i - 1);
					result.cubicTo(	prev.location.x + prev.dx, 
									prev.location.y + prev.dy, 
									point.location.x - point.dx, 
									point.location.y - point.dy, 
									point.location.x, 
									point.location.y);
				}
			}

			return result;
		}

		public Path BuildDefaultPath() 
		{
			Path result = new Path();

			result.moveTo(m_start.x, m_start.y);
			result.lineTo(m_target.x, m_target.y);

			return result;
		}

	}

	private final class PartialPathBuilder implements IPathBuilderCallbacks
	{
		private boolean[][] m_map = null;
		private Location m_start = null;
		private Location m_end = null;
		
		public PartialPathBuilder(Location start, Location target, boolean[][] map)
		{
			m_start = start;
			m_end = target;
			this.m_map = map;
		}
		
		public boolean TestForDestination(Location loc) 
		{
			return this.m_map[loc.x][loc.y];
		}

		public Path BuildPath(LocationNode endNode) 
		{
			Path result = new Path();

			result.moveTo(m_start.x, m_start.y);
			result.lineTo(endNode.location.x, endNode.location.y);

			return result;
		}

		public Path BuildDefaultPath() 
		{
			Path result = new Path();

			result.moveTo(m_start.x, m_start.y);
			result.lineTo(m_end.x, m_end.y);

			return result;
		}

	}
}
