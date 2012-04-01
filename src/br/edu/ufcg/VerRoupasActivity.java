package br.edu.ufcg;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Calibragem2;
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

		Button meioButton = new Button(this);
		meioButton.setText("Calibrar");
		meioButton.setOnClickListener(new CalibrarListener());

		RelativeLayout layoutProximo = new RelativeLayout(this);
		LinearLayout linearProximo = new LinearLayout(this);
		linearProximo.addView(proximaButton);
		linearProximo.setGravity(Gravity.RIGHT);
		linearProximo.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutProximo.addView(linearProximo);
		layoutProximo.setGravity(Gravity.BOTTOM);

		RelativeLayout layoutAnterior = new RelativeLayout(this);
		LinearLayout linearAnterior = new LinearLayout(this);
		linearAnterior.addView(anteriorButton);
		linearAnterior.setGravity(Gravity.LEFT);
		linearAnterior.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutAnterior.addView(linearAnterior);
		layoutAnterior.setGravity(Gravity.BOTTOM);

		RelativeLayout layoutMeio = new RelativeLayout(this);
		LinearLayout linearMeio = new LinearLayout(this);
		linearMeio.addView(meioButton);
		linearMeio.setGravity(Gravity.CENTER);
		linearMeio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		layoutMeio.addView(linearMeio);
		layoutMeio.setGravity(Gravity.BOTTOM);

		addContentView(layoutAnterior, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutMeio, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		addContentView(layoutProximo, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private Drawable carregaDrawable(byte[] imagem) {
		Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
				b.getHeight(), matrix, true);
		return new BitmapDrawable(girado);
	}

	private class VoltaListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorRoupa.voltaRoupa();
		}

	}

	private class CalibrarListener implements OnClickListener {

		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(),
					CalibragemRoupasActivity.class);
			i.putExtra("roupa", visualizadorRoupa.getRoupaAtual());
			startActivity(i);
		}

	}

	private class ProximoListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorRoupa.proximaRoupa();
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

	@Override
	protected void onResume() {
		super.onResume();
		visualizadorRoupa.invalidate();
	}

	public class VisualizadorRoupa extends View {

		private List<Roupa> roupas;

		private int posicaoRoupa;
		private Drawable roupaAtual;

		public VisualizadorRoupa(Context context, List<Roupa> roupas) {
			super(context);
			this.roupas = roupas;
			if (!roupas.isEmpty()) {
				roupaAtual = carregaDrawable(roupas.get(posicaoRoupa)
						.getImagem());

				Calibragem calibragemModelo = dao.getCalibragens().get(getRoupaAtual().getCategoria());
				Calibragem2 calibragemRoupa = dao.getCalibragens2().get(
						getRoupaAtual().getId());
				if (calibragemRoupa != null) {
					roupaAtual.setBounds(calibragemRoupa.left, calibragemRoupa.top,
							calibragemRoupa.right, calibragemRoupa.bottom);					
				} else {
					roupaAtual.setBounds(calibragemModelo.left, calibragemModelo.top,
							calibragemModelo.right, calibragemModelo.bottom);	
				}
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
			Roupa roupa = roupas.get(posicaoRoupa);
			roupaAtual = carregaDrawable(roupa.getImagem());
			Calibragem calibragemModelo = dao.getCalibragens().get(getRoupaAtual().getCategoria());
			Calibragem2 calibragemRoupa = dao.getCalibragens2().get(
					getRoupaAtual().getId());
			if (calibragemRoupa != null) {
				roupaAtual.setBounds(calibragemRoupa.left, calibragemRoupa.top,
						calibragemRoupa.right, calibragemRoupa.bottom);					
			} else {
				roupaAtual.setBounds(calibragemModelo.left, calibragemModelo.top,
						calibragemModelo.right, calibragemModelo.bottom);	
			}
			roupaAtual.draw(canvas);
		}

		public void proximaRoupa() {
			if (posicaoRoupa < roupas.size() - 1) {
				posicaoRoupa++;
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaRoupa() {
			if (posicaoRoupa > 0) {
				posicaoRoupa--;
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
			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
					b.getHeight(), matrix, true);
			return new BitmapDrawable(girado);
		}

		public Roupa getRoupaAtual() {
			return roupas.get(posicaoRoupa);
		}
	}
}