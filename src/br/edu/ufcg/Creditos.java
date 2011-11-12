package br.edu.ufcg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import br.edu.ufcg.BD.BDAdapter;

public class Creditos extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.creditos);
        
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        BDAdapter dao = new BDAdapter(this);
        byte[] pleas = dao.getPlecas();
        Log.e("iai????????", String.valueOf(pleas));
        Bitmap plecas = BitmapFactory.decodeByteArray(pleas, 0, pleas.length);
        ImageView i = new ImageView(this);
        i.setImageBitmap(plecas);
        setContentView(i);
        
    }

}
