package br.edu.ufcg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.async.Connection;
import br.edu.ufcg.model.ToastPersonalizado;

public class OpcoesActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private ProgressDialog dialogoAguarde;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opcoes);

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
			
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}	

	
	//@Override
	public void onClick(View v) {
		BDAdapter dao = new BDAdapter(this);
		Intent i;
		if (v.getId() == R.id.button_provador) {
			i = new Intent(v.getContext(), ProvadorActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("background", dao.getManequimPadrao());
			startActivity(i);
		} else if (v.getId() == R.id.button_colecoes) {
			
			dialogoAguarde = ProgressDialog.show(OpcoesActivity.this,"","Aguarde...");
			new Thread() {
				public void run() {
					try { Thread.sleep(50); }
					catch (Exception e) { }
					dialogoAguarde.dismiss();
				}
			}.start();	
			
			if(temConexao()){
				if (!isServidorDown()) {
					i = new Intent(v.getContext(), LojasActivity.class);
					startActivity(i);					
				} else {
					new ToastPersonalizado(getApplicationContext(), Toast.LENGTH_SHORT, 
							"Coleções indisponíveis no momento.\nTente novamente mais tarde.").show();
				}
				
			}else{
				new ToastPersonalizado(getApplicationContext(), Toast.LENGTH_SHORT, 
						"Sem conexão com a Internet!").show();
			}
		} else if (v.getId() == R.id.button_closet) {
			if (dao.getRoupas().isEmpty()) {
				new ToastPersonalizado(this, Toast.LENGTH_SHORT, 
						"Não há roupas cadastradas").show();
			}
			i = new Intent(v.getContext(), VerRoupasActivity.class);
			startActivity(i);
		} else if (v.getId() == R.id.button_looks) {
			if (dao.getLooks().isEmpty()) {
				new ToastPersonalizado(this, Toast.LENGTH_SHORT, 
						"Não há looks cadastrados").show();
			} else {
				i = new Intent(v.getContext(), FavoritosActivity.class);
				startActivity(i);				
			}
		} else if (v.getId() == R.id.button_manequim) {
			i = new Intent(v.getContext(), EscolherManequimActivity.class);
			startActivity(i);
		} else if (v.getId() == R.id.button_sobre) {
			i = new Intent(v.getContext(), SobreActivity.class);
			startActivity(i);
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
	
	private boolean isServidorDown() {
		try {
			Connection.getStreamFor("loja/getLojas");
		} catch (Exception e) {
			return true;
		}
		return false;
	}
	
}