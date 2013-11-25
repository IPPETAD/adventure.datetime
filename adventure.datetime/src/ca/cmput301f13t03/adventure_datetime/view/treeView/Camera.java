package ca.cmput301f13t03.adventure_datetime.view.treeView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

class Camera
{
	// java generics are a pain to reuse just to make region use
	// floats or ints, not going to bother for this simple case
	private float x = -350.0f;
	private float y = -250.0f;
	
	private float m_zoomLevel = 1.0f;
	
	private Matrix m_transform = null;
	private Matrix m_inverse = null;
	
	public Camera()
	{
		
	}
	
	public void DrawLocal(Canvas canvas, Paint paint, Path p)
	{
		p.transform(this.GetTransform());
		canvas.drawPath(p, paint);
		p.transform(this.GetInverseTransform());
	}
	
	public void DrawLocal(Canvas canvas, Paint paint, Bitmap image, int x, int y)
	{
		Matrix trans = new Matrix(GetTransform());
		trans.setTranslate(x - this.x, y - this.y);
		canvas.drawBitmap(image, trans, paint);
	}
	
	public void DrawLocal(Canvas canvas, Paint paint, String text, int centerX, int centerY)
	{
		float points[] = { centerX, centerY };
		
		this.GetTransform().mapPoints(points);
		
		canvas.drawText(text, points[0], points[1], paint);
	}
	
	private Matrix GetTransform()
	{
		if(m_transform == null)
		{
			m_transform = new Matrix();
			m_transform.setScale(m_zoomLevel, m_zoomLevel);
			m_transform.setTranslate(-this.x, -this.y);
		}
		
		return m_transform;
	}
	
	private Matrix GetInverseTransform()
	{
		if(m_inverse == null)
		{
			m_inverse = new Matrix();
			m_inverse.setScale(1.0f / m_zoomLevel, 1.0f / m_zoomLevel);
			m_inverse.setTranslate(this.x, this.y);
		}
		
		return m_inverse;
	}
	
	public void SetPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
		m_transform = null;
	}
	
	public void SetZoom(float zoom)
	{
		assert(zoom > 0);
		
		m_zoomLevel = zoom;
		m_transform = null;
	}
}
