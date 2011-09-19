package br.edu.ufcg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SobreActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sobre);

		Button bCreditos = (Button) findViewById(R.id.button_creditos);
		bCreditos.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_creditos:
			i = new Intent(v.getContext(), Creditos.class);
			startActivity(i);
			break;

		}
	}

}