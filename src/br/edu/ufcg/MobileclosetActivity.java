package br.edu.ufcg;

import java.io.ByteArrayOutputStream;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Roupa;

public class MobileclosetActivity extends Activity {
	/** Called when the activity is first created. */

	private BDAdapter dao;
	
	private AnimationDrawable animation;
	
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			animation.start();
		} else {
			animation.stop();
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ImageView spin_animation = new ImageView(this);  
	    setContentView(spin_animation);  
	    spin_animation.setBackgroundResource(R.anim.spin_animation);  
	    this.animation = (AnimationDrawable) spin_animation.getBackground();  
		
		
		dao = new BDAdapter(this);
		
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.manequim_padrao);
		Bitmap camisa_padrao = BitmapFactory.decodeResource(getResources(), R.drawable.camisa_padrao);
		Bitmap calca_padrao = BitmapFactory.decodeResource(getResources(), R.drawable.calca_padrao);
		Bitmap saia_padrao = BitmapFactory.decodeResource(getResources(), R.drawable.saia_padrao);
		Bitmap camisao_padrao = BitmapFactory.decodeResource(getResources(), R.drawable.camisao_padrao);
		Bitmap vestido_padrao = BitmapFactory.decodeResource(getResources(), R.drawable.vestido_padrao);
		Bitmap camiseta_padrao = BitmapFactory.decodeResource(getResources(), R.drawable.camiseta_padrao);
		Bitmap short_padrao = BitmapFactory.decodeResource(getResources(), R.drawable.short_padrao);
		
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
		
		if (dao.getRoupas().isEmpty()) {
			ByteArrayOutputStream bos_camisa = new ByteArrayOutputStream(); 
			camisa_padrao.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos_camisa); 
			byte[] bitmapCamisa = bos_camisa.toByteArray();
			
			ByteArrayOutputStream bos_calca = new ByteArrayOutputStream(); 
			calca_padrao.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos_calca); 
			byte[] bitmapCalca = bos_calca.toByteArray();
			
			ByteArrayOutputStream bos_saia = new ByteArrayOutputStream(); 
			saia_padrao.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos_saia); 
			byte[] bitmapsaia = bos_saia.toByteArray();
			
			ByteArrayOutputStream bos_camisao = new ByteArrayOutputStream(); 
			camisao_padrao.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos_camisao); 
			byte[] bitmapcamisao = bos_camisao.toByteArray();
			
			ByteArrayOutputStream bos_vestido = new ByteArrayOutputStream(); 
			vestido_padrao.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos_vestido); 
			byte[] bitmapvestido = bos_vestido.toByteArray();
			
			ByteArrayOutputStream bos_camiseta = new ByteArrayOutputStream(); 
			camiseta_padrao.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos_camiseta); 
			byte[] bitmapcamiseta = bos_camiseta.toByteArray();
			
			ByteArrayOutputStream bos_short = new ByteArrayOutputStream(); 
			short_padrao.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos_short); 
			byte[] bitmapshort = bos_short.toByteArray();
			
			dao.inserirRoupa(new Roupa(0, bitmapCamisa, Categoria.CAMISA));
			dao.inserirRoupa(new Roupa(1, bitmapCalca, Categoria.CALCA));
			dao.inserirRoupa(new Roupa(2, bitmapsaia, Categoria.SAIA));
			dao.inserirRoupa(new Roupa(3, bitmapcamisao, Categoria.CAMISA_MANGA_LONGA));
			dao.inserirRoupa(new Roupa(4, bitmapvestido, Categoria.VESTIDO));
			dao.inserirRoupa(new Roupa(5, bitmapcamiseta, Categoria.CAMISETA));
			dao.inserirRoupa(new Roupa(6, bitmapshort, Categoria.SHORT));
			
			dao.insertCalibragem(new Calibragem(Categoria.CAMISA, 37, 39, 197, 159));
			dao.insertCalibragem(new Calibragem(Categoria.CALCA, 51, 125, 175, 281));
			dao.insertCalibragem(new Calibragem(Categoria.SAIA, 29, 122, 200, 213));
			dao.insertCalibragem(new Calibragem(Categoria.CAMISA_MANGA_LONGA, 14, 37, 223, 170));
			dao.insertCalibragem(new Calibragem(Categoria.VESTIDO, 50, 42, 178, 190));
			dao.insertCalibragem(new Calibragem(Categoria.CAMISETA, 65, 41, 165, 157));
			dao.insertCalibragem(new Calibragem(Categoria.SHORT, 54, 128, 182, 220));

		}
		

		//Botao Iniciar
		ImageButton bIniciar = new ImageButton(this);
	    bIniciar.setPadding(0, 0, 10, 10);
		bIniciar.setImageResource(R.drawable.button_iniciar);
		bIniciar.setBackgroundColor(Color.TRANSPARENT);
		bIniciar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i;
				if (dao.getRoupas().isEmpty()) {
					i = new Intent(v.getContext(), OpcoesActivity.class);
				} else {
					i = new Intent(v.getContext(), ProvadorActivity.class);
					i.putExtra("background", dao.getManequimPadrao());
				}
				startActivity(i);
			}
		});
		
				
		RelativeLayout layoutIniciar = new RelativeLayout(this);
		LinearLayout linearIniciar = new LinearLayout(this);
		linearIniciar.addView(bIniciar);
		linearIniciar.setGravity(Gravity.RIGHT);
		linearIniciar.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutIniciar.addView(linearIniciar);
		layoutIniciar.setGravity(Gravity.BOTTOM);
		addContentView(layoutIniciar, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		//Botao Fechar
		ImageButton closeButton = new ImageButton(this);
		closeButton.setImageResource(R.drawable.close);
		closeButton.setBackgroundColor(Color.TRANSPARENT);
		closeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
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
	
	
}