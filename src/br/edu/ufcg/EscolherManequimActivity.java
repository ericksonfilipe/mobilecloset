package br.edu.ufcg;

import java.io.Serializable;
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
import br.edu.ufcg.async.Listener;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Manequim;
import br.edu.ufcg.model.Roupa;

public class EscolherManequimActivity extends Activity {

	private BDAdapter dao;
	private VisualizadorManequim visualizadorManequim;
	public ImageButton anteriorButton;
	public ImageButton proximoButton;
	public ImageButton addButton;
	public ImageButton deleteButton;
	public ImageButton escolherButton;

	public class RetornoListener implements Listener, Serializable {

		public void notifica() {
			visualizadorManequim.removeImagem();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new BDAdapter(this);

		visualizadorManequim = new VisualizadorManequim(this, dao.getManequins());
		setContentView(visualizadorManequim);


		addButton = new ImageButton(this);
		addButton.setImageResource(R.drawable.add);
		addButton.setBackgroundColor(Color.TRANSPARENT);
		addButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), TirarFotoManequimActivity.class);
				startActivity(i);				
			}
		});

		deleteButton = new ImageButton(this);
		deleteButton.setImageResource(R.drawable.delete);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		deleteButton.setOnClickListener(new OnClickListener() {

			//Opcao "Deseja realmente excluir"
			public void onClick(View v) {
				if (visualizadorManequim.ehManequimPadrao()) {
					Toast.makeText(v.getContext(), "Este manequim é padrão do MobileCloset. Não pode ser excluído.", Toast.LENGTH_LONG).show();
				} else {
					Intent i = new Intent(v.getContext(), ExcluirManequim.class);
					//i.putExtra("listener", new RetornoListener());
					startActivity(i);
				}
			}
		});

		proximoButton = new ImageButton(this);
		if (dao.getManequins().size() > 1) {
			proximoButton.setImageResource(R.drawable.next);
		} else {			
			proximoButton.setImageResource(R.drawable.next_cinza);
		}
		proximoButton.setBackgroundColor(Color.TRANSPARENT);
		proximoButton.setOnClickListener(new ProximoListener());

		anteriorButton = new ImageButton(this);
		anteriorButton.setImageResource(R.drawable.previous_cinza);
		anteriorButton.setBackgroundColor(Color.TRANSPARENT);
		anteriorButton.setOnClickListener(new VoltaListener());

		//		Button meioButton = new Button(this);
		//		meioButton.setText("Escolher!");
		//		meioButton.setOnClickListener(new CalibrarListener());

		RelativeLayout layoutAdd = new RelativeLayout(this);
		LinearLayout linearAdd = new LinearLayout(this);
		linearAdd.addView(addButton);
		linearAdd.setGravity(Gravity.LEFT);
		linearAdd.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutAdd.addView(linearAdd);
		layoutAdd.setGravity(Gravity.BOTTOM);

		RelativeLayout layoutDelete = new RelativeLayout(this);
		LinearLayout linearDelete = new LinearLayout(this);
		linearDelete.addView(deleteButton);
		linearDelete.setGravity(Gravity.RIGHT);
		linearDelete.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutDelete.addView(linearDelete);
		layoutDelete.setGravity(Gravity.BOTTOM);

		RelativeLayout layoutProximo = new RelativeLayout(this);
		LinearLayout linearProximo = new LinearLayout(this);
		linearProximo.addView(proximoButton);
		linearProximo.setGravity(Gravity.RIGHT);
		linearProximo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutProximo.addView(linearProximo);
		layoutProximo.setGravity(Gravity.CENTER);

		RelativeLayout layoutAnterior = new RelativeLayout(this);
		LinearLayout linearAnterior = new LinearLayout(this);
		linearAnterior.addView(anteriorButton);
		linearAnterior.setGravity(Gravity.LEFT);
		linearAnterior.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutAnterior.addView(linearAnterior);
		layoutAnterior.setGravity(Gravity.CENTER);

		//		RelativeLayout layoutMeio = new RelativeLayout(this);
		//		LinearLayout linearMeio = new LinearLayout(this);
		//		linearMeio.addView(meioButton);
		//		linearMeio.setGravity(Gravity.CENTER);
		//		linearMeio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		//		layoutMeio.addView(linearMeio);
		//		layoutMeio.setGravity(Gravity.BOTTOM);

		escolherButton = new ImageButton(this);
		escolherButton.setImageResource(R.drawable.escolher);
		escolherButton.setBackgroundColor(Color.TRANSPARENT);
		escolherButton.setOnClickListener(new CalibrarListener());

		RelativeLayout layoutMeio = new RelativeLayout(this);
		LinearLayout linearMeio = new LinearLayout(this);
		linearMeio.addView(escolherButton);
		linearMeio.setGravity(Gravity.CENTER);
		linearMeio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		layoutMeio.addView(linearMeio);
		layoutMeio.setGravity(Gravity.BOTTOM);

		addContentView(layoutAnterior, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutMeio, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutProximo, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutAdd, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutDelete, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		visualizadorManequim.setManequins(dao.getManequins());
		visualizadorManequim.setPosicao(dao.getManequins().size() - 1);
		visualizadorManequim.atualizaImagensBotoes();

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
			Manequim manequimEscolhido = visualizadorManequim.getManequim();
			dao.inserirManequimPadrao(manequimEscolhido);
			finish();
			startActivity(new Intent(arg0.getContext(), TesteDeteccaoFace.class));
		}
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		MenuInflater inflate = new MenuInflater(this);
	//		inflate.inflate(R.menu.escolher_manequim_menu, menu);
	//		return true;
	//	}
	//
	//	@Override
	//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
	//		visualizadorManequim.removeImagem();
	//		return true;
	//	}

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

		public boolean ehManequimPadrao() {
			return posicao == 0;
		}

		public void removeImagem() {
			if (ehManequimPadrao()) {
				Toast.makeText(getContext(), "Este manequim é padrão do MobileCloset. Não pode ser excluído.", Toast.LENGTH_LONG).show();
			} else {
				if (dao.getIdManequimPadrao() == manequins.get(posicao).getId()) {
					dao.inserirManequimPadrao(manequins.get(0));
				}

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
				atualizaImagensBotoes();
			}
		}

		//		@Override
		//		protected void onDraw(Canvas canvas) {
		//			super.onDraw(canvas);
		//			if (manequimAtual != null) {
		//				manequimAtual.draw(canvas);
		//			}
		//		}

		public Manequim getManequim() {
			return manequins.get(posicao);
		}

		public List<Manequim> getManequins() {
			return manequins;
		}

		public void setManequins(List<Manequim> manequins) {
			this.manequins = manequins;
		}

		public int getPosicao() {
			return posicao;
		}

		public void setPosicao(int posicao) {
			this.posicao = posicao;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (!manequins.isEmpty()) {
				Manequim manequim = manequins.get(posicao);
				manequimAtual = carregaDrawable(manequim.getImagem());
				manequimAtual.setBounds(0, 0, getWidth(), getHeight());
				manequimAtual.draw(canvas);				
			}
		}

		public void proximoManequim() {
			if (posicao < manequins.size()-1) {
				posicao++;
				//				Manequim manequim = manequins.get(posicao);
				//				manequimAtual = carregaDrawable(manequim.getImagem());
				//				manequimAtual.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaManequim() {
			if (posicao > 0) {
				posicao--;
				Manequim manequim = manequins.get(posicao);
				manequimAtual = carregaDrawable(manequim.getImagem());
				manequimAtual.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void atualizaImagensBotoes() {
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