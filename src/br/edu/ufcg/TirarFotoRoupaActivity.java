package br.edu.ufcg;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Roupa;

public class TirarFotoRoupaActivity extends Activity implements ImageListener, OnClickListener {

	private CameraView mPreview;
	private BDAdapter dh;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_roupas);
		mPreview = (CameraView) findViewById(R.id.imagem_camera_roupas);
		mPreview.addImageListener(this);
		
		Bundle params = getIntent().getExtras();
        Categoria categoria = (Categoria) params.get("categoria");

        LinearLayout layout = new LinearLayout(this);
        
        //codigo feio pra funfar, ajeitar depois =P
        if (categoria.toString()=="Bermuda") layout.setBackgroundResource(R.drawable.molde_short);
        else if (categoria.toString()=="Cal�a") layout.setBackgroundResource(R.drawable.molde_calca);
        else if (categoria.toString()=="Camisa") layout.setBackgroundResource(R.drawable.molde_camisa);
        else if (categoria.toString()=="Camisa manga longa") layout.setBackgroundResource(R.drawable.molde_camisao);
        else if (categoria.toString()=="Camiseta") layout.setBackgroundResource(R.drawable.molde_camiseta);
        else if (categoria.toString()=="Saia") layout.setBackgroundResource(R.drawable.molde_saia);
        else if (categoria.toString()=="Short") layout.setBackgroundResource(R.drawable.molde_short);
        else layout.setBackgroundResource(R.drawable.molde_camiseta);
        
        
        addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		Button tiraFoto = new Button(this);
		tiraFoto.setBackgroundColor(Color.TRANSPARENT);
		tiraFoto.setOnClickListener(this);
		addContentView(tiraFoto, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		this.dh = new BDAdapter(this);

	}

	public void takeImage(byte[] image) { 
		// Cleaning memory 
		System.gc(); 
		mPreview.setVisibility(View.INVISIBLE);
		mPreview.freezeCamera();

		Bundle params = getIntent().getExtras();
		Categoria categoria = (Categoria) params.get("categoria");
		
		Bitmap plecas = BitmapFactory.decodeByteArray(image, 0, image.length);
		Bitmap mudado = Bitmap.createScaledBitmap(plecas, 400, 400, true);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		mudado.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		
		Roupa roupa = new Roupa(0, bitmapdata, categoria);
		this.dh.inserirRoupa(roupa);

		this.finish();
	}

	public void onClick(View v) {
		mPreview.takePicture();

	}


}
