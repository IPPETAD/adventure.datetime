package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.view.treeView.Camera;

/**
 * Class that handles positioning of elements
 * @author Jesse
 */
class NodeGrid
{
	private static final String TAG = "NODE_GRID";
	
	private ArrayList<GridSegment> m_segments = new ArrayList<GridSegment>();
	private ArrayList<FragmentConnection> m_connections = new ArrayList<FragmentConnection>();
	private ArrayList<FragmentNode> m_nodes = new ArrayList<FragmentNode>();
	
	private Resources m_res = null;
	
	public NodeGrid(Resources res)
	{
		m_res = res;
	}
	
	public void Draw(Canvas surface, Camera camera)
	{
		synchronized(m_segments)
		{
			// TODO::JT remove this
			/*
			if(temp != null)
			{
				Paint tempPaintTrue = new Paint();
				tempPaintTrue.setColor(Color.GRAY);
				Paint tempPaintFalse = new Paint();
				tempPaintFalse.setColor(Color.LTGRAY);
				
				Paint edge = new Paint();
				edge.setColor(Color.BLACK);
				edge.setStyle(Style.STROKE);
				edge.setStrokeWidth(1.0f);
				
				Region reg = new Region(0, 0, temp.length * GridSegment.GRID_SIZE, temp[0].length * GridSegment.GRID_SIZE);
				Region localReg = camera.GetLocalTransform(reg);
				
				for(int x = 0 ; x < temp.length ; ++x)
				{
					for(int y = 0 ; y < temp[x].length ; ++y)
					{
						Paint color = null;
						
						if(temp[x][y])
						{
							color = tempPaintTrue;
						}
						else
						{
							color = tempPaintFalse;
						}
						
						surface.drawRect(new Rect(	
								x * GridSegment.GRID_SIZE + this.temp_h + localReg.x, 
								y * GridSegment.GRID_SIZE + temp_v + localReg.y, 
								x * GridSegment.GRID_SIZE + GridSegment.GRID_SIZE + temp_h + localReg.x, 
								y * GridSegment.GRID_SIZE + GridSegment.GRID_SIZE + temp_v + localReg.y), color);
						
						
						
						surface.drawRect(new Rect(	
								x * GridSegment.GRID_SIZE + this.temp_h + localReg.x, 
								y * GridSegment.GRID_SIZE + temp_v + localReg.y, 
								x * GridSegment.GRID_SIZE + GridSegment.GRID_SIZE + temp_h + localReg.x, 
								y * GridSegment.GRID_SIZE + GridSegment.GRID_SIZE + temp_v + localReg.y), edge);
						
					}
				}
			}*/
			
			for(FragmentConnection connection : m_connections)
			{
				connection.Draw(surface, camera);
			}
			
			for(FragmentNode frag : m_nodes)
			{
				frag.Draw(surface, camera);
			}
		}
	}
	
	/**
	 * Set the fragments that are to be displayed by this component
	 */
	public void SetFragments(Map<UUID, StoryFragment> fragments)
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
			SetupConnections();
		}
	}
	
	private void SetupNodes(Map<UUID, StoryFragment> fragsMap)
	{
		NodePlacer nodePlacer = new NodePlacer();
		
		Set<UUID> placedFragments = new HashSet<UUID>();
		Set<StoryFragment> notPlacedFragments = new HashSet<StoryFragment>();
		
		notPlacedFragments.addAll(fragsMap.values());
		
		while(!notPlacedFragments.isEmpty())
		{
			// place the head node
			StoryFragment headFrag = notPlacedFragments.iterator().next();
			
			FragmentNode headNode = new FragmentNode(headFrag, m_res);
			nodePlacer.PlaceFragment(headNode);
			notPlacedFragments.remove(headFrag);
			placedFragments.add(headFrag.getFragmentID());
			m_nodes.add(headNode);
			
			// construct a list of nodes to place based upon the head node
			Set<StoryFragment> linkedFragments = GetLinkedFragments(headFrag, fragsMap);
			
			// place all linked nodes
			for(StoryFragment frag : linkedFragments)
			{
				if(!placedFragments.contains(frag.getFragmentID()))
				{
					FragmentNode nextNode = new FragmentNode(frag, m_res);
					nodePlacer.PlaceFragment(nextNode);
					notPlacedFragments.remove(frag);
					placedFragments.add(frag.getFragmentID());
					m_nodes.add(nextNode);
				}
			}
		}
		
		assert(notPlacedFragments.size() == 0);
		this.m_segments = nodePlacer.GetSegments();
	}
	
	private void SetupConnections()
	{
		ConnectionPlacer placer = new ConnectionPlacer(this.m_segments, GridSegment.GRID_SIZE);
		Map<UUID, FragmentNode> lookupList = new HashMap<UUID, FragmentNode>();
		
		// construct the lookup map
		for(FragmentNode node : m_nodes)
		{
			lookupList.put(node.GetFragment().getFragmentID(), node);
		}
		
		// now iterate over each fragment node and connect it with its choices
		for(FragmentNode node : m_nodes)
		{
			List<Choice> links = node.GetFragment().getChoices();
			
			for(Choice choice : links)
			{
				UUID key = choice.getTarget();
				// lookup the node
				if(lookupList.containsKey(key))
				{
					FragmentConnection connection = new FragmentConnection();
					placer.PlaceConnection(connection, node, lookupList.get(key));
					this.m_connections.add(connection);
				}
				else
				{
					// What the hell? How could there be a choice without
					// a node?
					Log.e(TAG, "Choice with no associated node encountered! Discarding choice.");
				}
			}
		}
	}
	
	private Set<StoryFragment> GetLinkedFragments(StoryFragment head, Map<UUID, StoryFragment> allFrags)
	{
		Set<StoryFragment> linkedFrags = new TreeSet<StoryFragment>();
		List<Choice> links = new ArrayList<Choice>(head.getChoices());
		
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
	
}
