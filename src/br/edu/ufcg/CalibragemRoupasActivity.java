package br.edu.ufcg;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;

public class CalibragemRoupasActivity  extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new Zoom(this));
	}



	public class Zoom extends View {
		private Drawable image;
		private int zoomControler_w=20;
		private int zoomControler_h=20;
		public Zoom(Context context)
		{
			super(context);
			image=context.getResources().getDrawable(R.drawable.icon);
			setFocusable(true);

		}
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			//here u can control the width and height of the images........ this line is very important
			image.setBounds((getWidth()/2)-zoomControler_w, (getHeight()/2)-zoomControler_h, (getWidth()/2)+zoomControler_w, (getHeight()/2)+zoomControler_h);
			image.draw(canvas);
		}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				zoomControler_h+=10;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				zoomControler_w+=10;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				zoomControler_h-=10;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				zoomControler_w-=10;
				break;
			}

			if (zoomControler_w < 10) zoomControler_w = 10;
			if (zoomControler_h < 10) zoomControler_h =10;
			invalidate();
			return true;
		}
	}
}
