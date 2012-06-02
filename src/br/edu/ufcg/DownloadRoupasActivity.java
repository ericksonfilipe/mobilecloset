package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DownloadRoupasActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String nomeLoja = (String) getIntent().getExtras().get("nomeLoja");
		int qtdeRoupas = (Integer) getIntent().getExtras().get("qtdeRoupas");

		TextView infoCategoria = new TextView(this);
		infoCategoria.setText("Download da Coleção concluído!\n(" + qtdeRoupas + " peças da loja "+nomeLoja + ")");
		infoCategoria.setTextSize(TypedValue.COMPLEX_UNIT_PX, 14);

		LinearLayout maior = new LinearLayout(this);
		maior.setOrientation(LinearLayout.VERTICAL);
		maior.setPadding(5, 5, 5, 5);
		maior.addView(infoCategoria);
		
		LinearLayout layoutBotao = new LinearLayout(this);
		layoutBotao.setGravity(Gravity.CENTER);
		
		Button okButton = new Button(this);
		okButton.setText("Ok");
		okButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				finish();				
			}
		});
		
		layoutBotao.addView(okButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		maior.addView(layoutBotao);
		addContentView(maior, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

}
