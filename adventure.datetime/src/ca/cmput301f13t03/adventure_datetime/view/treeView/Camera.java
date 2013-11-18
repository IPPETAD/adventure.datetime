package ca.cmput301f13t03.adventure_datetime.view.treeView;

class Camera
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
