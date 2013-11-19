package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.ArrayList;

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
		
	}
}
