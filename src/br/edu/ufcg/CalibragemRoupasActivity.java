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
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Categoria;

public class CalibragemRoupasActivity  extends Activity {

	private boolean DEBUG = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String caminhoBackground = (String) getIntent().getExtras().get("background");

		Bitmap b = null;
		if (DEBUG) {
			b = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		} else {
			b = carregarImagem(caminhoBackground);
		}

		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		BitmapDrawable bd = new BitmapDrawable(girado);
		MyImageView myImageView = new MyImageView(this);
		myImageView.setBackgroundDrawable(bd);
		setContentView(myImageView);
	}

	public class MyImageView extends View {

		private static final int INVALID_POINTER_ID = -1;

		private Drawable mImage;
		private float mPosX;
		private float mPosY;

		private int zoomControler_w = 20;
		private int zoomControler_h = 20;

		private float mLastTouchX;
		private float mLastTouchY;
		private int mActivePointerId = INVALID_POINTER_ID;

		private ScaleGestureDetector mScaleDetector;
		private float mScaleFactor = 1.f;

		private boolean zoom = false;

		private int[] imagens = new int[] {R.drawable.molde_camisa, R.drawable.molde_calca, R.drawable.molde_saia};
		private int posicao = 0;

		public MyImageView(Context context) {
			this(context, null, 0);
			mImage = getResources().getDrawable(imagens[posicao]);

			mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());

			setFocusable(true);
			this.requestFocus();
			this.setFocusableInTouchMode(true);
		}

		public MyImageView(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public MyImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			// Let the ScaleGestureDetector inspect all events.
			mScaleDetector.onTouchEvent(ev);

			final int action = ev.getAction();
			switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				final float x = ev.getX();
				final float y = ev.getY();

				mLastTouchX = x;
				mLastTouchY = y;
				mActivePointerId = ev.getPointerId(0);
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				final int pointerIndex = ev.findPointerIndex(mActivePointerId);
				final float x = ev.getX(pointerIndex);
				final float y = ev.getY(pointerIndex);

				// Only move if the ScaleGestureDetector isn't processing a gesture.
				if (!mScaleDetector.isInProgress()) {
					final float dx = x - mLastTouchX;
					final float dy = y - mLastTouchY;

					mPosX += dx;
					mPosY += dy;

					invalidate();
				}

				mLastTouchX = x;
				mLastTouchY = y;

				break;
			}

			case MotionEvent.ACTION_UP: {
				mActivePointerId = INVALID_POINTER_ID;
				break;
			}

			case MotionEvent.ACTION_CANCEL: {
				mActivePointerId = INVALID_POINTER_ID;
				break;
			}

			case MotionEvent.ACTION_POINTER_UP: {
				final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
				>> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				final int pointerId = ev.getPointerId(pointerIndex);
				if (pointerId == mActivePointerId) {
					// This was our active pointer going up. Choose a new
					// active pointer and adjust accordingly.
					final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
					mLastTouchX = ev.getX(newPointerIndex);
					mLastTouchY = ev.getY(newPointerIndex);
					mActivePointerId = ev.getPointerId(newPointerIndex);
				}
				break;
			}
			}

			return true;
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			zoom = true;
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				zoomControler_h = 10;
				zoomControler_w = 0;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				zoomControler_w = 10;
				zoomControler_h = 0;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				zoomControler_h = -10;
				zoomControler_w = 0;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				zoomControler_w= -10;
				zoomControler_h = 0;
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				BDAdapter dao = new BDAdapter(getApplicationContext());
				Rect bounds = mImage.getBounds();

				Categoria[] categorias = new Categoria[] {};
				switch (posicao) {
				case 0:
					categorias = new Categoria[] {Categoria.CAMISA, Categoria.CAMISA_MANGA_LONGA, Categoria.CAMISETA, Categoria.VESTIDO};
					break;
				case 1:
					categorias = new Categoria[] {Categoria.CALCA, Categoria.SHORT};
					break;
				case 2:
					categorias = new Categoria[] {Categoria.SAIA};
					break;
				}

				for (Categoria categoria : categorias) {
					dao.insertCalibragem(new Calibragem(categoria, bounds.left, bounds.top, bounds.right, bounds.bottom));
				}

				posicao++;
				if (posicao >= imagens.length) {
					finish();
					return false;
				}

				mImage = getResources().getDrawable(imagens[posicao]);
				mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
				break;
			}

			invalidate();
			return true;
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			canvas.save();
			canvas.translate(mPosX, mPosY);
			canvas.scale(mScaleFactor, mScaleFactor);

			if (zoom) {
				int left = mImage.getBounds().left;
				int top = mImage.getBounds().top;
				int right = mImage.getBounds().right;
				int bottom = mImage.getBounds().bottom;
				mImage.setBounds(left-zoomControler_w, top-zoomControler_h, right+zoomControler_w, bottom+zoomControler_h);
				zoom = false;
			}
			mImage.draw(canvas);
			canvas.restore();
		}

		private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				mScaleFactor *= detector.getScaleFactor();

				// Don't let the object get too small or too large.
				mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

				invalidate();
				return true;
			}
		}

	}

	private Bitmap carregarImagem(String src) {
		try {
			String caminho = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + src;
			InputStream is = new BufferedInputStream(new FileInputStream(caminho));
			Bitmap d = BitmapFactory.decodeStream(is);
			return d;
		} catch (Exception e) {
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
