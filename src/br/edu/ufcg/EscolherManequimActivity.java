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
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Manequim;

public class EscolherManequimActivity extends Activity {

	private BDAdapter dao;
	private VisualizadorManequim visualizadorManequim;
	public ImageButton anteriorButton;
	public ImageButton proximoButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new BDAdapter(this);

		visualizadorManequim = new VisualizadorManequim(this, dao.getManequins());
		setContentView(visualizadorManequim);

		proximoButton = new ImageButton(this);
		proximoButton.setImageResource(R.drawable.next);
		proximoButton.setBackgroundColor(Color.TRANSPARENT);
		proximoButton.setOnClickListener(new ProximoListener());

		anteriorButton = new ImageButton(this);
		anteriorButton.setImageResource(R.drawable.previous_cinza);
		anteriorButton.setBackgroundColor(Color.TRANSPARENT);
		anteriorButton.setOnClickListener(new VoltaListener());
		
		Button meioButton = new Button(this);
		meioButton.setText("Calibrar");
		meioButton.setOnClickListener(new CalibrarListener());

		RelativeLayout layoutProximo = new RelativeLayout(this);
		LinearLayout linearProximo = new LinearLayout(this);
		linearProximo.addView(proximoButton);
		linearProximo.setGravity(Gravity.RIGHT);
		linearProximo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutProximo.addView(linearProximo);
		layoutProximo.setGravity(Gravity.BOTTOM);
		
		RelativeLayout layoutAnterior = new RelativeLayout(this);
		LinearLayout linearAnterior = new LinearLayout(this);
		linearAnterior.addView(anteriorButton);
		linearAnterior.setGravity(Gravity.LEFT);
		linearAnterior.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutAnterior.addView(linearAnterior);
		layoutAnterior.setGravity(Gravity.BOTTOM);
		
		RelativeLayout layoutMeio = new RelativeLayout(this);
		LinearLayout linearMeio = new LinearLayout(this);
		linearMeio.addView(meioButton);
		linearMeio.setGravity(Gravity.CENTER);
		linearMeio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutMeio.addView(linearMeio);
		layoutMeio.setGravity(Gravity.BOTTOM);

		addContentView(layoutAnterior, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutMeio, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutProximo, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private class VoltaListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorManequim.voltaManequim();
		}

	}

	private class ProximoListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorManequim.proximoManequim();
		}
	}
	
	private class CalibrarListener implements OnClickListener {

		public void onClick(View arg0) {
			Toast.makeText(EscolherManequimActivity.this, "Agora, calibre o molde das roupas no manequim escolhido.", Toast.LENGTH_LONG).show();
			Manequim manequimEscolhido = visualizadorManequim.getManequim();
			dao.inserirManequimPadrao(manequimEscolhido);
			Intent i = new Intent(EscolherManequimActivity.this, CalibragemRoupasActivity.class);
			i.putExtra("background", manequimEscolhido.getImagem());
			finish();
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.ver_roupa_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		visualizadorManequim.removeImagem();
		return true;
	}

	public class VisualizadorManequim extends View {

		private List<Manequim> manequins;

		private int posicao;
		private Drawable manequimAtual;


		public VisualizadorManequim(Context context, List<Manequim> manequins) {
			super(context);
			this.manequins = manequins;
			if (!manequins.isEmpty()) {
				manequimAtual = carregaDrawable(manequins.get(posicao).getImagem());
				manequimAtual.setBounds(0, 0, getApplicationContext().getResources().getDisplayMetrics().widthPixels, getApplicationContext().getResources().getDisplayMetrics().heightPixels);
			}

			setFocusable(true);
			this.requestFocus();
			this.requestLayout();
		}

		public void removeImagem() {
			dao.removeManequim(manequins.get(posicao));
			manequins.remove(posicao);
			if (manequins.isEmpty()) {
				finish();
				return;
			} else if (posicao == manequins.size()) {
				posicao--;
			}
			Manequim manequim = manequins.get(posicao);
			manequimAtual = carregaDrawable(manequim.getImagem());
			manequimAtual.setBounds(0, 0, getWidth(), getHeight());
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (manequimAtual != null) {
				manequimAtual.draw(canvas);
			}
		}
		
		public Manequim getManequim() {
			return manequins.get(posicao);
		}

		public void proximoManequim() {
			if (posicao < manequins.size()-1) {
				posicao++;
				Manequim roupa = manequins.get(posicao);
				manequimAtual = carregaDrawable(roupa.getImagem());
				manequimAtual.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaManequim() {
			if (posicao > 0) {
				posicao--;
				Manequim roupa = manequins.get(posicao);
				manequimAtual = carregaDrawable(roupa.getImagem());
				manequimAtual.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		private void atualizaImagensBotoes() {
			if (posicao > 0) {
				anteriorButton.setImageResource(R.drawable.previous);
			} else {
				anteriorButton.setImageResource(R.drawable.previous_cinza);
			}

			if (posicao < manequins.size() - 1) {
				proximoButton.setImageResource(R.drawable.next);
			} else {
				proximoButton.setImageResource(R.drawable.next_cinza);
			}
		}
	
		private Drawable carregaDrawable(byte[] imagem) {
			Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			return new BitmapDrawable(girado);
		}
	}
}