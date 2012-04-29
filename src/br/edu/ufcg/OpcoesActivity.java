package br.edu.ufcg;

import br.edu.ufcg.BD.BDAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class OpcoesActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	//criado para a v2
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opcoes_v2);

		Button bVisualizadorRoupas = (Button) findViewById(R.id.button_VisualizadorRoupas);
		bVisualizadorRoupas.setOnClickListener(this);
		
		Button bLooksSalvos = (Button) findViewById(R.id.button_LooksSalvos);
		bLooksSalvos.setOnClickListener(this);
		
		Button bDownloadColecoes = (Button) findViewById(R.id.button_DownloadColecoes);
		bDownloadColecoes.setOnClickListener(this);
		
		Button bEscolherManequim = (Button) findViewById(R.id.button_EscolherManequim);
		bEscolherManequim.setOnClickListener(this);
		
		Button bCadastrarRoupas = (Button) findViewById(R.id.button_CadastrarRoupas);
		bCadastrarRoupas.setOnClickListener(this);
		
		Button bSobre = (Button) findViewById(R.id.button_Sobre);
		bSobre.setOnClickListener(this);
		
		Button bSair = (Button) findViewById(R.id.button_sair_v2);
		bSair.setOnClickListener(this);


		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}	

	//criado para v2
	//@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_VisualizadorRoupas:
			i = new Intent(v.getContext(), ProvadorActivity.class);
			BDAdapter dao = new BDAdapter(this);
			if (dao.getManequimPadrao() == null) {
//			i.putExtra("manequimFaltando", true);
//			startActivity(i);
				Toast.makeText(this, "Não há manequim escolhido!", Toast.LENGTH_LONG).show();
			} else {
				i.putExtra("background", dao.getManequimPadrao());
				startActivity(i);
			}
			break;
		case R.id.button_LooksSalvos:
			//i = new Intent(v.getContext(), ManequimActivity.class);
			//startActivity(i);
			break;
		case R.id.button_DownloadColecoes:
			//i = new Intent(v.getContext(), ManequimActivity.class);
			//startActivity(i);
			break;
		case R.id.button_EscolherManequim:
			i = new Intent(v.getContext(), ManequimActivity.class);
			startActivity(i);
			break;
		case R.id.button_CadastrarRoupas:
			i = new Intent(v.getContext(), RoupasActivity.class);
			startActivity(i);
			break;
		case R.id.button_Sobre:
			i = new Intent(v.getContext(), Sobre_v2_Activity.class);
			startActivity(i);
			break;
		case R.id.button_sair_v2:
			finish();
			break;
		}
	}	

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//		
//		Button bTeste = (Button) findViewById(R.id.button_teste);
//		bTeste.setOnClickListener(this);
//
//		Button bManequim = (Button) findViewById(R.id.button_manequim);
//		bManequim.setOnClickListener(this);
//
//		Button bRoupas = (Button) findViewById(R.id.button_roupas);
//		bRoupas.setOnClickListener(this);
//		
//		Button bProvar = (Button) findViewById(R.id.button_provar);
//		bProvar.setOnClickListener(this);
//
//		Button bSair = (Button) findViewById(R.id.button_sair);
//		bSair.setOnClickListener(this);
//
//		Button bCreditos = (Button) findViewById(R.id.button_creditos);
//		bCreditos.setOnClickListener(this);
//
//		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//	}	
	
	
//	//@Override
//	public void onClick(View v) {
//		Intent i;
//		switch (v.getId()) {
//		case R.id.button_manequim:
//			i = new Intent(v.getContext(), ManequimActivity.class);
//			startActivity(i);
//			break;
//		case R.id.button_roupas:
//			i = new Intent(v.getContext(), RoupasActivity.class);
//			startActivity(i);
//			break;
//		case R.id.button_provar:
//			i = new Intent(v.getContext(), ProvadorActivity.class);
//			BDAdapter dao = new BDAdapter(this);
//			if (dao.getManequimPadrao() == null) {
////				i.putExtra("manequimFaltando", true);
////				startActivity(i);
//				Toast.makeText(this, "Não há manequim escolhido!", Toast.LENGTH_LONG).show();
//			} else {
//				i.putExtra("background", dao.getManequimPadrao());
//				startActivity(i);
//			}
//			break;
//		case R.id.button_creditos:
//			i = new Intent(v.getContext(), Creditos.class);
//			startActivity(i);
//			break;
//		case R.id.button_sair:
//			finish();
//			break;
//		case R.id.button_teste:
//			i = new Intent(v.getContext(), TesteDeteccaoFace.class);
//			startActivity(i);
//			break;
//		}
//	}
	

	
}