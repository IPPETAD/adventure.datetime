package ca.cmput301f13t03.adventure_datetime.view;

import java.util.ArrayList;
import java.util.Map;

import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IAllFragmentsListener;
import android.content.Context;
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
			// draw stuffs
			m_grid.Draw(m_surface);
			
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
		
		public void Draw(SurfaceHolder surface)
		{
			synchronized(m_segments)
			{
				
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
				
				for(FragmentConnection connection : m_connections)
				{
					connection.Draw(m_surface);
				}
				
				for(FragmentNode frag : m_nodes)
				{
					frag.Draw(m_surface);
				}
			}
		}
		
		private void PlaceFragment(FragmentNode fragment)
		{
			
		}
		
		/**
		 * Represents a section of the grid
		 * @author Jesse
		 */
		private class GridSegment
		{
			private static final int BASE_WIDTH = 50;
			private static final int BASE_HEIGHT = 30;
			
			private int x = 0;
			private int y = 0;
			private int width = 0;
			private int height = 0;
			
			public GridSegment(int x, int y, int width, int height)
			{
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
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
		
		public void Draw(SurfaceHolder surface)
		{
			// TODO::JT
		}
	}
	
	/**
	 * Represents a story fragment on the tree view
	 * @author Jesse
	 *
	 */
	private class FragmentNode
	{
		private StoryFragment m_fragment = null;
		
		public FragmentNode(StoryFragment fragment)
		{
			this.m_fragment = fragment;
		}
		
		public void Draw(SurfaceHolder surface)
		{
			// TODO::JT
		}
	}
}
