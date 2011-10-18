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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Manequim;
import br.edu.ufcg.model.Roupa;

public class VisualizacaoDeRoupas extends Activity {
	
	Gallery gallery;
	private BDAdapter dh;
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizacao_roupas);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.dh = new BDAdapter(this);     
                
        gallery = (Gallery) findViewById(R.id.gallery);
	    gallery.setAdapter(new ImageAdapter(this));

//	    gallery.setOnItemClickListener(new OnItemClickListener() {
//	        public void onItemClick(AdapterView parent, View v, int position, long id) {
//	            Toast.makeText(VisualizacaoDeRoupas.this, "" + position, Toast.LENGTH_SHORT);
//	            Roupa roupaEscolhida = (Roupa) gallery.getAdapter().getItem(position);
//	            dh.inserirRoupa(roupaEscolhida);
//	            
//				startActivity(new Intent(VisualizacaoDeRoupas.this, CalibragemRoupasActivity.class));
//				
//	        }
//	    });

        
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

	    public Roupa getItem(int position) {
	    	BDAdapter bd = new BDAdapter(VisualizacaoDeRoupas.this);
	    	
	    	Bundle params = getIntent().getExtras();
			Categoria categoria = (Categoria) params.get("categoria");
			List<Roupa> roupas = bd.getRoupas(categoria);
			
	        return roupas.get(position);
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView = new ImageView(mContext);

	        Drawable drawable = carregarImagem(Environment.getExternalStoragePublicDirectory(
	        		Environment.DIRECTORY_PICTURES) + File.separator + imagens[position]);
	        imageView.setImageDrawable(drawable);
	        imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	        imageView.setBackgroundResource(mGalleryItemBackground);

	        return imageView;
	    }
	    
	    private void carregaArrayImagens() {
			BDAdapter bd = new BDAdapter(VisualizacaoDeRoupas.this);
			
			Bundle params = getIntent().getExtras();
			Categoria categoria = (Categoria) params.get("categoria"); //aparentemente nao faz efeito aqui
			
			List<Roupa> roupas = bd.getRoupas(categoria);
			imagens = new String[roupas.size()];
			for (int i = 0; i < roupas.size(); i++) {
				imagens[i] = roupas.get(i).getCaminhoImagem();
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