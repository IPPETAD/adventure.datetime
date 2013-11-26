package ca.cmput301f13t03.adventure_datetime.view.treeView;

import ca.cmput301f13t03.adventure_datetime.view.IFragmentSelected;
import android.view.MotionEvent;

class InputHandler 
{
	private Camera m_camera = null;
	private NodeGrid m_nodeGrid = null;
	
	// touch input
	private boolean m_hasPrimaryTouch = false;
	private int m_pointerId = 0; // only supporting single touch
	private boolean m_isDragging = false;
	
	private static final float DRAG_MINIMUM_SQUARED = 625.0f;
	private float m_initialX = 0;
	private float m_initialY = 0;
	private float m_previousX = 0;
	private float m_previousY = 0;
	
	private IFragmentSelected m_selectionCallback = null;
	
	private enum MoveType
	{
		TAP,
		DRAG
	}
	
	public InputHandler(Camera camera, NodeGrid nodeGrid)
	{
		m_camera = camera;
		m_nodeGrid = nodeGrid;
	}
	
	public boolean OnTouchAction(MotionEvent event)
	{
		boolean result = false;
		int pointerIndex = event.getActionIndex();
		int pointerId = event.getPointerId(pointerIndex);
		
		if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
		{
			HandleTouchDown(event, pointerId, pointerIndex);
		}
		else if(event.getActionMasked() == MotionEvent.ACTION_UP)
		{
			result = HandleTouchUp(event, pointerId, pointerIndex);
		}
		else if(event.getActionMasked() == MotionEvent.ACTION_MOVE)
		{
			result = HandleMove(event, pointerId, pointerIndex);
		}
		else if(event.getActionMasked() == MotionEvent.ACTION_CANCEL)
		{
			result = HandleCancel(event, pointerId);
		}
		
		return result;
	}
	
	public void SetSelectionCallback(IFragmentSelected callback)
	{
		m_selectionCallback = callback;
	}
	
	private boolean HandleTouchUp(MotionEvent event, int pointerId, int pointerIndex)
	{
		boolean result = false;
		
		if(m_hasPrimaryTouch && m_pointerId == pointerId)
		{
			// then the pointer was released
			// stop tracking
			m_pointerId = 0;
			m_hasPrimaryTouch = false;
			result = true;
			
			m_previousX = event.getX(pointerIndex);
			m_previousY = event.getY(pointerIndex);
			
			// and check for a tap event
			if(this.GetMoveType(m_previousX, m_previousY) == MoveType.TAP)
			{
				HandleTap(m_previousX, m_previousY);
			}
		}
		else
		{
			// some other pointer was released, don't care
			result = false;
		}
		
		return result;
	}
	
	private boolean HandleTouchDown(MotionEvent event, int pointerId, int pointerIndex)
	{
		boolean result = false;
		
		if(!m_hasPrimaryTouch)
		{
			m_pointerId = pointerId;
			m_previousX = event.getX(pointerIndex);
			m_previousY = event.getY(pointerIndex);
			m_initialX = m_previousX;
			m_initialY = m_previousY;
			m_hasPrimaryTouch = true;
			m_isDragging = false;
			
			result = true;
		}
		else
		{
			// reject input if we are already tracking a pointer
			result = false;
		}
		
		return result;
	}
	
	private boolean HandleMove(MotionEvent event, int pointerId, int pointerIndex)
	{
		boolean result = false;
		
		if(m_hasPrimaryTouch && m_pointerId == pointerId)
		{
			// only handle the move if it is the primary touch index
			result = true;
			
			float newX = event.getX(pointerIndex);
			float newY = event.getY(pointerIndex);
			
			// determine move type (tap or drag)
			MoveType type = GetMoveType(newX, newY);
			
			// perform action
			if(type == MoveType.DRAG)
			{
				HandleDrag(newX, newY);
			}
			// ignoring tap until the tap is released
			
			m_previousX = newX;
			m_previousY = newY;
		}
		
		return result;
	}
	
	private boolean HandleCancel(MotionEvent event, int pointerId)
	{
		boolean result = false;
		
		if(m_hasPrimaryTouch && m_pointerId == pointerId)
		{
			m_hasPrimaryTouch = false;
			m_pointerId = 0;
			result = true;
		}
		else
		{
			// reject input if we are already tracking a pointer
			result = false;
		}
		
		return result;
	}
	
	private void HandleDrag(float currX, float currY)
	{
		m_isDragging = true;
		
		float deltaX = currX - m_previousX;
		float deltaY = currY - m_previousY;
		
		float newX = m_camera.GetXTarget() - deltaX;
		float newY = m_camera.GetYTarget() - deltaY;
		
		m_camera.LookAt(newX, newY);
	}
	
	private void HandleTap(float currX, float currY)
	{
		// early out if we are dragging
		if(m_isDragging)
		{
			return;
		}
		
		float cords[] = { currX, currY };
		m_camera.ScreenCordsToWorldCords(cords);
		
		FragmentNode tappedNode = m_nodeGrid.GetNodeAtLocation((int)(cords[0]), (int)(cords[1]));
		
		if(tappedNode != null)
		{
			// the node was selected!
			if(m_selectionCallback != null)
			{
				m_selectionCallback.OnFragmentSelected(tappedNode.GetFragment());
			}
			// else... I guess no one will ever know...
		}
		// else we just ignore the tap, cuz there is nothing there!
	}
	
	private MoveType GetMoveType(float currX, float currY)
	{
		if(GetDistanceFromStartSquared(currX, currY) > DRAG_MINIMUM_SQUARED)
		{
			return MoveType.DRAG;
		}
		else
		{
			return MoveType.TAP;
		}
	}
	
	private float GetDistanceFromStartSquared(float currX, float currY)
	{
		return 	(currX - m_initialX)*(currX - m_initialX) +
				(currY - m_initialY)*(currY - m_initialY);
	}
}
