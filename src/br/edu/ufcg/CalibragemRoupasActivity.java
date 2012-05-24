package br.edu.ufcg;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Calibragem2;
import br.edu.ufcg.model.Roupa;

public class CalibragemRoupasActivity  extends Activity {

	private boolean DEBUG = false;
//	private boolean modificarLargura = true;
	private MyImageView myImageView;
	private Calibragem2 calibragemRoupa;
	private Calibragem calibragemModelo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Roupa roupa = (Roupa) getIntent().getExtras().get("roupa");

		BDAdapter dao = new BDAdapter(this);
		byte[] imagem = dao.getManequimPadrao();
		calibragemModelo = dao.getCalibragens().get(roupa.getCategoria());
		
		calibragemRoupa = dao.getCalibragens2().get(roupa.getId());
		if(calibragemRoupa == null) {
			if (calibragemModelo == null) {
				calibragemRoupa = new Calibragem2(roupa.getId(), 0, 0, 100, 100);	
			} else {
				calibragemRoupa = new Calibragem2(roupa.getId(), calibragemModelo.left, calibragemModelo.top, calibragemModelo.right, calibragemModelo.bottom);				
			}
		}

		Bitmap b = null;
		if (DEBUG) {
			b = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		BitmapDrawable bd = new BitmapDrawable(girado);
		myImageView = new MyImageView(this, roupa);
		myImageView.setBackgroundDrawable(bd);
		setContentView(myImageView);

		ImageButton zoomInAltura = new ImageButton(this);
		zoomInAltura.setImageDrawable(getResources().getDrawable(R.drawable.up));
		zoomInAltura.setBackgroundColor(Color.TRANSPARENT);
		zoomInAltura.setOnClickListener(new AumentarListener(myImageView, false));

		ImageButton zoomOutAltura = new ImageButton(this);
		zoomOutAltura.setImageDrawable(getResources().getDrawable(R.drawable.bottom));
		zoomOutAltura.setBackgroundColor(Color.TRANSPARENT);
		zoomOutAltura.setOnClickListener(new DiminuirListener(myImageView, false));
		
		ImageButton zoomInLargura = new ImageButton(this);
		zoomInLargura.setImageDrawable(getResources().getDrawable(R.drawable.right));
		zoomInLargura.setBackgroundColor(Color.TRANSPARENT);
		zoomInLargura.setOnClickListener(new AumentarListener(myImageView, true));

		ImageButton zoomOutLargura = new ImageButton(this);
		zoomOutLargura.setImageDrawable(getResources().getDrawable(R.drawable.left));
		zoomOutLargura.setBackgroundColor(Color.TRANSPARENT);
		zoomOutLargura.setOnClickListener(new DiminuirListener(myImageView, true));
		
		ImageButton salvaButton = new ImageButton(this);
		salvaButton.setImageDrawable(getResources().getDrawable(R.drawable.add));
		salvaButton.setBackgroundColor(Color.TRANSPARENT);
		salvaButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				myImageView.salvarCalibragem();
			}
		});

		RelativeLayout layoutDireito = new RelativeLayout(this);
		LinearLayout linearDireito = new LinearLayout(this);
		linearDireito.addView(zoomOutLargura);
		linearDireito.addView(zoomInLargura);
		linearDireito.setGravity(Gravity.RIGHT);
		linearDireito.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutDireito.addView(linearDireito);
		layoutDireito.setGravity(Gravity.BOTTOM);
		
		RelativeLayout layoutEsquerdo = new RelativeLayout(this);
		LinearLayout linearEsquerdo = new LinearLayout(this);
		linearEsquerdo.addView(zoomInAltura);
		linearEsquerdo.addView(zoomOutAltura);
		linearEsquerdo.setGravity(Gravity.LEFT);
		linearEsquerdo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		linearEsquerdo.setOrientation(LinearLayout.VERTICAL);
		layoutEsquerdo.addView(linearEsquerdo);
		layoutEsquerdo.setGravity(Gravity.BOTTOM);
		
		RelativeLayout layoutCentral = new RelativeLayout(this);
		LinearLayout linearCentral = new LinearLayout(this);
		linearCentral.addView(salvaButton);
		linearCentral.setGravity(Gravity.CENTER);
		linearCentral.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutCentral.addView(linearCentral);
		layoutCentral.setGravity(Gravity.BOTTOM);

		addContentView(layoutDireito, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutEsquerdo, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutCentral, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.calibragem_menu, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.salvarCalibragem:
//			myImageView.salvarCalibragem();
//			break;
//		case R.id.alterarDimensoes:
//			modificarLargura = !modificarLargura;
//			item.setTitle(modificarLargura ? "Alterar Altura" : "Alterar Largura");
//			break;
//		}
//		return true;
//	}

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

		public MyImageView(Context context, Roupa roupa) {
			this(context, null, 0);
			this.mImage = carregaDrawable(roupa.getImagem());
			this.mImage.setBounds(calibragemRoupa.left, calibragemRoupa.top, calibragemRoupa.right, calibragemRoupa.bottom);

			this.setFocusable(true);
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

		private Drawable carregaDrawable(byte[] imagem) {
			Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			return new BitmapDrawable(girado);
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

		public void zoomInLargura() {
			zoom = true;
			zoomControler_w = 10;
			zoomControler_h = 0;
			invalidate();
		}

		public void zoomOutLargura() {
			zoom = true;
			zoomControler_w = -10;
			zoomControler_h = 0;
			invalidate();
		}

		public void zoomInAltura() {
			zoom = true;
			zoomControler_h = 10;
			zoomControler_w = 0;
			invalidate();
		}

		public void zoomOutAltura() {
			zoom = true;
			zoomControler_h = -10;
			zoomControler_w = 0;
			invalidate();
		}

		public void salvarCalibragem() {
			BDAdapter dao = new BDAdapter(getApplicationContext());

			calibragemRoupa.left += (int)mPosX;
			calibragemRoupa.top += (int)mPosY;
			calibragemRoupa.right += (int)mPosX;
			calibragemRoupa.bottom += (int)mPosY;
			dao.atualizaCalibragem2(calibragemRoupa);

			finish();
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			canvas.save();
			canvas.translate(mPosX, mPosY);
			canvas.scale(mScaleFactor, mScaleFactor);

			if (zoom) {
				calibragemRoupa.left = mImage.getBounds().left - zoomControler_w;
				calibragemRoupa.top = mImage.getBounds().top - zoomControler_h;
				calibragemRoupa.right = mImage.getBounds().right + zoomControler_w;
				calibragemRoupa.bottom = mImage.getBounds().bottom + zoomControler_h;
				mImage.setBounds(calibragemRoupa.left, calibragemRoupa.top, calibragemRoupa.right, calibragemRoupa.bottom);
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

	private class AumentarListener implements OnClickListener {
		private MyImageView view;
		private boolean modificarLargura;
		
		public AumentarListener(MyImageView view, boolean modificarLargura) {
			this.view = view;
			this.modificarLargura = modificarLargura;
		}

		public void onClick(View arg0) {
			if (modificarLargura) {
				view.zoomInLargura();
			} else {
				view.zoomInAltura();
			}
		}
	}

	private class DiminuirListener implements OnClickListener {
		private MyImageView view;
		private boolean modificarLargura;
		
		public DiminuirListener(MyImageView view, boolean modificarLargura) {
			this.view = view;
			this.modificarLargura = modificarLargura;
		}

		public void onClick(View arg0) {
			if (modificarLargura) {
				view.zoomOutLargura();
			} else {
				view.zoomOutAltura();
			}
		}
	}
}
