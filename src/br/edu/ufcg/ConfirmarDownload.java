package br.edu.ufcg;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.edu.ufcg.EscolherManequimActivity.RetornoListener;

public class ConfirmarDownload extends Activity {

	private RetornoListener listener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		listener = (RetornoListener) getIntent().getExtras().get("listener");
		setContentView(R.layout.download_colecao);
		
		Button bDownload = new Button(this);
		bDownload.setText("Download");
		bDownload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				finish();				
			}
		});
		
		Button bVoltar = new Button(this);
		bVoltar.setText("Voltar");
		bVoltar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
		TextView info = new TextView(this);
		info.setText("Informações e quantidade de peças");
		
		RelativeLayout layoutCentral = new RelativeLayout(this);
		LinearLayout linearCentral = new LinearLayout(this);
		linearCentral.addView(bDownload);
		linearCentral.addView(bVoltar);
		linearCentral.addView(info);
		linearCentral.setGravity(Gravity.RIGHT);
		linearCentral.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutCentral.addView(linearCentral);
		layoutCentral.setGravity(Gravity.BOTTOM);
		
		addContentView(layoutCentral, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}
	
}

