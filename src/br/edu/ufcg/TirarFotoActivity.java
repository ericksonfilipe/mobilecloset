package br.edu.ufcg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TirarFotoActivity extends Activity implements ImageListener {

	CameraView mPreview;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		mPreview = (CameraView) findViewById(R.id.imagem_camera);
		mPreview.addImageListener(this);
	}

	public void takeImage(byte[] image) {
		// Cleaning memory 
		System.gc(); 
		mPreview.setVisibility(View.INVISIBLE);
		mPreview.freezeCamera();
	}

}