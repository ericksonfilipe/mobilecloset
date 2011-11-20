package br.edu.ufcg;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Roupa;

public class VerRoupasActivity extends Activity {

	private BDAdapter dao;
	private VisualizadorRoupa visualizadorRoupa;
	public ImageButton anteriorButton;
	public ImageButton proximaButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new BDAdapter(getApplicationContext());
		byte[] manequim = dao.getManequimPadrao();

		visualizadorRoupa = new VisualizadorRoupa(this, dao.getRoupas());
		visualizadorRoupa.setBackgroundDrawable(carregaDrawable(manequim));
		setContentView(visualizadorRoupa);

		proximaButton = new ImageButton(this);
		proximaButton.setImageResource(R.drawable.next);
		proximaButton.setBackgroundColor(Color.TRANSPARENT);
		proximaButton.setOnClickListener(new ProximoListener());

		anteriorButton = new ImageButton(this);
		anteriorButton.setImageResource(R.drawable.previous_cinza);
		anteriorButton.setBackgroundColor(Color.TRANSPARENT);
		anteriorButton.setOnClickListener(new VoltaListener());

		RelativeLayout layout = new RelativeLayout(this);
		LinearLayout linear = new LinearLayout(this);
		linear.addView(anteriorButton);
		linear.addView(proximaButton);
		linear.setGravity(Gravity.RIGHT);
		linear.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layout.addView(linear);
		layout.setGravity(Gravity.BOTTOM);
		
		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private Drawable carregaDrawable(byte[] imagem) {
		Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		return new BitmapDrawable(girado);
	}

	private class VoltaListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorRoupa.voltaRoupaSuperior();
		}

	}

	private class ProximoListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorRoupa.proximaRoupaSuperior();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.ver_roupa_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.removerRoupa:
			visualizadorRoupa.removeImagem();
			break;
		}
		return true;
	}

	public class VisualizadorRoupa extends View {

		private List<Roupa> roupas;

		private int posicaoRoupa;
		private Drawable roupaAtual;


		public VisualizadorRoupa(Context context, List<Roupa> roupas) {
			super(context);
			this.roupas = roupas;
			if (!roupas.isEmpty()) {
				roupaAtual = carregaDrawable(roupas.get(posicaoRoupa).getImagem());
				roupaAtual.setBounds(0, 0, getApplicationContext().getResources().getDisplayMetrics().widthPixels, getApplicationContext().getResources().getDisplayMetrics().heightPixels);
			}

			setFocusable(true);
			this.requestFocus();
			this.requestLayout();
		}

		public void removeImagem() {
			dao.removeRoupa(roupas.get(posicaoRoupa));
			roupas.remove(posicaoRoupa);
			if (roupas.isEmpty()) {
				finish();
				return;
			} else if (posicaoRoupa == roupas.size()) {
				posicaoRoupa--;
			}
			Roupa roupa = roupas.get(posicaoRoupa);
			roupaAtual = carregaDrawable(roupa.getImagem());
			roupaAtual.setBounds(0, 0, getWidth(), getHeight());
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (roupaAtual != null) {
				roupaAtual.draw(canvas);
			}
		}

		public void proximaRoupaSuperior() {
			if (posicaoRoupa < roupas.size()-1) {
				posicaoRoupa++;
				Roupa roupa = roupas.get(posicaoRoupa);
				roupaAtual = carregaDrawable(roupa.getImagem());
				roupaAtual.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaRoupaSuperior() {
			if (posicaoRoupa > 0) {
				posicaoRoupa--;
				Roupa roupa = roupas.get(posicaoRoupa);
				roupaAtual = carregaDrawable(roupa.getImagem());
				roupaAtual.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		private void atualizaImagensBotoes() {
			if (posicaoRoupa > 0) {
				anteriorButton.setImageResource(R.drawable.previous);
			} else {
				anteriorButton.setImageResource(R.drawable.previous_cinza);
			}

			if (posicaoRoupa < roupas.size() - 1) {
				proximaButton.setImageResource(R.drawable.next);
			} else {
				proximaButton.setImageResource(R.drawable.next_cinza);
			}
		}

		private Drawable carregaDrawable(byte[] imagem) {
			Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			return new BitmapDrawable(girado);
		}
		
		public Roupa getRoupaAtual() {
			return roupas.get(posicaoRoupa);
		}
	}
}