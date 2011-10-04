package br.edu.ufcg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TirarFotoActivity extends Activity implements ImageListener, OnClickListener {

	private CameraView mPreview;
	private DataHelper dh;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		mPreview = (CameraView) findViewById(R.id.imagem_camera);
		mPreview.addImageListener(this);
		
		Button tiraFoto = (Button) findViewById(R.id.button_camera);
		tiraFoto.setOnClickListener(this);
      
      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this.dh = new DataHelper(this);
		
	}

	public void takeImage(byte[] image) { 
		 // Cleaning memory 
		 System.gc(); 
		 mPreview.setVisibility(View.INVISIBLE);
		 mPreview.freezeCamera();
		 
		 this.dh.salvarImagemMemoriaExterna(image);
		 System.out.println("xxxxxxxxxxxxxxxxx FOI");

		 //try {
		 //	OutputStream fos = openFileOutput(File.separator + "var" + File.separator + "mobileCloset" + File.separator + "imagem.jpg", Context.MODE_WORLD_READABLE);
		 //Log.e("pffffffff", Environment.getExternalStorageDirectory()+"");
		 //fos.write(image);
		 //fos.flush();
		 //fos.close();
			
		 //} catch (FileNotFoundException e) {
		 //e.printStackTrace();
		 //} catch (IOException e) {
		 //	e.printStackTrace();
		 //}
		 
		 this.finish();
	}

	public void onClick(View v) {
			mPreview.takePicture();
		
	}
	
	

}