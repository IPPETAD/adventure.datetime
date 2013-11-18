package ca.cmput301f13t03.adventure_datetime.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IAllFragmentsListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TreeView extends SurfaceView implements IAllFragmentsListener, SurfaceHolder.Callback, Runnable
{
	private static final float FPS = 30.0f;
	private static final String TAG = "TreeView";
	
	Thread m_drawingThread = null;
	boolean m_isDrawing = false;
	SurfaceHolder m_surface = null;
	NodeGrid m_grid = null;
	Map<String, StoryFragment> m_fragments = null;
	Camera m_camera = null;
	
	// must have all constructors or it doesn't work
	public TreeView(Context context) 
	{
		super(context);
		Setup();
	}
	
	public TreeView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		Setup();
	}
	
	public TreeView(Context context,AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		Setup();
	}
	
	private void Setup()
	{
		this.getHolder().addCallback(this);
		Locator.getPresenter().Subscribe(this);
	}
	
	@Override
	public void OnAllFragmentsChange(Map<String, StoryFragment> newFragments) 
	{
		m_fragments = newFragments;
		if(m_grid != null)
		{
			m_grid.SetFragments(newFragments);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		// Don't care (yet)
	}

	@Override
	public void surfaceCreated(SurfaceHolder surface) 
	{
		m_camera = new Camera();
		m_grid = new NodeGrid();
		m_grid.SetFragments(m_fragments);
		m_isDrawing = true;
		m_surface = surface;
		m_drawingThread = new Thread(this);
		m_drawingThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surface) 
	{
		m_isDrawing = false;
		if(m_drawingThread != null)
		{
			try 
			{
				// allow it to finish drawing the current frame
				m_drawingThread.join();
			} 
			catch (InterruptedException e) 
			{
				Log.w(TAG, "Interrupted while Joining! " + e.getMessage());
			}
			m_drawingThread = null;
			m_grid = null;
		}
	}
	
	/**
	 * Main thread for drawing
	 */
	public void run() 
	{
		while(m_isDrawing)
		{
			Canvas canvas = m_surface.lockCanvas();
			
			try
			{
				// clear canvas
				
				// draw stuffs
				m_grid.Draw(canvas, m_camera);
			}
			finally
			{
				m_surface.unlockCanvasAndPost(canvas);
			}
			
			// then sleep for a bit
			try 
			{
				Thread.sleep((long) (1000.0f / FPS));
			} 
			catch (InterruptedException e) 
			{
				// Don't care, just log it and move on
				Log.w(TAG, "Interrupted while sleeping!");
			}
		}
	}
	
	private class Region
	{
		// getters and setter provide little value here
		public int x;
		public int y;
		public int width;
		public int height;
		
		public Region(int x, int y, int width, int height)
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public boolean Contains(int x, int y)
		{
			return 	x > this.x && x < (this.x + this.width) &&
					y > this.y && y < (this.y + this.height);
		}
	}
	
	private class Camera
	{
		// java generics are a pain to reuse just to make region use
		// floats or ints, not going to bother for this simple case
		private float x = -350.0f;
		private float y = -250.0f;
		
		private float m_zoomLevel = 1.0f;
		
		public Camera()
		{
			
		}
		
		public Region GetLocalTransform(Region region)
		{
			// the rounding isn't important
			return new Region(	(int)((region.x - this.x) * m_zoomLevel),
								(int)((region.y - this.y) * m_zoomLevel),
								(int)(region.width * m_zoomLevel),
								(int)(region.height * m_zoomLevel));
		}
		
		public void SetPosition(float x, float y)
		{
			this.x = x;
			this.y = y;
		}
		
		public void SetZoom(float zoom)
		{
			assert(zoom > 0);
			
			m_zoomLevel = zoom;
		}
	}
	
	/**
	 * Class that handles positioning of elements
	 * @author Jesse
	 */
	private class NodeGrid
	{
		private static final int BASE_WIDTH = 10;
		private static final int BASE_HEIGHT = 10;
		
		private ArrayList<GridSegment> m_segments = new ArrayList<TreeView.NodeGrid.GridSegment>();
		private ArrayList<FragmentConnection> m_connections = new ArrayList<TreeView.FragmentConnection>();
		private ArrayList<FragmentNode> m_nodes = new ArrayList<TreeView.FragmentNode>();
		
		public NodeGrid()
		{
			
		}
		
		public void Draw(Canvas surface, Camera camera)
		{
			synchronized(m_segments)
			{
				for(FragmentConnection connection : m_connections)
				{
					connection.Draw(surface, camera);
				}
				
				for(FragmentNode frag : m_nodes)
				{
					// temp
					Paint backgroundPaint = new Paint();
					backgroundPaint.setColor(Color.RED);
					Paint textPaint = new Paint();
					textPaint.setColor(Color.BLACK);
					
					
					frag.Draw(surface, camera, backgroundPaint, textPaint);
				}
			}
		}
		
		/**
		 * Set the fragments that are to be displayed by this component
		 */
		public void SetFragments(Map<String, StoryFragment> fragments)
		{
			// early out
			if(fragments == null)
			{
				return;
			}
			
			synchronized(m_segments)
			{
				// clear the list of segments as we rebuild
				m_segments.clear();
				m_connections.clear();
				m_nodes.clear();

				SetupNodes(fragments);
			}
		}
		
		private void SetupNodes(Map<String, StoryFragment> fragsMap)
		{
			NodePlacer nodePlacer = new NodePlacer();
			
			Set<FragmentNode> placedFragments = new HashSet<FragmentNode>();
			Set<StoryFragment> notPlacedFragments = new HashSet<StoryFragment>();
			
			notPlacedFragments.addAll(fragsMap.values());
			
			while(!notPlacedFragments.isEmpty())
			{
				// place the head node
				StoryFragment headFrag = notPlacedFragments.iterator().next();
				
				FragmentNode headNode = new FragmentNode(headFrag);
				nodePlacer.PlaceFragment(headNode);
				notPlacedFragments.remove(headFrag);
				placedFragments.add(headNode);
				m_nodes.add(headNode);
				
				// construct a list of nodes to place based upon the head node
				Set<StoryFragment> linkedFragments = GetLinkedFragments(headFrag, fragsMap);
				
				// place all linked nodes
				for(StoryFragment frag : linkedFragments)
				{
					FragmentNode nextNode = new FragmentNode(frag);
					nodePlacer.PlaceFragment(nextNode);
					notPlacedFragments.remove(frag);
					placedFragments.add(nextNode);
					m_nodes.add(nextNode);
				}
			}
			
			assert(notPlacedFragments.size() == 0);
		}
		
		private Set<StoryFragment> GetLinkedFragments(StoryFragment head, Map<String, StoryFragment> allFrags)
		{
			Set<StoryFragment> linkedFrags = new TreeSet<StoryFragment>();
			ArrayList<Choice> links = head.getChoices();
			
			if(links != null && !links.isEmpty())
			{
				do
				{
					Choice link = links.get(0);
					
					assert(allFrags.containsKey(link.getTarget()));
					
					StoryFragment frag = allFrags.get(link.getTarget());
					
					// if we don't already have it then add it to the list
					if(!linkedFrags.contains(frag))
					{
						linkedFrags.add(frag);
						links.addAll(frag.getChoices());
						links.remove(0);
					}
				}while(!links.isEmpty());
			}
			
			return linkedFrags;
		}
		
		private class NodePlacer
		{
			private static final int SEGMENT_SIZE = 100;
			
			ArrayList<GridSegment> m_gridSegments = new ArrayList<TreeView.NodeGrid.GridSegment>();
			Map<String, FragmentNode> m_placedNodes = new HashMap<String, TreeView.FragmentNode>();
			
			NodePlacer()
			{
				
			}
			
			public void PlaceFragment(FragmentNode fragment)
			{
				// placement is judged by weighting this nodes position against all of the
				// nodes it is linked too and then finding a spot that it fits that is near where
				// we started
				
				List<FragmentNode> linkedFrags = GetLinkedFrags(fragment);
				
				int centerX = 0;
				int centerY = 0;
				
				for(FragmentNode node : linkedFrags)
				{
					centerX += node.x;
					centerY += node.y;
				}
				
				if(linkedFrags.size() > 0)
				{
					centerX /= linkedFrags.size();
					centerY /= linkedFrags.size();
				}
				
				PlaceNear(centerX, centerY, fragment);
			}
			
			private void PlaceNear(int x, int y, FragmentNode fragment)
			{
				boolean isPlaced = false;
				
				// first try placing at the original point, if not we'll expand our search outwards
				fragment.x = x;
				fragment.y = y;
				isPlaced = TryPlace(fragment);
				
				if(!isPlaced)
				{
					// then we failed to place at the desired point
					// search outwards by trying locations and stopping
					// after a success
					final double EXPECTED_RADIUS = FragmentNode.WIDTH;
					final double VERTICAL_MOD = FragmentNode.HEIGHT / EXPECTED_RADIUS;
					final double HORIZONTAL_MOD = FragmentNode.WIDTH / EXPECTED_RADIUS;
					
					// select a random 45 degree angle
					double angle = (Math.random() * 7);
					angle *= 45;
					double radiusModifier = 1.0;
					double INITIAL_ANGLE = angle;
					
					while(!isPlaced)
					{
						fragment.x = (int) (Math.cos(angle) * EXPECTED_RADIUS * VERTICAL_MOD * radiusModifier);
						fragment.y = (int) (Math.sin(angle) * EXPECTED_RADIUS * HORIZONTAL_MOD * radiusModifier);
						isPlaced = TryPlace(fragment);
						
						angle += 45.0;
						if(angle - INITIAL_ANGLE > 360.0)
						{
							angle -= 360.0;
							radiusModifier++;
						}
					}
				}
			}
			
			private boolean TryPlace(FragmentNode fragment)
			{
				GridSegment gridSegment = GetGridSegmentAt(fragment.x, fragment.y);
				
				if(gridSegment.CanPlace(fragment))
				{
					gridSegment.Place(fragment);
					return true;
				}
				else
				{
					return false;
				}
			}
			
			private GridSegment GetGridSegmentAt(int x, int y)
			{
				GridSegment result = null;
				for(GridSegment segment : m_gridSegments)
				{
					if(segment.Contains(x, y))
					{
						result = segment;
					}
				}
				
				if(result == null)
				{
					// no segment is at that position yet
					// so we'll make one!
					
					// round x down to the nearest SEGMENT_SIZE
					// down is defined as closer to negative infinity
					int newX = (int) Math.floor(x / (float)SEGMENT_SIZE) * SEGMENT_SIZE;
					int newY = (int) Math.floor(y / (float)SEGMENT_SIZE) * SEGMENT_SIZE;
					
					result = new GridSegment(newX, newY, SEGMENT_SIZE, SEGMENT_SIZE);
					m_gridSegments.add(result);
				}
				
				return result;
			}
			
			private List<FragmentNode> GetLinkedFrags(FragmentNode fragment)
			{
				List<FragmentNode> result = new ArrayList<FragmentNode>();
				
				for(Choice adjacentIds : fragment.GetFragment().getChoices())
				{
					if(m_placedNodes.containsKey(adjacentIds.getTarget()))
					{
						result.add(m_placedNodes.get(adjacentIds.getTarget()));
					}
				}
				
				return result;
			}
			
			public ArrayList<GridSegment> GetSegments()
			{
				return m_gridSegments;
			}
		}
		
		/**
		 * Represents a section of the grid
		 * @author Jesse
		 */
		private class GridSegment extends Region
		{
			public static final int GRID_SIZE = 10;
			
			private boolean[][] m_space = null;
			
			public GridSegment(int x, int y, int width, int height)
			{
				super(x, y, width, height);
				m_space = new boolean[width / GRID_SIZE][height / GRID_SIZE];
			}
			
			public boolean IsEmpty(int x, int y)
			{
				return m_space[x][y];
			}
			
			public boolean CanPlace(Region region)
			{
				if(	region.x + region.width >= this.x + this.width ||
					region.y + region.height >= this.y + this.height)
				{
					// early out just in case we have a region that
					// is out of bounds on the right or bottom
					return false;
				}
				if(region.x < this.x || region.y < this.y)
				{
					// another early out for handling if we are
					// out of bounds on the top or left
					return false;
				}
				
				int baseX = (region.x - this.x) / GRID_SIZE;
				int baseY = (region.y - this.y) / GRID_SIZE;
				int endX = (region.x - this.x + region.width) / GRID_SIZE;
				int endY = (region.y - this.y + region.height) / GRID_SIZE;
				
				for(int currX = baseX ; currX <= endX ; ++currX)
				{
					for(int currY = baseY ; currY <= endY ; ++currY)
					{
						if(IsEmpty(currX, currY))
						{
							return false;
						}
					}
				}
				
				return true;
			}
			
			public void Place(Region region)
			{
				assert(CanPlace(region) == true);
				
				int baseX = (region.x - this.x) / GRID_SIZE;
				int baseY = (region.y - this.y) / GRID_SIZE;
				int endX = (region.x - this.x + region.width) / GRID_SIZE;
				int endY = (region.y - this.y + region.height) / GRID_SIZE;
				
				for(int currX = baseX ; currX <= endX ; ++currX)
				{
					for(int currY = baseY ; currY <= endY ; ++currY)
					{
						m_space[currX][currY] = true;
					}
				}
			}
		}
	}
	
	/**
	 * Represents a connection between two fragments
	 * @author Jesse
	 */
	private class FragmentConnection
	{
		public FragmentConnection(FragmentNode start, FragmentNode end, NodeGrid grid)
		{
			// use the provided info to build the path
		}
		
		public void Draw(Canvas surface, Camera camera)
		{
			// TODO::JT
		}
	}
	
	/**
	 * Represents a story fragment on the tree view
	 * @author Jesse
	 *
	 */
	private class FragmentNode extends Region
	{
		public static final int WIDTH = 40;
		public static final int HEIGHT = 25;
		private static final int MAX_TXT_LENGTH = 25;
		
		private StoryFragment m_fragment = null;
		private Rect m_displayRect = null;
		
		public FragmentNode(StoryFragment fragment)
		{
			super(0, 0, WIDTH, HEIGHT);
			this.m_fragment = fragment;
			this.m_displayRect = new Rect(0, 0, this.width, this.height);
		}
		
		public StoryFragment GetFragment()
		{
			return m_fragment;
		}
		
		public void Draw(Canvas surface, Camera camera, Paint background, Paint text)
		{
			if(m_displayRect != null)
			{
				
				
				// more hacky shit for now
				// transform the rectangle
				Region localCords = camera.GetLocalTransform(this);
				
				Rect dispRect = new Rect(localCords.x, localCords.y, localCords.width, localCords.height);
				
				//end hacky shit
				
				surface.drawRect(dispRect, background);
				
				String dispTxt = m_fragment.getStoryText();
				if(dispTxt.length() > MAX_TXT_LENGTH)
				{
					dispTxt = dispTxt.substring(0, MAX_TXT_LENGTH);
				}
				surface.drawText(m_fragment.getStoryText(), localCords.x, localCords.y, text);
			}
		}
	}
}
