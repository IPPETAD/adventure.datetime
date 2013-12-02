package ca.cmput301f13t03.adventure_datetime.view.treeView;

import java.util.UUID;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.Paint.Style;
import android.graphics.Path;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.view.treeView.Camera;

/**
 * Represents a connection between two fragments
 * @author Jesse
 */
final class FragmentConnection
{
	private static final int PARTICLE_H_OFFSET = 10;
	private static final int PARTICLE_V_OFFSET = 10;
	private static final float FRAME_ANIMATION_SPEED = 0.015f;
	
	private static Paint s_pathStyle = null;
	private static Paint s_particleStyle = null;
	private static Bitmap s_particle = null;
	
	private PathMeasure m_measure = null;
	private Path m_connectionPath = null;
	private float m_length = 0.0f;
	private float m_animationProgress = 0.0f;
	
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
		
		s_particleStyle = new Paint();
	}
	
	public FragmentConnection(UUID origin, UUID target, Resources res)
	{
		m_originId = origin;
		m_targetId = target;
		
		if(s_particle == null)
		{
			s_particle = BitmapFactory.decodeResource(res, R.drawable.particle);
		}
	}
	
	public void SetPath(Path path)
	{
		m_connectionPath = path;
		m_measure = new PathMeasure(m_connectionPath, false);
		m_length = m_measure.getLength();
	}
	
	public void Draw(Canvas surface, Camera camera)
	{
		camera.DrawLocal(surface, s_pathStyle, this.m_connectionPath);
		
		float pos[] = { 0.0f, 0.0f };
		if(m_measure.getPosTan(m_animationProgress * m_length, pos, null))
		{
			camera.DrawLocal(	surface, 
								s_particleStyle, 
								s_particle, 
								(int)(pos[0]) - PARTICLE_H_OFFSET, 
								(int)(pos[1]) - PARTICLE_V_OFFSET);
		}
		
		if(m_animationProgress >= 1.0f)
		{
			m_animationProgress = 0.0f;
		}
		
		m_animationProgress += FRAME_ANIMATION_SPEED;
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