package br.edu.ufcg;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

public class EscolherManequimActivity extends Activity {
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escolher_manequim);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        ImageView imgView =(ImageView)findViewById(R.id.ImageManequim);
        Drawable drawable = LoadImage(Environment.getExternalStoragePublicDirectory(
        		Environment.DIRECTORY_PICTURES) + "/image.jpg");
        
        imgView.setImageDrawable(drawable);
        
    }
    
	private Drawable LoadImage(String src) {
	      try {
	          InputStream is = new BufferedInputStream(new FileInputStream(src));
	          Drawable d = Drawable.createFromStream(is, "src name");
	          return d;
	      } catch (Exception e) {
	          System.out.println("Exc="+e);
	          return null;
	      }
	}

}