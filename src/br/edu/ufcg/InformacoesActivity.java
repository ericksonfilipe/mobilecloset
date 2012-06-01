package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Roupa;

public class InformacoesActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Roupa roupa = (Roupa) getIntent().getExtras().get("roupaLoja");
		Loja loja = roupa.getLoja();
		
		
		ImageView infoLoja = new ImageView(this);
		infoLoja.setBackgroundDrawable(carregaDrawable(loja.getLogo()));
		
		TextView nomeLoja = new TextView(this);
		nomeLoja.setText("Loja: " + loja.getNome());
		
		TextView infoCodigo = new TextView(this);
		infoCodigo.setText("Codigo da roupa: " + roupa.getCodigo());
		
		RelativeLayout layoutCentral = new RelativeLayout(this);
		LinearLayout linearCentral = new LinearLayout(this);
		linearCentral.addView(infoLoja);
		linearCentral.addView(nomeLoja);
		linearCentral.addView(infoCodigo);
		linearCentral.setGravity(Gravity.RIGHT);
		linearCentral.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutCentral.addView(linearCentral);
		layoutCentral.setGravity(Gravity.BOTTOM);
		
		addContentView(layoutCentral, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	
	}
	
	private Drawable carregaDrawable(byte[] imagem) {
		Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		return new BitmapDrawable(girado);
	}

}
