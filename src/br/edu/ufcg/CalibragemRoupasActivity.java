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
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Categoria;

public class CalibragemRoupasActivity  extends Activity {

	private Categoria categoria = Categoria.CAMISA;
	private Drawable image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String caminhoBackground = (String) getIntent().getExtras().get("background");
//		Bitmap b = carregarImagem(caminhoBackground);
				Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.background);
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

		private int zoomControler_w = 20;
		private int zoomControler_h = 20;

		private Integer x; 
		private Integer y;
		private boolean move = false;
		private boolean zoom = false;

		public Zoom(Context context) {
			super(context);
			x = 40;
			y = 40;
			image=context.getResources().getDrawable(R.drawable.icon);
			image.setBounds(x, y, 80, 80);
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

		//FIXME tá mto bugado isso =x
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
			case KeyEvent.KEYCODE_DPAD_CENTER:
				Log.e("salvando", "me chamou");
				BDAdapter dao = new BDAdapter(getApplicationContext());
				Rect bounds = image.getBounds();
				dao.insertCalibragem(new Calibragem(categoria, bounds.left, bounds.top, bounds.right, bounds.bottom));
				Log.e("deu certo?", dao.getCalibragens().get(categoria).toString());
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
