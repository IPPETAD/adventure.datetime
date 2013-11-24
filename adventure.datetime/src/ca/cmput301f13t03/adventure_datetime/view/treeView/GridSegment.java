package ca.cmput301f13t03.adventure_datetime.view.treeView;

/**
 * Represents a section of the grid
 * @author Jesse
 */
public class GridSegment extends Region
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
		int localX = (x - this.x) / GRID_SIZE;
		int localY = (y - this.y) / GRID_SIZE;
		
		return m_space[localX][localY];
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
		
		int startX = region.x;
		int startY = region.y;
		int endX = region.x + region.width;
		int endY = region.y + region.height;
		
		for(int currX = startX ; currX < endX ; currX += GRID_SIZE)
		{
			for(int currY = startY ; currY < endY ; currY += GRID_SIZE)
			{
				if(IsEmpty(currX, currY))
				{
					return false;
				}
			}
		}
		
		// if we made it here then no collisions were detected
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
