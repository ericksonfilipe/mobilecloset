package br.edu.ufcg;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import br.edu.ufcg.BD.BDAdapter;

public class TirarFotoManequimActivity extends Activity implements ImageListener, OnClickListener {

	private CameraView mPreview;
	private BDAdapter dao;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		mPreview = (CameraView) findViewById(R.id.imagem_camera);
		mPreview.addImageListener(this);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundResource(R.drawable.tracejado2);
		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		this.dao = new BDAdapter(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.tirar_foto_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mPreview.takePicture();
		return true;
	}

	public void takeImage(byte[] image) { 
		// Cleaning memory 
		
		System.gc(); 
		mPreview.setVisibility(View.INVISIBLE);
		mPreview.freezeCamera();
		
		Bitmap plecas = BitmapFactory.decodeByteArray(image, 0, image.length);
		Bitmap mudado = Bitmap.createScaledBitmap(plecas, 500, 500, true);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		mudado.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		
		this.dao.inserirManequim(bitmapdata);

		this.finish();
	}

	public void onClick(View v) {
		mPreview.takePicture();

	}



}