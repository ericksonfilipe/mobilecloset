package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SobreActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sobre);
		
		TextView textoSuperior = new TextView(this);
		textoSuperior.setBackgroundColor(Color.TRANSPARENT);
		textoSuperior.setText("\nDesenvolvido por Erickson Guedes, Izabela Melo, Janderson Aguiar e Savyo Nóbrega");
		textoSuperior.setTextScaleX((float) 0.9);
		textoSuperior.setTypeface(Typeface.DEFAULT_BOLD);
		textoSuperior.setTextColor(Color.BLACK);
		textoSuperior.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		
		TextView textoInferior = new TextView(this);
		textoInferior.setBackgroundColor(Color.TRANSPARENT);
		textoInferior.setText("O Mobile Closet é um closet virtual em que é possível fazer o download dos closets de lojas famosas. " +
				"Além disso, ele armazena as roupas do usuário e faz a combinação delas, facilitando, assim, a escolha da roupa.");
		textoInferior.setTextScaleX((float) 1.2);
		textoInferior.setTypeface(Typeface.DEFAULT_BOLD);
		textoInferior.setTextColor(Color.BLACK);
		textoInferior.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		
		RelativeLayout layoutSuperior = new RelativeLayout(this);
		LinearLayout linearAnterior = new LinearLayout(this);
		linearAnterior.addView(textoSuperior);
		linearAnterior.setGravity(Gravity.TOP);
		linearAnterior.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutSuperior.addView(linearAnterior);
		layoutSuperior.setGravity(Gravity.TOP);
		
		RelativeLayout layoutInferior = new RelativeLayout(this);
		LinearLayout linearInferior = new LinearLayout(this);
		linearInferior.addView(textoInferior);
		linearInferior.setGravity(Gravity.BOTTOM);
		linearInferior.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutInferior.addView(linearInferior);
		layoutInferior.setGravity(Gravity.BOTTOM);
		
		addContentView(layoutSuperior, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutInferior, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

}