package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.view.treeView.GridSegment;

class NodePlacer
{
	private static final int SEGMENT_SIZE = 800;
	
	ArrayList<GridSegment> m_gridSegments = new ArrayList<GridSegment>();
	Map<String, FragmentNode> m_placedNodes = new HashMap<String, FragmentNode>();
	
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
			final double EXPECTED_RADIUS = fragment.width * 2.0;
			final double VERTICAL_MOD = (double)fragment.height / (double)fragment.width;
			final double HORIZONTAL_MOD = (double)fragment.width / (double)fragment.width; // kinda reduntant...
			
			// select a random 45 degree angle
			double angle = (Math.random() * 7);
			angle *= 45;
			double radiusModifier = 1.0;
			
			angle = Math.toRadians(angle);
			
			double angleIncrement = Math.toRadians(45);
			double twoPi = Math.toRadians(360.0);
			double INITIAL_ANGLE = angle;
			
			while(!isPlaced)
			{
				fragment.x = (int) (Math.cos(angle) * EXPECTED_RADIUS * HORIZONTAL_MOD * radiusModifier) + x;
				fragment.y = (int) (Math.sin(angle) * EXPECTED_RADIUS * VERTICAL_MOD * radiusModifier) + y;
				isPlaced = TryPlace(fragment);
				
				angle += angleIncrement;
				if(angle - INITIAL_ANGLE > twoPi)
				{
					angle -= twoPi;
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
