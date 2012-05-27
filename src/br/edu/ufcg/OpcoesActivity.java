package br.edu.ufcg;

import br.edu.ufcg.BD.BDAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class OpcoesActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	//criado para a v2
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opcoes_v2);

		Button bVisualizadorRoupas = (Button) findViewById(R.id.button_provador);
		bVisualizadorRoupas.setOnClickListener(this);
				
		Button bDownloadColecoes = (Button) findViewById(R.id.button_colecoes);
		bDownloadColecoes.setOnClickListener(this);
		
		Button bCadastrarRoupas = (Button) findViewById(R.id.button_closet);
		bCadastrarRoupas.setOnClickListener(this);
		
		Button bLooksSalvos = (Button) findViewById(R.id.button_looks);
		bLooksSalvos.setOnClickListener(this);
		
		Button bEscolherManequim = (Button) findViewById(R.id.button_manequim);
		bEscolherManequim.setOnClickListener(this);
				
		Button bSobre = (Button) findViewById(R.id.button_sobre);
		bSobre.setOnClickListener(this);
		
		ImageButton closeButton = new ImageButton(this);
		closeButton.setImageResource(R.drawable.close);
		closeButton.setBackgroundColor(Color.TRANSPARENT);
		closeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				moveTaskToBack(true); //esconde a aplicacao e deixa o Android decidir quando terminar.
				//android.os.Process.killProcess(android.os.Process.myPid()); //encerra realmente.
				//finish(); //modo "usual"; soh pega na primeira activity.
			}
		});
		
		RelativeLayout layoutClose = new RelativeLayout(this);
		LinearLayout linearClose = new LinearLayout(this);
		linearClose.addView(closeButton);
		linearClose.setGravity(Gravity.RIGHT);
		linearClose.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutClose.addView(linearClose);
		layoutClose.setGravity(Gravity.TOP);
		addContentView(layoutClose, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}	

	//criado para v2
	//@Override
	public void onClick(View v) {
		BDAdapter dao = new BDAdapter(this);
		Intent i;
		switch (v.getId()) {
		case R.id.button_provador:
			i = new Intent(v.getContext(), ProvadorActivity.class);
//			if (dao.getManequimPadrao() == null) {
//				Toast.makeText(this, "Não há manequim escolhido!", Toast.LENGTH_SHORT).show();
//			} else {
//				i.putExtra("background", dao.getManequimPadrao());
//				startActivity(i);
//			}
			i.putExtra("background", dao.getManequimPadrao());
			startActivity(i);
			break;
		case R.id.button_colecoes:
			if(temConexao()){
				i = new Intent(v.getContext(), LojasActivity.class);
				startActivity(i);
			}else{
				Toast.makeText (getApplicationContext(), "Sem conexão com a Internet!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button_closet:
			if (dao.getRoupas().isEmpty()) {
				Toast.makeText(this, "Não há roupas cadastradas", Toast.LENGTH_SHORT).show();
			}
			i = new Intent(v.getContext(), VerRoupasActivity.class);
			startActivity(i);
			break;
		case R.id.button_looks:
			if (dao.getLooks().isEmpty()) {
				Toast.makeText(this, "Não há looks cadastrados", Toast.LENGTH_SHORT).show();
			} else {
				i = new Intent(v.getContext(), FavoritosActivity.class);
				startActivity(i);				
			}
			break;
		case R.id.button_manequim:
			i = new Intent(v.getContext(), EscolherManequimActivity.class);
			startActivity(i);
			break;
		case R.id.button_sobre:
			i = new Intent(v.getContext(), SobreActivity.class);
			startActivity(i);
			break;
		}
	}
	
	private boolean temConexao() {
        boolean lblnRet = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                lblnRet = true;
            } else {
                lblnRet = false;
            }
        }catch (Exception e) {
        	e.getMessage();
        }
        return lblnRet;
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
//				Toast.makeText(this, "Não há manequim escolhido!", Toast.LENGTH_SHORT).show();
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