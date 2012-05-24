package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.edu.ufcg.EscolherManequimActivity.RetornoListener;
import br.edu.ufcg.EscolherManequimActivity.VisualizadorManequim;

public class ExcluirManequim extends Activity implements OnClickListener {

	private RetornoListener listener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		listener = (RetornoListener) getIntent().getExtras().get("listener");
		setContentView(R.layout.confima_exclusao);
		
		Button bSim = (Button) findViewById(R.id.button_nao);
		bSim.setOnClickListener((OnClickListener) this);
		
		Button bNao = (Button) findViewById(R.id.button_sim);
		bNao.setOnClickListener((OnClickListener) this);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_sim:
			//listener.notifica();
			finish();
			break;
		case R.id.button_nao:
			finish();
			break;
		}
	}
	
}

