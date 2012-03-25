package br.edu.ufcg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Creditos extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditos);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}
	
}

