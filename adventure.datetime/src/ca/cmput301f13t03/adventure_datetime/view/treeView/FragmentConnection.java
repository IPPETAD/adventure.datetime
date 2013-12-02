package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.UUID;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import ca.cmput301f13t03.adventure_datetime.view.treeView.Camera;

/**
 * Represents a connection between two fragments
 * @author Jesse
 */
final class FragmentConnection
{
	private static Paint s_pathStyle = null;
	
	private Path m_connectionPath = null;
	private UUID m_originId = null;
	private UUID m_targetId = null;
	
	static
	{
		s_pathStyle = new Paint();
		s_pathStyle.setAntiAlias(true);
		s_pathStyle.setAlpha(200);
		s_pathStyle.setColor(Color.CYAN); // TODO::JT get a better colour!
		s_pathStyle.setStyle(Style.STROKE);
		s_pathStyle.setStrokeWidth(3.0f);
	}
	
	public FragmentConnection(UUID origin, UUID target)
	{
		m_originId = origin;
		m_targetId = target;
	}
	
	public void SetPath(Path path)
	{
		this.m_connectionPath = path;
	}
	
	public void Draw(Canvas surface, Camera camera)
	{
		camera.DrawLocal(surface, s_pathStyle, this.m_connectionPath);
	}
	
	public UUID GetOrigin()
	{
		return m_originId;
	}
	
	public UUID GetTarget()
	{
		return m_targetId;
	}
}