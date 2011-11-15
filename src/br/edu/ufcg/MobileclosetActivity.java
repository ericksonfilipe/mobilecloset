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

public class MobileclosetActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button bManequim = (Button) findViewById(R.id.button_manequim);
		bManequim.setOnClickListener(this);

		Button bRoupas = (Button) findViewById(R.id.button_roupas);
		bRoupas.setOnClickListener(this);
		
		Button bProvar = (Button) findViewById(R.id.button_provar);
		bProvar.setOnClickListener(this);

		Button bSair = (Button) findViewById(R.id.button_sair);
		bSair.setOnClickListener(this);

		Button bCreditos = (Button) findViewById(R.id.button_creditos);
		bCreditos.setOnClickListener(this);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	//@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_manequim:
			i = new Intent(v.getContext(), ManequimActivity.class);
			startActivity(i);
			break;
		case R.id.button_roupas:
			i = new Intent(v.getContext(), RoupasActivity.class);
			startActivity(i);
			break;
		case R.id.button_provar:
			i = new Intent(v.getContext(), ProvadorActivity.class);
			BDAdapter dao = new BDAdapter(this);
			if (dao.getManequimPadrao() == null) {
//				i.putExtra("manequimFaltando", true);
//				startActivity(i);
				Toast.makeText(this, "Não há manequim escolhido!", Toast.LENGTH_LONG).show();
			} else {
				i.putExtra("background", dao.getManequimPadrao());
				startActivity(i);
			}
			break;
		case R.id.button_creditos:
			i = new Intent(v.getContext(), Creditos.class);
			startActivity(i);
			break;
		case R.id.button_sair:
			finish();
			break;
		}
	}
}