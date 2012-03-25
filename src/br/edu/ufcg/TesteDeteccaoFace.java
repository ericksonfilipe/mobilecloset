package br.edu.ufcg;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public class TesteDeteccaoFace extends Activity {
	private br.edu.ufcg.TesteDeteccaoFace.MyImageView myImageView;
	private Face[] arrayFaces;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		BitmapDrawable bd = carregaDrawable();
		myImageView = new MyImageView(this);
		myImageView.setBackgroundDrawable(bd);
		setContentView(myImageView);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}
	
	private BitmapDrawable carregaDrawable() {
		 Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.pessoa2);
		 
		 FaceDetector f = new FaceDetector(b.getWidth(), b.getHeight(), 1);
		 arrayFaces = new Face[1];
		 int numFace = f.findFaces(b,arrayFaces);
		
		
		 System.err.println("-------------- aqui: numero de faces: " + numFace);
		
		
		 Matrix matrix = new Matrix();
		 Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
		 matrix, true);
		 return new BitmapDrawable(girado);
		 }
	
	
	public class MyImageView extends View {

		private Drawable roupaInferior;
		private Drawable roupaSuperior;

		public MyImageView(Context context) {
			this(context, null, 0);
			
//			Bitmap camisa = BitmapFactory.decodeResource(getResources(), R.drawable.camisa);
//			Matrix matrix = new Matrix();
//			Bitmap girado = Bitmap.createBitmap(camisa, 0, 0, camisa.getWidth(), camisa.getHeight(), matrix, true);
//			
//			Face face = arrayFaces[0];
//			PointF point = new PointF();
//			face.getMidPoint(point);
			 
//			roupaSuperior = new BitmapDrawable(girado);
//			roupaSuperior.setBounds(point.x, (point.x + camisa.getWidth()), point.y, (point.y + camisa.getHeight()));

			this.setFocusable(true);
			this.requestFocus();
			this.setFocusableInTouchMode(true);
		}

		public MyImageView(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public MyImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (roupaInferior != null) {
				roupaInferior.draw(canvas);
			}
			if (roupaSuperior != null) {
				roupaSuperior.draw(canvas);
			}
		}

	}
}