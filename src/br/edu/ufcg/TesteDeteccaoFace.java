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
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class TesteDeteccaoFace extends Activity {
	private br.edu.ufcg.TesteDeteccaoFace.MyImageView myImageView;
	private Face[] arrayFaces;
	private LinearLayout layout;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.pessoa2);
		 
		 FaceDetector f = new FaceDetector(b.getWidth(), b.getHeight(), 1);
		 arrayFaces = new Face[1];
		 int numFace = f.findFaces(b,arrayFaces);
		
		
		 System.err.println("-------------- aqui: numero de faces: " + numFace);
		
		
		 Matrix matrix = new Matrix();
		 Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
		 matrix, true);
		
		BitmapDrawable bd = new BitmapDrawable(girado);
		myImageView = new MyImageView(this);
		myImageView.setBackgroundDrawable(bd);
		setContentView(myImageView);
		
		
		Bitmap camisa = BitmapFactory.decodeResource(getResources(), R.drawable.camisa_preta);
		Matrix matrix1 = new Matrix();
		Bitmap girado1 = Bitmap.createBitmap(camisa, 0, 0, camisa.getWidth(), camisa.getHeight(), matrix1, true);
		
		Face face = arrayFaces[0];
		PointF point = new PointF();
		face.getMidPoint(point);
		
		System.out.println("x: "+point.x);
		System.out.println((int)point.x);
		System.out.println("y: " + point.y);
		System.out.println((int)point.y);
		
		layout = new LinearLayout(this);
		layout.scrollTo(3*(int)point.x, 100+(int)point.y);
		layout.setBackgroundDrawable(new BitmapDrawable(girado1));
//	    layout.setBackgroundResource(R.drawable.camisa_preta);
	        
	    addContentView(layout, new LayoutParams(camisa.getWidth(), camisa.getHeight()));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}
	
//	private BitmapDrawable carregaDrawable() {
//		 Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.pessoa2);
//		 
//		 FaceDetector f = new FaceDetector(b.getWidth(), b.getHeight(), 1);
//		 arrayFaces = new Face[1];
//		 int numFace = f.findFaces(b,arrayFaces);
//		
//		
//		 System.err.println("-------------- aqui: numero de faces: " + numFace);
//		
//		
//		 Matrix matrix = new Matrix();
//		 Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
//		 matrix, true);
//		 return new BitmapDrawable(girado);
//		 }
	
	
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
//			
//			roupaSuperior = new BitmapDrawable(girado);
////			int f = (int) point.x + camisa.getWidth();
////			int g = (int) point.y + camisa.getHeight();
////			roupaSuperior.setBounds(f, (int)point.x, g,(int)point.y);
//			invalidate();
			
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