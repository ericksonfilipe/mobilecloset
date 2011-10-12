package br.edu.ufcg;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

public class CalibragemRoupasActivity  extends Activity{
	
	ImageView img_camisa;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.calibragem);
	        
	        
	        //------pegando o manequim ------------------------------------
	        // DESCOMENTAR ISSO DEPOIS
	       // ImageView imgView =(ImageView) findViewById(R.id.ImageManequim);
	       // BDAdapter bd = new BDAdapter(this);
	        
	        //por enquanto, pegando ultimo manequim 
	        //String caminhoManequim = bd.getAllManequins().get(bd.getAllManequins().size()-1).getPath();
	        
	        //Drawable drawable = LoadImage(Environment.getExternalStoragePublicDirectory(
	          //              Environment.DIRECTORY_PICTURES) + File.separator + caminhoManequim);
	        
	        //Log.e("drawable", "drawable null? " + drawable); //TIRAR
	        
	        //imgView.setImageDrawable(drawable);
	        
	        //------------------------- parte da calibragem da camisa ----------------------------------------------
	        
	        img_camisa = new ImageView(this);
//	        img_camisa.setBackgroundColor(Color.TRANSPARENT);
	        img_camisa.setBackgroundResource(R.drawable.camisa);
	        
	        addContentView(img_camisa, new LayoutParams(100, 100));
	        
			Button mais = new Button(this);
			mais.setText("+");
			//mais.setBackgroundColor(Color.TRANSPARENT);
			mais.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					int width = img_camisa.getWidth() + 10;
					int height = img_camisa.getHeight() + 10;
					
					

					
				}
			});
	
			addContentView(mais, new LayoutParams(30, 30));
			//--------------------------------------------------------------------------------------------------

			
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        
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
