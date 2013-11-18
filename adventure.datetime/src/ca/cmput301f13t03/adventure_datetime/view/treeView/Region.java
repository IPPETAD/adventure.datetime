package ca.cmput301f13t03.adventure_datetime.view.treeView;

public class Region
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
