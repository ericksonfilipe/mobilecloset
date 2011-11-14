package br.edu.ufcg;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Roupa;

public class VisualizacaoDeRoupasActivity extends Activity {

	Gallery gallery;
	private ImageAdapter imageAdapter;
	private BDAdapter dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visualizacao_roupas);
		
		dao = new BDAdapter(this);

		gallery = (Gallery) findViewById(R.id.gallery);
		imageAdapter = new ImageAdapter(this);
		gallery.setAdapter(imageAdapter);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.ver_roupa_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		imageAdapter.removeImagem();
		return true;
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		private Roupa[] imagens;
		private int posicaoAtual;

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
			List<Roupa> roupas = dao.getRoupas();
			return roupas.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			posicaoAtual = position;
			ImageView imageView = new ImageView(mContext);

			byte[] imagem = imagens[position].getImagem();
			Bitmap d = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);

			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(d, 0, 0, d.getWidth(), d.getHeight(), matrix, true);

			imageView.setImageBitmap(girado);
			imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setBackgroundResource(mGalleryItemBackground);

			return imageView;
		}
	
		public void removeImagem() {
			dao.removeRoupa(imagens[posicaoAtual]);
			carregaArrayImagens();
			notifyDataSetChanged();
		}
		
		private void carregaArrayImagens() {
			List<Roupa> roupas = dao.getRoupas();
			imagens = new Roupa[roupas.size()];
			for (int i = 0; i < roupas.size(); i++) {
				imagens[i] = roupas.get(i);
			}
		}
	}
}