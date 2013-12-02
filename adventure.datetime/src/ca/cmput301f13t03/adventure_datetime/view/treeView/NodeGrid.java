package ca.cmput301f13t03.adventure_datetime.view.treeView;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	ConnectionPlacer m_connectionPlacer = null;

	private StoryFragment m_selectedFrag = null;
	private FragmentNode m_selectedNode = null;

	private Resources m_res = null;
	private FragmentNode m_headNode = null;

	private Lock m_syncLock = new ReentrantLock();
	private Map<UUID, StoryFragment> m_fragments = null;
	private UUID m_headFragmentId = null;
	private volatile boolean m_reloadView = false;

	public NodeGrid(Resources res)
	{
		m_res = res;
	}

	public void Draw(Canvas surface, Camera camera)
	{
		if(m_syncLock.tryLock())
		{
			try
			{
				// early out, just in case the threads haven't yet
				// set up the fragments
				if(m_fragments == null)
				{
					return;
				}

				if(m_reloadView)
				{
					RebuildView();
					FragmentNode headNode = GetTopLevelFragment(m_headFragmentId);
					camera.LookAt(headNode.x + headNode.width / 2, headNode.y + headNode.height / 2);
				}

				for(FragmentConnection connection : m_connections)
				{
					connection.Draw(surface, camera);
				}

				for(FragmentNode frag : m_nodes)
				{
					frag.Draw(surface, camera);
				}
			}
			finally
			{
				m_syncLock.unlock();
			}
		}
	}

	public void RefreshView()
	{
		try
		{
			m_syncLock.lock();

			for(FragmentNode node : m_nodes)
			{
				node.RefreshContents();
			}
		}
		finally
		{
			m_syncLock.unlock();
		}
	}

	public void AddChoice(StoryFragment origin, Choice choice)
	{
		try
		{
			m_syncLock.lock();

			FragmentConnection newConnection = 
					new FragmentConnection(	origin.getFragmentID(),
							choice.getTarget(), m_res);
			FragmentNode originNode = GetNode(origin.getFragmentID());
			FragmentNode targetNode = GetNode(choice.getTarget());

			if(originNode != null && targetNode != null)
			{
				m_connectionPlacer.PlaceConnection(newConnection, originNode, targetNode);
				m_connections.add(newConnection);
			}
		}
		finally
		{
			m_syncLock.unlock();
		}
	}

	public void RemoveChoice(StoryFragment origin, Choice choice)
	{
		try
		{
			m_syncLock.lock();

			int toDelete = 0;
			int index = 0;
			UUID originId = origin.getFragmentID();
			UUID targetId = choice.getTarget();

			for(FragmentConnection connection : m_connections)
			{
				if(	connection.GetOrigin().equals(originId) &&
						connection.GetTarget().equals(targetId))
				{
					toDelete = index;
					break;
				}

				++index;
			}

			if(toDelete < m_connections.size())
			{
				m_connections.remove(toDelete);
			}
		}
		finally
		{
			m_syncLock.unlock();
		}
	}

	private FragmentNode GetNode(UUID fragId)
	{
		FragmentNode result = null;

		for(FragmentNode node : m_nodes)
		{
			if(node.GetFragment().getFragmentID().equals(fragId))
			{
				result = node;
				break;
			}
		}

		return result;
	}

	private void RebuildView()
	{
		// clear the list of segments as we rebuild
		m_segments.clear();
		m_connections.clear();
		m_nodes.clear();
		m_connectionPlacer = null;

		SetupNodes(m_fragments);
		SetupConnections();

		m_reloadView = false;
		SelectFragment(m_selectedFrag);
	}

	/**
	 * Set the fragments that are to be displayed by this component
	 */
	public void SetFragments(Map<UUID, StoryFragment> fragments, UUID headFragmentId)
	{
		m_syncLock.lock();
		try
		{
			m_headFragmentId = headFragmentId;
			m_fragments = fragments;
			m_reloadView = true;
		}
		finally
		{
			m_syncLock.unlock();
		}
	}

	public void SelectFragment(StoryFragment frag)
	{
		if(frag == null) return;

		if(m_selectedNode != null)
		{
			m_selectedNode.SetIsSelected(false);
		}

		m_selectedFrag = frag;

		if(m_nodes != null)
		{
			for(FragmentNode node : m_nodes)
			{
				if(node.GetFragment().getFragmentID().equals(frag.getFragmentID()))
				{
					m_selectedNode = node;
					m_selectedNode.SetIsSelected(true);
				}
			}
		}
	}

	public FragmentNode GetNodeAtLocation(int x, int y)
	{
		FragmentNode result = null;

		// ya, not the most efficent way to do it, but it works
		// would rather use a BSP tree, but that is just overkill...
		for(FragmentNode m_node : m_nodes)
		{
			if(m_node.Contains(x, y))
			{
				result = m_node;
			}
		}

		return result;
	}

	private FragmentNode GetTopLevelFragment(UUID headId)
	{
		if(m_headNode == null || !(m_headNode.GetFragment().getFragmentID().equals(headId)))
		{
			for(FragmentNode node : m_nodes)
			{
				if(node.GetFragment().getFragmentID().equals(headId))
				{
					m_headNode = node;
					break;
				}
			}
		}

		return m_headNode;
	}

	private void SetupNodes(Map<UUID, StoryFragment> fragsMap)
	{
		NodePlacer nodePlacer = new NodePlacer();

		Set<UUID> placedFragments = new HashSet<UUID>();
		Set<StoryFragment> notPlacedFragments = new HashSet<StoryFragment>();
		Set<UUID> candidates = new HashSet<UUID>();

		notPlacedFragments.addAll(fragsMap.values());
		candidates.add(m_headFragmentId);

		while(!notPlacedFragments.isEmpty())
		{
			// place the head node
			StoryFragment headFrag = SelectFragment(notPlacedFragments, candidates);

			AddNode(nodePlacer, notPlacedFragments, headFrag, placedFragments, candidates);

			// construct a list of nodes to place based upon the head node
			Set<StoryFragment> linkedFragments = GetLinkedFragments(headFrag, fragsMap);

			// place all linked nodes
			while(!linkedFragments.isEmpty())
			{
				StoryFragment frag = SelectFragment(linkedFragments, candidates);
				linkedFragments.remove(frag);

				if(!placedFragments.contains(frag.getFragmentID()))
				{
					AddNode(nodePlacer, notPlacedFragments, frag, placedFragments, candidates);
				}
			}
		}

		assert(notPlacedFragments.size() == 0);
		this.m_segments = nodePlacer.GetSegments();
	}

	private void AddNode(	
			NodePlacer nodePlacer, 
			Set<StoryFragment> notPlacedFragments, 
			StoryFragment frag,
			Set<UUID> placedFragments,
			Set<UUID> candidates)
	{
		FragmentNode nextNode = new FragmentNode(frag, m_res);
		nodePlacer.PlaceFragment(nextNode);
		notPlacedFragments.remove(frag);
		placedFragments.add(frag.getFragmentID());
		m_nodes.add(nextNode);
		
		for(Choice c : frag.getChoices())
		{
			candidates.add(c.getTarget());
		}
	}

	private StoryFragment SelectFragment(Set<StoryFragment> unPlaced, Set<UUID> criteria)
	{
		StoryFragment result = null;

		for(StoryFragment frag : unPlaced)
		{
			if(criteria.contains(frag.getFragmentID()))
			{
				result = frag;
				break;
			}
		}

		if(result == null)
		{
			result = unPlaced.iterator().next();
		}

		return result;
	}

	private void SetupConnections()
	{
		Map<UUID, FragmentNode> lookupList = new HashMap<UUID, FragmentNode>();

		// construct the lookup map
		for(FragmentNode node : m_nodes)
		{
			lookupList.put(node.GetFragment().getFragmentID(), node);
		}

		if(m_connectionPlacer == null)
		{
			m_connectionPlacer = new ConnectionPlacer(this.m_segments, GridSegment.GRID_SIZE);
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
					FragmentConnection connection = 
							new FragmentConnection(	node.GetFragment().getFragmentID(), 
									choice.getTarget(), m_res);
					m_connectionPlacer.PlaceConnection(connection, node, lookupList.get(key));
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
				if(frag != null && !linkedFrags.contains(frag))
				{
					linkedFrags.add(frag);
					links.addAll(frag.getChoices());
				}

				links.remove(0);
			}while(!links.isEmpty());
		}

		return linkedFrags;
	}

}
