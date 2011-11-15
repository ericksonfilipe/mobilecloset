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

public class ManequimActivity extends Activity implements OnClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manequim);
		
		Button bManequim = (Button) findViewById(R.id.button_tirar_foto);
		bManequim.setOnClickListener(this);

		Button bRoupas = (Button) findViewById(R.id.button_escolher_manequim);
		bRoupas.setOnClickListener(this);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_tirar_foto:
			i = new Intent(v.getContext(), TirarFotoManequimActivity.class);
			startActivity(i);
			break;
		case R.id.button_escolher_manequim:
			BDAdapter dao = new BDAdapter(this);
			if (dao.getManequins().isEmpty()) {
				Toast.makeText(this, "Não há manequins cadastrados!", Toast.LENGTH_LONG).show();
			} else {
				//i = new Intent(v.getContext(), CalibragemRoupasActivity.class);
				i = new Intent(v.getContext(), EscolherManequimActivity.class);
				startActivity(i);
			}
			break;
		}
		
	}

}
