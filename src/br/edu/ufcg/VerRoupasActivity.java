package br.edu.ufcg;

import java.io.Serializable;

import br.edu.ufcg.model.Categoria;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VerRoupasActivity extends Activity implements OnClickListener {
	 /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ver_roupas);

		Button bShorts = (Button) findViewById(R.id.button_shorts);
		bShorts.setOnClickListener(this);
		
		Button bBermudas = (Button) findViewById(R.id.button_bermudas);
		bBermudas.setOnClickListener(this);

		Button bCalcas = (Button) findViewById(R.id.button_calcas);
		bCalcas.setOnClickListener(this);
		
		Button bSaias = (Button) findViewById(R.id.button_saias);
		bSaias.setOnClickListener(this);

		Button bCamisas = (Button) findViewById(R.id.button_camisas);
		bCamisas.setOnClickListener(this);
		
		Button bVestidos = (Button) findViewById(R.id.button_vestidos);
		bVestidos.setOnClickListener(this);
		
		 this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	//@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_shorts:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.SHORT);
			startActivity(i);
			break;
		case R.id.button_bermudas:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.BERMUDA);
			startActivity(i);
			break;
		case R.id.button_calcas:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.CALCA);
			startActivity(i);
			break;
		case R.id.button_saias:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.SAIA);
			startActivity(i);
			break;
		case R.id.button_vestidos:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.VESTIDO);
			startActivity(i);
			break;	
		case R.id.button_camisas:
			i = new Intent(v.getContext(), VerCamisasActivity.class);
			startActivity(i);
			break;		
		}
	}
}