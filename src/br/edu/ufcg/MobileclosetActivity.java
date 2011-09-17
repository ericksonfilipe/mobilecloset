package br.edu.ufcg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MobileclosetActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bEscolher = (Button) findViewById(R.id.buttonEscolherRoupas);
        bEscolher.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonEscolherRoupas:
			Intent i = new Intent(v.getContext(), EscolherRoupasActivity.class);
			startActivity(i);
		}
		
	}
}