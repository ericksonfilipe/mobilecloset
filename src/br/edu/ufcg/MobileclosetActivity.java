package br.edu.ufcg;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;

public class MobileclosetActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private BDAdapter dao;
	
	//criado para a v2
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_v2);

		Button bIniciar = (Button) findViewById(R.id.button_iniciar);
		bIniciar.setOnClickListener(this);
		
		dao = new BDAdapter(this);
		
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.manequim_padrao);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		b.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		
		if (dao.getManequins().isEmpty()) {
			dao.inserirManequim(bitmapdata);
			dao.inserirManequimPadrao(dao.getManequins().get(0));
		} else {
			if (dao.getManequimPadrao() == null) {
				dao.inserirManequimPadrao(dao.getManequins().get(0));
			}
		}
		

//		Button bOpcoes = (Button) findViewById(R.id.button_opcoes_v2);
//		bOpcoes.setOnClickListener(this);
		
		ImageButton closeButton = new ImageButton(this);
		closeButton.setImageResource(R.drawable.close);
		closeButton.setBackgroundColor(Color.TRANSPARENT);
		closeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				moveTaskToBack(true); //esconde a aplicacao e deixa o Android decidir quando terminar.
				//android.os.Process.killProcess(android.os.Process.myPid()); //encerra realmente.
				//finish(); //modo "usual"; soh pega na primeira activity.
			}
		});
		
		RelativeLayout layoutClose = new RelativeLayout(this);
		LinearLayout linearClose = new LinearLayout(this);
		linearClose.addView(closeButton);
		linearClose.setGravity(Gravity.LEFT);
		linearClose.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutClose.addView(linearClose);
		layoutClose.setGravity(Gravity.BOTTOM);
		addContentView(layoutClose, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}	

	//criado para v2
	//@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_iniciar:
			if (dao.getRoupas().isEmpty()) {
				i = new Intent(v.getContext(), OpcoesActivity.class);
				startActivity(i);
			} else {
				i = new Intent(v.getContext(), ProvadorActivity.class);
				i.putExtra("background", dao.getManequimPadrao());				
				startActivity(i);
			}
			break;
		}
	}	

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//		
//		Button bTeste = (Button) findViewById(R.id.button_teste);
//		bTeste.setOnClickListener(this);
//
//		Button bManequim = (Button) findViewById(R.id.button_manequim);
//		bManequim.setOnClickListener(this);
//
//		Button bRoupas = (Button) findViewById(R.id.button_roupas);
//		bRoupas.setOnClickListener(this);
//		
//		Button bProvar = (Button) findViewById(R.id.button_provar);
//		bProvar.setOnClickListener(this);
//
//		Button bSair = (Button) findViewById(R.id.button_sair);
//		bSair.setOnClickListener(this);
//
//		Button bCreditos = (Button) findViewById(R.id.button_creditos);
//		bCreditos.setOnClickListener(this);
//
//		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//	}	
	
	
//	//@Override
//	public void onClick(View v) {
//		Intent i;
//		switch (v.getId()) {
//		case R.id.button_manequim:
//			i = new Intent(v.getContext(), ManequimActivity.class);
//			startActivity(i);
//			break;
//		case R.id.button_roupas:
//			i = new Intent(v.getContext(), RoupasActivity.class);
//			startActivity(i);
//			break;
//		case R.id.button_provar:
//			i = new Intent(v.getContext(), ProvadorActivity.class);
//			BDAdapter dao = new BDAdapter(this);
//			if (dao.getManequimPadrao() == null) {
////				i.putExtra("manequimFaltando", true);
////				startActivity(i);
//				Toast.makeText(this, "Não há manequim escolhido!", Toast.LENGTH_LONG).show();
//			} else {
//				i.putExtra("background", dao.getManequimPadrao());
//				startActivity(i);
//			}
//			break;
//		case R.id.button_creditos:
//			i = new Intent(v.getContext(), Creditos.class);
//			startActivity(i);
//			break;
//		case R.id.button_sair:
//			finish();
//			break;
//		case R.id.button_teste:
//			i = new Intent(v.getContext(), TesteDeteccaoFace.class);
//			startActivity(i);
//			break;
//		}
//	}
	

	
}