package ca.cmput301f13t03.adventure_datetime.view.treeView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.view.treeView.Camera;
import ca.cmput301f13t03.adventure_datetime.view.treeView.Region;

/**
 * Represents a story fragment on the tree view
 * @author Jesse
 *
 */
class FragmentNode extends Region
{
	public static final int WIDTH = 120;
	public static final int HEIGHT = 75;
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
	
	public void Draw(Canvas surface, Camera camera, Paint background, Paint text)
	{
		if(m_displayRect != null)
		{
			// more hacky shit for now
			// transform the rectangle
			Region localCords = camera.GetLocalTransform(this);
			
			Rect dispRect = new Rect(	localCords.x, 
										localCords.y, 
										localCords.x + localCords.width, 
										localCords.y + localCords.height);
			
			//end hacky shit
			
			surface.drawRect(dispRect, background);
			
			String dispTxt = m_fragment.getStoryText();
			if(dispTxt.length() > MAX_TXT_LENGTH)
			{
				dispTxt = dispTxt.substring(0, MAX_TXT_LENGTH);
			}
			surface.drawText(m_fragment.getStoryText(), localCords.x, localCords.y, text);
		}
	}
}
