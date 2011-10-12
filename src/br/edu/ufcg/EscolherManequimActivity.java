package br.edu.ufcg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Manequim;

public class EscolherManequimActivity extends Activity {
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escolher_manequim);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
        
        Gallery gallery = (Gallery) findViewById(R.id.gallery);
	    gallery.setAdapter(new ImageAdapter(this));

	    gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	            Toast.makeText(EscolherManequimActivity.this, "" + position, Toast.LENGTH_SHORT);
	            
				startActivity(new Intent(EscolherManequimActivity.this, CalibragemRoupasActivity.class));
				
	        }
	    });
	    
//        
//        ImageView imgView =(ImageView)findViewById(R.id.ImageManequim);
//        BDAdapter bd = new BDAdapter(this);
//        
//        //por enquanto, pegando ultimo manequim 
//        String caminhoManequim = bd.getAllManequins().get(bd.getAllManequins().size()-1).getPath();
//        
//        Drawable drawable = LoadImage(Environment.getExternalStoragePublicDirectory(
//        		Environment.DIRECTORY_PICTURES) + File.separator + caminhoManequim);
//        
//        Log.e("drawable", "drawable null? " + drawable); //TIRAR
//        
//        imgView.setImageDrawable(drawable);
        
    }
	
	public class ImageAdapter extends BaseAdapter {
	    int mGalleryItemBackground;
	    private Context mContext;

	    private String[] imagens;

	    public ImageAdapter(Context c) {
	        mContext = c;
	        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
	        mGalleryItemBackground = attr.getResourceId(
	                R.styleable.HelloGallery_android_galleryItemBackground, 0);
	        attr.recycle();
	        
	        carregaArrayImagens();
	    }

	    public int getCount() {
	        return imagens.length;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView = new ImageView(mContext);

	        Drawable drawable = carregarImagem(Environment.getExternalStoragePublicDirectory(
	        		Environment.DIRECTORY_PICTURES) + File.separator + imagens[position]);
	        imageView.setImageDrawable(drawable);
	        imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
	        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	        imageView.setBackgroundResource(mGalleryItemBackground);

	        return imageView;
	    }
	    
	    private void carregaArrayImagens() {
			BDAdapter bd = new BDAdapter(EscolherManequimActivity.this);
			List<Manequim> manequins = bd.getAllManequins();
			imagens = new String[manequins.size()];
			for (int i = 0; i < manequins.size(); i++) {
				imagens[i] = manequins.get(i).getCaminhoImagem();
			}
		}
	}
    
	private Drawable carregarImagem(String src) {
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