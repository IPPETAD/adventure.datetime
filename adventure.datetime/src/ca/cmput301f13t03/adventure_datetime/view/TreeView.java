package ca.cmput301f13t03.adventure_datetime.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IAllFragmentsListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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
	
	public TreeView(Context context) 
	{
		super(context);
		
		this.getHolder().addCallback(this);
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
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) 
	{
		// TODO What do I need to do here?
	}

	@Override
	public void surfaceCreated(SurfaceHolder surface) 
	{
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
			
			// clear canvas
			
			// draw stuffs
			m_grid.Draw(canvas);
			
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
		
		public void Draw(Canvas surface)
		{
			synchronized(m_segments)
			{
				for(FragmentConnection connection : m_connections)
				{
					connection.Draw(surface);
				}
				
				for(FragmentNode frag : m_nodes)
				{
					// temp
					Paint backgroundPaint = new Paint();
					backgroundPaint.setColor(Color.RED);
					Paint textPaint = new Paint();
					textPaint.setColor(Color.BLACK);
					frag.Draw(surface, backgroundPaint, textPaint);
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
		
		private void SetupNodes(Map<String, StoryFragment> frags)
		{
			Map<String, FragmentNode> setFragments = new HashMap<String, FragmentNode>();
			NodePlacer newPlacer = new NodePlacer();
			
			StoryFragment headFragment = null;
			// ugly, ugly, UGLY!
			for(StoryFragment frag : frags.values())
			{
				headFragment = frag;
				break;
			}
			
			if(headFragment != null)
			{
				FragmentNode headNode = new FragmentNode(headFragment);
				m_nodes.add(headNode);
				newPlacer.PlaceFragment(headNode);
				setFragments.put(headFragment.getFragmentID(), headNode);
				
				// list of fragments to be added to the view
				List<StoryFragment> nextFragments = new ArrayList<StoryFragment>();
				
				// fragments are inserted in a breadth first manner from the head node
				for(Choice choice : headFragment.getChoices())
				{
					StoryFragment t1Fragment = frags.get(choice.getTarget());
					nextFragments.add(t1Fragment);
				}
				
				// place all of the fragments in a breadth first ordering
				while(nextFragments.size() > 0)
				{
					StoryFragment nextFragment = nextFragments.get(0);
					
					// only if the fragment hasn't already been set
					if(!setFragments.containsKey(nextFragment.getFragmentID()))
					{
						// create a node and place it
						FragmentNode nodeFrag = new FragmentNode(nextFragment);
						newPlacer.PlaceFragment(nodeFrag);
						setFragments.put(nextFragment.getFragmentID(), nodeFrag);
						
						// get the adjacent nodes
						for(Choice choice : nextFragment.getChoices())
						{
							StoryFragment otherFragment = frags.get(choice.getTarget());
							nextFragments.add(otherFragment);
						}
					}
					
					nextFragments.remove(0);
				}
			}
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
				
				PlaceNear(centerX, centerY);
			}
			
			private void PlaceNear(int x, int y)
			{
				
			}
			
			private boolean TryPlace(int x, int y)
			{
				GridSegment gridSegment = GetGridSegmentAt(x, y);
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
					int newX = (int) Math.floor(x / 100.0) * 100;
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
			
			private boolean[][] m_space = new boolean[BASE_WIDTH / GRID_SIZE][BASE_HEIGHT / GRID_SIZE];
			
			public GridSegment(int x, int y, int width, int height)
			{
				super(x, y, width, height);
			}
			
			public boolean IsEmpty(int x, int y)
			{
				return m_space[x][y];
			}
			
			public boolean CanPlace(Region region)
			{
				int baseX = (region.x - this.x) / GRID_SIZE;
				int baseY = (region.y - this.y) / GRID_SIZE;
				int endX = (region.x - this.x + region.width) / GRID_SIZE;
				int endY = (region.y - this.y + region.height) / GRID_SIZE;
				
				for(int currX = baseX ; currX <= endX ; ++currX)
				{
					for(int currY = baseY ; currY <= endY ; ++currY)
					{
						if(m_space[currX][currY])
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
		
		public void Draw(Canvas surface)
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
		private static final int WIDTH = 125;
		private static final int HEIGHT = 125;
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
		
		public void Draw(Canvas surface, Paint background, Paint text)
		{
			if(m_displayRect != null)
			{
				surface.drawRect(m_displayRect, background);
				
				String dispTxt = m_fragment.getStoryText();
				if(dispTxt.length() > MAX_TXT_LENGTH)
				{
					dispTxt = dispTxt.substring(0, MAX_TXT_LENGTH);
				}
				surface.drawText(m_fragment.getStoryText(), this.x, this.y, text);
			}
		}
		
		public void SetPos(Point p)
		{
			this.x = p.x;
			this.y = p.y;
			this.m_displayRect = new Rect(0, 0, this.width, this.height);
		}
	}
}
