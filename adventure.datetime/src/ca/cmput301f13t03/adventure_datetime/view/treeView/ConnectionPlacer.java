package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import android.graphics.Path;
import android.widget.HorizontalScrollView;

public class ConnectionPlacer 
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
		Location start = new Location(originFrag.x, originFrag.y);
		Location end = new Location(targetFrag.x, targetFrag.y);
		
		// Path values
		Path startPath = null;
		Path endPath = null;
		Path midPath = null;
		
		// first draw a line in the direction of the target until a free point is reached
		// store this result in the start path
		startPath = GetPartialStraightPath(start, end);
		
		// now draw a line from the target in the direction of the start
		// store this result in the end path
		endPath = GetPartialStraightPath(end, start);
		
		// now path find from the start path to the end path
		
		// finally join the 3 paths together, transform them to global space and assign it to the connection
	}
	
	private boolean IsLocationEmpty(Location loc)
	{
		// might want to do some validation here!
		return m_map[loc.x][loc.y];
	}
	
	private Path GetPartialStraightPath(Location start, Location target)
	{
		Path result = new Path();
		result.moveTo(start.x, start.y);
		SortedLocationList openList = new SortedLocationList(target);
		Set<SortedLocationList> closedList = new HashSet<SortedLocationList>();
		
		LocationDistancePair startPoint = new LocationDistancePair(start, target);
		openList.add(startPoint);
		
		while(openList.size() > 0)
		{
			LocationDistancePair currentLocation = openList.getFirst();
			
			if(IsLocationEmpty(currentLocation.location))
			{
				// Sweet! we found the nearest empty spot!
				result.lineTo(currentLocation.location.x, currentLocation.location.y);
				break;
			}
			else
			{
				// no luck, this node is empty.
				// add it to the closedList
			}
		}
		
		return result;
	}
	
	private class Location implements Comparable<Location>
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
	private class LocationDistancePair
	{
		public LocationDistancePair(Location src, Location target)
		{
			location = src;
			// not using Math.pow cuz it is slow for squaring stuff
			manhattanDistance = (target.x - src.x)*(target.x - src.x) +
								(target.y - src.y)*(target.y - src.y);
					
		}
		
		public Location location;
		public double manhattanDistance;
	}
	
	private class SortedLocationList extends LinkedList<LocationDistancePair>
	{
		private Location m_target = null;
		
		public SortedLocationList(Location target)
		{
			m_target = target;
		}
		
		@Override
		public boolean add(LocationDistancePair object) 
		{
			Iterator<LocationDistancePair> itr = this.iterator();
			int insertLocation = 0;
			
			while(itr.hasNext())
			{
				if(itr.next().manhattanDistance > object.manhattanDistance)
				{
					// found it!
					break;
				}
				++insertLocation;
			}
			
			super.add(insertLocation, object);
			
			return true; // cuz the java spec says that add must always return true for a linked list
		}
	}
}
