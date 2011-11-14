package br.edu.ufcg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Manequim;

public class EscolherManequimActivity extends Activity {

	Gallery gallery;
	private BDAdapter dh;
	private List<Manequim> manequins = new ArrayList<Manequim>();

	private boolean DEBUG = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escolher_manequim);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.dh = new BDAdapter(this);     
		this.manequins = dh.getManequins();

		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this));

		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Manequim manequimEscolhido = (Manequim) gallery.getAdapter().getItem(position);
				dh.inserirManequimPadrao(manequimEscolhido);
				Intent i = new Intent(EscolherManequimActivity.this, CalibragemRoupasActivity.class);
				i.putExtra("background", manequimEscolhido.getImagem());
				finish();
				startActivity(i);
			}
		});

	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		private Manequim[] imagens;

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

		public Manequim getItem(int position) {
			return manequins.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);

			if (DEBUG) {
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.background));
			} else {
				byte[] imagem = imagens[position].getImagem();
				Bitmap d = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);

				Matrix matrix = new Matrix();
				matrix.setRotate(90);
				Bitmap girado = Bitmap.createBitmap(d, 0, 0, d.getWidth(), d.getHeight(), matrix, true);
				imageView.setImageBitmap(girado);
			}
			imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setBackgroundResource(mGalleryItemBackground);

			return imageView;

		}

		private void carregaArrayImagens() {
			imagens = new Manequim[manequins.size()];
			for (int i = 0; i < manequins.size(); i++) {
				imagens[i] = manequins.get(i);
			}
		}
	}
}