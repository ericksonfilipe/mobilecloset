//package br.edu.ufcg;
//
//import android.app.Activity;
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//
//public class ProvarActivity extends Activity {
//         /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.provar);
//        
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }
//    
//}

package br.edu.ufcg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Roupa;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class ProvarActivity extends Activity {
	
	private BDAdapter dh;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provar);
		
		this.dh = new BDAdapter(this);
		
		Provador provador = new Provador(this, carregaRoupasSuperiores(), carregaRoupasSuperiores());
		provador.setBackgroundColor(Color.WHITE);
		setContentView(provador);

	}

	private Drawable[] carregaRoupasSuperiores() {
		List<Roupa> listaDeRoupas = dh.getRoupas();
		
		//int[] ids = new int[] {R.drawable.molde_camisa, R.drawable.molde_camisao, R.drawable.molde_camiseta};
		//Drawable[] roupas = new Drawable[ids.length];
		Drawable[] roupas = new Drawable[listaDeRoupas.size()];
		
		for (int i = 0; i < roupas.length; i++) {
			//roupas[i] = getResources().getDrawable(0);
			roupas[i] = carregarRoupa(listaDeRoupas.get(i).getImagem().toString());

		}
		
//		
//		Drawable[] roupas = new Drawable[2];
//		
//		roupas[0] = findViewById(R.id.button_tirar_foto).getBackground();
//		roupas[1] = findViewById(R.id.button_manequim).getBackground();
		
//		Intent i = new Intent((findViewById(R.id.button_shorts)).getContext(), VisualizacaoDeRoupas.class);
//		startActivity(i);
		
		return null;
	}

	private Drawable[] carregaRoupasInferiores() {
		int[] ids = new int[] {R.drawable.molde_short, R.drawable.molde_calca, R.drawable.molde_saia};
		Drawable[] roupas = new Drawable[ids.length];
		for (int i = 0; i < roupas.length; i++) {
			roupas[i] = getResources().getDrawable(ids[i]);

		}
		return roupas;
	}
	
	private Drawable carregarRoupa(String src){
		try {
			//String caminho = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + src;
			InputStream is = new BufferedInputStream(new FileInputStream(src));
			Drawable d = Drawable.createFromStream(is, "src name");
			
			Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
			
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			Drawable d2 = new BitmapDrawable(girado);
			
			return d2;
		} catch (Exception e) {
			System.out.println("Exc="+e);
			return null;
		}
	}

	public class Provador extends View {

		private Drawable[] roupasSuperiores;
		private Drawable[] roupasInferiores;

		private int posicaoRoupaSuperior;
		private int posicaoRoupaInferior;

		private Drawable roupaSuperior;
		private Drawable roupaInferior;

		public Provador(Context context, Drawable[] roupasSuperiores, Drawable[] roupasInferiores) {
			super(context);
			this.roupasSuperiores = roupasSuperiores;
			this.roupasInferiores = roupasInferiores;
			roupaSuperior = roupasSuperiores[posicaoRoupaSuperior];
			roupaSuperior.setBounds(0, 0, roupaSuperior.getIntrinsicWidth(), roupaSuperior.getIntrinsicHeight());

			roupaInferior = roupasInferiores[posicaoRoupaInferior];
			roupaInferior.setBounds(0, roupaSuperior.getIntrinsicHeight(), roupaInferior.getIntrinsicWidth(), roupaSuperior.getIntrinsicHeight() + roupaInferior.getIntrinsicHeight());

			setFocusable(true);
			this.requestFocus();
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			roupaInferior.draw(canvas);
			roupaSuperior.draw(canvas);
		}

		public void proximaRoupaSuperior() {
			if (posicaoRoupaSuperior != roupasSuperiores.length-1) {
				posicaoRoupaSuperior++;
				Log.e("superior", String.valueOf(posicaoRoupaSuperior));
				roupaSuperior = roupasSuperiores[posicaoRoupaSuperior];
				roupaSuperior.setBounds(0, 0, roupaSuperior.getIntrinsicWidth(), roupaSuperior.getIntrinsicHeight());
				invalidate();
			}
		}

		public void voltaRoupaSuperior() {
			if (posicaoRoupaSuperior != 0) {
				posicaoRoupaSuperior--;
				Log.e("superior", String.valueOf(posicaoRoupaSuperior));
				roupaSuperior = roupasSuperiores[posicaoRoupaSuperior];
				roupaSuperior.setBounds(0, 0, roupaSuperior.getIntrinsicWidth(), roupaSuperior.getIntrinsicHeight());
				invalidate();
			}
		}

		public void proximaRoupaInferior() {
			if (posicaoRoupaInferior != roupasInferiores.length-1) {
				posicaoRoupaInferior++;
				roupaInferior = roupasInferiores[posicaoRoupaInferior];
				roupaInferior.setBounds(0, roupaSuperior.getIntrinsicHeight(), roupaInferior.getIntrinsicWidth(), roupaSuperior.getIntrinsicHeight() + roupaInferior.getIntrinsicHeight());
				invalidate();
			}
		}

		public void voltaRoupaInferior() {
			if (posicaoRoupaInferior != 0) {
				posicaoRoupaInferior--;
				roupaInferior = roupasInferiores[posicaoRoupaInferior];
				roupaInferior.setBounds(0, roupaSuperior.getIntrinsicHeight(), roupaInferior.getIntrinsicWidth(), roupaSuperior.getIntrinsicHeight() + roupaInferior.getIntrinsicHeight());
				invalidate();
			}
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				proximaRoupaSuperior();
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				voltaRoupaSuperior();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				proximaRoupaInferior();
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				voltaRoupaInferior();
				break;
			}
			return true;
		}

	}
}
