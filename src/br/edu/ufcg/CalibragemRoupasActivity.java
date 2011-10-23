package br.edu.ufcg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class CalibragemRoupasActivity  extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String caminhoBackground = (String) getIntent().getExtras().get("background");
		Bitmap b = carregarImagem(caminhoBackground);
//		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		BitmapDrawable bd = new BitmapDrawable(girado);
		Zoom z = new Zoom(this);
//		Drawable d = carregarImagem(caminhoBackground);
		z.setBackgroundDrawable(bd);
//		z.setBackgroundDrawable(d);
//		Log.e("OMG", d.toString());
		setContentView(z);
	}

	public class Zoom extends View {

		private Drawable image;
		private int zoomControler_w = 20;
		private int zoomControler_h = 20;
		
		private Integer x; 
		private Integer y;
		private boolean move = false;
		private boolean zoom = false;

		public Zoom(Context context) {
			super(context);
			x = getWidth()/2;;
			y = getHeight()/2;
			image=context.getResources().getDrawable(R.drawable.icon);
			image.setBounds(x, y, 40, 40);
			setFocusable(true);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if (move) {
				image.setBounds(x, y, (int)x + getLargura(), (int)y + getAltura());
			}
			if (zoom) {
				image.setBounds(x-zoomControler_w, y-zoomControler_h, x+zoomControler_w, y+zoomControler_h);
			}

			//			canvas.save();
			image.draw(canvas);
			//			canvas.rotate(90);
			//			canvas.restore();
			zoom = move = false;

		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			zoom = true;
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
			if (zoomControler_h < 10) zoomControler_h = 10;
			invalidate();
			return true;
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			move = true;
			x = (int) (event.getX() - getLargura()/2);
			y = (int) (event.getY() - getAltura()/2);
			invalidate();
			return true;
		}

		private int getLargura() {
			return image.getBounds().right - image.getBounds().left;
		}
		
		private int getAltura() {
			return image.getBounds().top - image.getBounds().bottom;
		}
	}
	
	private Bitmap carregarImagem(String src) {
	      try {
	    	  String caminho = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + src;
	          InputStream is = new BufferedInputStream(new FileInputStream(caminho));
	          Bitmap d = BitmapFactory.decodeStream(is);
	          return d;
	      } catch (Exception e) {
	          System.out.println("Exc="+e);
	          return null;
	      }
	}

//	private Drawable carregarImagem(String src) {
//	      try {
//	    	  String caminho = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + src;
//	          InputStream is = new BufferedInputStream(new FileInputStream(caminho));
//	          Drawable d = Drawable.createFromStream(is, "src name");
//	          return d;
//	      } catch (Exception e) {
//	          return null;
//	      }
//	}
}
