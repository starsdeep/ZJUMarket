package net.loonggg.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;


public class DrawView extends View{

	/**
	 * @param args
	 */
	
	public DrawView(Context context,AttributeSet attrs){
		super(context,attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);

		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);
		canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/3, paint);
		canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/3-canvas.getWidth()/15, paint);
		canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/3-2*canvas.getWidth()/15, paint);
		canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/3-3*canvas.getWidth()/15, paint);
		paint.setColor(Color.GREEN);
		canvas.drawLine(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/2+canvas.getWidth()/3,canvas.getHeight()/2, paint);
		
				
	}

	
	
}
