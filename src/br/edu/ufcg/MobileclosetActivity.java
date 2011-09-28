package br.edu.ufcg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import android.util.Log;

public class MobileclosetActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	
	private TextView output;
	private DataHelper dh;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button bEscolher = (Button) findViewById(R.id.buttonEscolherRoupas);
		bEscolher.setOnClickListener(this);

		Button bSair = (Button) findViewById(R.id.button_sair);
		bSair.setOnClickListener(this);

		Button bSobre = (Button) findViewById(R.id.button_sobre);
		bSobre.setOnClickListener(this);
		
		Button bCreditos = (Button) findViewById(R.id.button_creditos);
		bCreditos.setOnClickListener(this);
		
		//código util para BD; lembrar de descomentar no main.xml para testar
//		output = (TextView) findViewById(R.id.out_text);
//
//        this.dh = new DataHelper(this);
//        this.dh.deleteAll();
//        this.dh.insert("Porky Pig");
//        this.dh.insert("Foghorn Leghorn");
//        this.dh.insert("Yosemite Sam");
//        List<String> names = this.dh.selectAll();
//        StringBuilder sb = new StringBuilder();
//        sb.append("Names in database:\n");
//        for (String name : names) {
//           sb.append(name + "\n");
//        }
//
//        Log.d("EXAMPLE", "names size - " + names.size());
//
//        output.setText(sb.toString());		
		
		
	}

	//@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.buttonEscolherRoupas:
			i = new Intent(v.getContext(), Closet.class);
			startActivity(i);
			break;
		case R.id.button_creditos:
			i = new Intent(v.getContext(), Creditos.class);
			startActivity(i);
			break;
		case R.id.button_sobre:
			i = new Intent(v.getContext(), SobreActivity.class);
			startActivity(i);
			break;
		case R.id.button_sair:
			finish();
			break;
		}
	}
}