package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Roupa;

public class InformacoesActivity extends Activity {
	
	private byte[] logoDaLoja;
	private String nomeDaLoja;
	private String categoriaDaRoupa;
	private String codigoDaRoupa;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Roupa roupa = (Roupa) getIntent().getExtras().get("roupaLoja");
		
		byte[] logoLojaSuperior = (byte[]) getIntent().getExtras().get("logoLojaSuperior");
		byte[] logoLojaInferior = (byte[]) getIntent().getExtras().get("logoLojaInferior");
		String codigoRoupaSuperior = (String) getIntent().getExtras().get("codigoRoupaSuperior");
		String codigoRoupaInferior = (String) getIntent().getExtras().get("codigoRoupaInferior");
		String nomeLojaSuperior = (String) getIntent().getExtras().get("nomeLojaSuperior");
		String nomeLojaInferior = (String) getIntent().getExtras().get("nomeLojaInferior");
		String categoriaRoupaSuperior = (String) getIntent().getExtras().get("categoriaRoupaSuperior");
		String categoriaRoupaInferior = (String) getIntent().getExtras().get("categoriaRoupaInferior");
		
		if (roupa != null) {
			Loja loja = roupa.getLoja();
			logoDaLoja = loja.getLogo();
			nomeDaLoja = loja.getNome();
			categoriaDaRoupa = roupa.getCategoria().getNome();
			codigoDaRoupa = roupa.getCodigo();
		} else {
			if (logoLojaInferior != null) {
				logoDaLoja = logoLojaInferior;
				nomeDaLoja = nomeLojaInferior;
				categoriaDaRoupa = categoriaRoupaInferior;
				codigoDaRoupa = codigoRoupaInferior;
			} else {
				logoDaLoja = logoLojaSuperior;
				nomeDaLoja = nomeLojaSuperior;
				categoriaDaRoupa = categoriaRoupaSuperior;
				codigoDaRoupa = codigoRoupaSuperior;
			}
			
		}
	
		ImageView logo = new ImageView(this);
		logo.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(logoDaLoja, 0, logoDaLoja.length)));
		
		TextView nomeLoja = new TextView(this);
		nomeLoja.setText("Loja: " + nomeDaLoja);
		nomeLoja.setTextSize(TypedValue.COMPLEX_UNIT_PX, 14);
		
		TextView infoCodigo = new TextView(this);
		infoCodigo.setText("Código: " + codigoDaRoupa);
		infoCodigo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 14);
		
		TextView infoCategoria = new TextView(this);
		infoCategoria.setText(categoriaDaRoupa);
		infoCategoria.setTextSize(TypedValue.COMPLEX_UNIT_PX, 14);
		
		LinearLayout maior = new LinearLayout(this);
		maior.setPadding(5, 5, 5, 5);
		LinearLayout info = new LinearLayout(this);
		info.setOrientation(LinearLayout.VERTICAL);
		info.addView(infoCategoria);
		info.addView(infoCodigo);
		info.addView(nomeLoja);
		info.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		maior.addView(logo);
		maior.addView(info);
		
		addContentView(maior, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	
	}
	
}
