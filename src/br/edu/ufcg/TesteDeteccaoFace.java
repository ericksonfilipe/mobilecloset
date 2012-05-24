package br.edu.ufcg;

//import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Categoria;
//import br.edu.ufcg.model.Roupa;

public class TesteDeteccaoFace  extends Activity {

	private boolean DEBUG = false;
	private boolean modificarLargura = true;
	private MyImageView myImageView;
	private BDAdapter dao;
//	private Roupa roupa;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new BDAdapter(this);
		
//		cadastrarModelosRoupas();

		byte[] imagem = dao.getManequimPadrao();

		Bitmap b = null;
		if (DEBUG) {
			b = BitmapFactory.decodeResource(getResources(), R.drawable.background_opcoes);
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		BitmapDrawable bd = new BitmapDrawable(girado);
		myImageView = new MyImageView(this);
		myImageView.setBackgroundDrawable(bd);
		setContentView(myImageView);

		ImageButton zoomIn = new ImageButton(this);
		zoomIn.setImageDrawable(getResources().getDrawable(R.drawable.zoom_in));
		zoomIn.setBackgroundColor(Color.TRANSPARENT);
		zoomIn.setOnClickListener(new AumentarListener(myImageView));

		ImageButton zoomOut = new ImageButton(this);
		zoomOut.setImageDrawable(getResources().getDrawable(R.drawable.zoom_out));
		zoomOut.setBackgroundColor(Color.TRANSPARENT);
		zoomOut.setOnClickListener(new DiminuirListener(myImageView));

		RelativeLayout layout = new RelativeLayout(this);
		LinearLayout linear = new LinearLayout(this);
		linear.addView(zoomOut);
		linear.addView(zoomIn);
		linear.setGravity(Gravity.RIGHT);
		linear.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layout.addView(linear);
		layout.setGravity(Gravity.BOTTOM);

		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	
//	public void cadastrarModelosRoupas() {
//		Bitmap bi = BitmapFactory.decodeResource(getResources(),
//                R.drawable.camisa_preta);
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
//		bi.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
//		byte[] bitmapdata = bos.toByteArray();
//
//		roupa = new Roupa(4, bitmapdata , Categoria.CAMISA);
//		
//		dao.inserirRoupa(roupa);
//		
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.calibragem_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.salvarCalibragem:
			myImageView.salvarCalibragem();
			break;
		case R.id.alterarDimensoes:
			modificarLargura = !modificarLargura;
			item.setTitle(modificarLargura ? "Alterar Altura" : "Alterar Largura");
			break;
		}
		return true;
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

		private int[] imagens = new int[] { R.drawable.modelo_camisa,
				R.drawable.modelo_calca, R.drawable.modelo_saia,
				R.drawable.modelo_camisa_manga_longa,
				R.drawable.modelo_vestido, R.drawable.modelo_camiseta,
				R.drawable.modelo_short };       
        
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
                Rect bounds = mImage.getBounds();

                Categoria[] categorias = new Categoria[] {};
                switch (posicao) {
                case 0:
                        categorias = new Categoria[] {Categoria.CAMISA};
                        break;
                case 1:
                        categorias = new Categoria[] {Categoria.CALCA};
                        break;
                case 2:
                        categorias = new Categoria[] {Categoria.SAIA};
                        break;
                case 3:
                		categorias = new Categoria[] {Categoria.CAMISA_MANGA_LONGA};
                		break;
                case 4:
            			categorias = new Categoria[] {Categoria.VESTIDO};
            			break;
                case 5:
                    categorias = new Categoria[] {Categoria.CAMISETA};
                    break;
                case 6:
                    categorias = new Categoria[] {Categoria.SHORT, Categoria.BERMUDA};
                    break;
                }
                
                Log.e("left", String.valueOf(bounds.centerX() - ((int)mImage.getIntrinsicWidth()/2)));
                Log.e("top", String.valueOf(bounds.centerY() - ((int)mImage.getIntrinsicHeight()/2)));
                Log.e("right", String.valueOf(bounds.centerX() + ((int)mImage.getIntrinsicWidth()/2)));
                Log.e("bottom", String.valueOf(bounds.centerY() + ((int)mImage.getIntrinsicHeight()/2)));
                
                for (Categoria categoria : categorias) {
                    dao.insertCalibragem(new Calibragem(categoria, bounds.left+(int)mPosX, bounds.top+(int)mPosY, bounds.right+(int)mPosX, bounds.bottom+(int)mPosY));
                        
                }

                posicao++;
                if (posicao >= imagens.length) {
                	Toast.makeText(getContext(), "Manequim escolhido com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                mImage = getResources().getDrawable(imagens[posicao]);
                mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
                invalidate();
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
	private class AumentarListener implements OnClickListener {
		private MyImageView view;
		public AumentarListener(MyImageView view) {
			this.view = view;
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
		public DiminuirListener(MyImageView view) {
			this.view = view;
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

