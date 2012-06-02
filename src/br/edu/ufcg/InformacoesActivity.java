package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Roupa;

public class InformacoesActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Roupa roupa = (Roupa) getIntent().getExtras().get("roupaLoja");
		Loja loja = roupa.getLoja();
		
		
		ImageView logo = new ImageView(this);
		logo.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(loja.getLogo(), 0, loja.getLogo().length)));
		
		TextView nomeLoja = new TextView(this);
		nomeLoja.setText("Loja: " + loja.getNome());
		
		TextView infoCodigo = new TextView(this);
		infoCodigo.setText("Codigo da roupa: " + roupa.getCodigo());
		
		LinearLayout maior = new LinearLayout(this);
		LinearLayout info = new LinearLayout(this);
		info.setOrientation(LinearLayout.VERTICAL);
		info.addView(nomeLoja);
		info.addView(infoCodigo);
		info.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		maior.addView(logo);
		maior.addView(info);
		
		addContentView(maior, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	
	}
	
}
