package br.edu.ufcg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import br.edu.ufcg.BD.BDAdapter;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class EscolherManequimActivity extends Activity {
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escolher_manequim);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        ImageView imgView =(ImageView)findViewById(R.id.ImageManequim);
        BDAdapter bd = new BDAdapter(this);
        
        //por enquanto, pegando ultimo manequim 
        String caminhoManequim = bd.getAllManequins().get(bd.getAllManequins().size()-1).getPath();
        
        Drawable drawable = LoadImage(Environment.getExternalStoragePublicDirectory(
        		Environment.DIRECTORY_PICTURES) + File.separator + caminhoManequim);
        
        Log.e("drawable", "drawable null? " + drawable); //TIRAR
        
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