package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import br.edu.ufcg.BD.BDAdapter;

public class TirarFotoActivity extends Activity implements ImageListener, OnClickListener {

	private CameraView mPreview;
	private BDAdapter dh;

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

		this.dh.salvarManequimMemoriaExterna(image);
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