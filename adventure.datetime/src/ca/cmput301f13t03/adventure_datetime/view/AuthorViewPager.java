package ca.cmput301f13t03.adventure_datetime.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AuthorViewPager extends ViewPager
{
	public AuthorViewPager(Context context) 
	{
		super(context);
		this.setOffscreenPageLimit(AuthorEdit.INDEX_COUNT);
	}
	
	public AuthorViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setOffscreenPageLimit(AuthorEdit.INDEX_COUNT);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean result = false;
		if(this.getCurrentItem() == AuthorEdit.OVERVIEW_INDEX)
		{
			// just ignore it if we are on the overview screen
			result = false;
		}
		else
		{
			result = super.onTouchEvent(event);
		}
		return result;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		boolean result = false;
		if(this.getCurrentItem() == AuthorEdit.OVERVIEW_INDEX)
		{
			// just ignore it if we are on the overview screen
			result = false;
		}
		else
		{
			result = super.onInterceptTouchEvent(event);
		}
		return result;
	}
	
}
