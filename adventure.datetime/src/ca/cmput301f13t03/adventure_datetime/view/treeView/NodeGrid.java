package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
	
	boolean[][] temp = null;
	int temp_h = 0;
	int temp_v = 0;
	
	public NodeGrid()
	{
		
	}
	
	public void Draw(Canvas surface, Camera camera)
	{
		synchronized(m_segments)
		{
			if(temp != null)
			{
				Paint tempPaintTrue = new Paint();
				tempPaintTrue.setColor(Color.GRAY);
				Paint tempPaintFalse = new Paint();
				tempPaintFalse.setColor(Color.LTGRAY);
				
				Region reg = new Region(0, 0, temp.length * 10, temp[0].length * 10);
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
								x * 10 + this.temp_h + localReg.x, 
								y * 10 + temp_v + localReg.y, 
								x * 10 + 10 + temp_h + localReg.x, 
								y * 10 + 10 + temp_v + localReg.y), color);
					}
				}
			}
			
			for(FragmentNode frag : m_nodes)
			{
				frag.Draw(surface, camera);
			}
			
			for(FragmentConnection connection : m_connections)
			{
				connection.Draw(surface, camera);
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
			
			FragmentNode headNode = new FragmentNode(headFrag);
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
					FragmentNode nextNode = new FragmentNode(frag);
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

		this.temp = placer.m_map;
		this.temp_h = placer.m_horizontalOffset;
		this.temp_v = placer.m_verticalOffset;
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
