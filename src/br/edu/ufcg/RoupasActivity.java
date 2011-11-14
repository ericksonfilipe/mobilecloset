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

public class RoupasActivity extends Activity implements OnClickListener {
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roupas);
        
		Button bManequim = (Button) findViewById(R.id.button_cadastrar_roupa);
		bManequim.setOnClickListener(this);

		Button bRoupas = (Button) findViewById(R.id.button_ver_roupas);
		bRoupas.setOnClickListener(this);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
    }

	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_cadastrar_roupa:
			i = new Intent(v.getContext(), TirarFotoRoupaActivity.class);
			startActivity(i);
			break;
		case R.id.button_ver_roupas:
			BDAdapter dao = new BDAdapter(this);
			if (dao.getRoupas().isEmpty()) {
				Toast.makeText(this, "N�o h� roupas cadastradas!", Toast.LENGTH_LONG).show();
			} else {
				i = new Intent(v.getContext(), VisualizacaoDeRoupasActivity.class);
				startActivity(i);
			}
			break;
		}
		
		
	}

}
