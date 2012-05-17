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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Look;

public class FavoritosActivity extends Activity {

	private boolean DEBUG = false;

	private BDAdapter dao;

	private Favoritos favorito;

	private ImageButton voltaButton;
	private ImageButton proximaButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			dao = new BDAdapter(this);

//			byte[] imagem = (byte[]) getIntent().getExtras().get("background");

//			Bitmap b = null;
//			//if (DEBUG) {
//			if (imagem == null) {
//				b = BitmapFactory.decodeResource(getResources(), R.drawable.manequim_padrao);
//			} else {
//				b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
//			}
//
//			Matrix matrix = new Matrix();
//			matrix.setRotate(90);
//			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
//			BitmapDrawable bd = new BitmapDrawable(girado);
//
			favorito = new Favoritos(this);
//			favorito.setBackgroundDrawable(bd);
			setContentView(favorito);
			
			addContentView(getLayoutBotoesEsquerda(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			addContentView(getLayoutBotoesDireita(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private RelativeLayout getLayoutBotoesEsquerda() {
		RelativeLayout layout = new RelativeLayout(this);

		LinearLayout esquerda = new LinearLayout(this);
		esquerda.setOrientation(LinearLayout.VERTICAL);
		esquerda.setGravity(Gravity.CENTER);

		voltaButton = new ImageButton(this);
		voltaButton.setImageResource(R.drawable.previous_cinza);
		voltaButton.setBackgroundColor(Color.TRANSPARENT);
		voltaButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				favorito.voltaLook();
			}
		});

		esquerda.addView(voltaButton);

		layout.addView(esquerda, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return layout;
	}

	private RelativeLayout getLayoutBotoesDireita() {
		RelativeLayout layout = new RelativeLayout(this);

		LinearLayout direita = new LinearLayout(this);
		direita.setOrientation(LinearLayout.VERTICAL);
		direita.setGravity(Gravity.CENTER);

		proximaButton = new ImageButton(this);
		if (favorito.listaLooks.size() <= 1) {
			proximaButton.setImageResource(R.drawable.next_cinza);
		} else {
			proximaButton.setImageResource(R.drawable.next);
		}
		proximaButton.setBackgroundColor(Color.TRANSPARENT);
		proximaButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				favorito.proximoLook();
			}
		});

		direita.addView(proximaButton);

		layout.setGravity(Gravity.RIGHT);
		layout.addView(direita, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return layout;
	}

	private Drawable carregaDrawable(byte[] imagem) {
		Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		return new BitmapDrawable(girado);
	}

	public class Favoritos extends View {

		private int posicao;
		
		public List<Look> listaLooks;

		private Drawable look;

		public Favoritos(Context context) {
			super(context);
			
			listaLooks = dao.getLooks();
			look = carregaDrawable(listaLooks.get(posicao).getImagem());
			look.setBounds(0, 0, getWidth(), getHeight());

			setFocusable(true);
			this.requestFocus();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			look.draw(canvas);
		}

		public void proximoLook() {
			if (posicao < listaLooks.size()-1) {
				posicao++;
				Look novolook = listaLooks.get(posicao);
				look = carregaDrawable(novolook.getImagem());
				look.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaLook() {
			if (posicao > 0) {
				posicao--;
				Look novolook = listaLooks.get(posicao);
				look = carregaDrawable(novolook.getImagem());
				look.setBounds(0, 0, getWidth(), getHeight());
				invalidate();
			}
			atualizaImagensBotoes();
		}

		private void atualizaImagensBotoes() {
			if (posicao > 0) {
				voltaButton.setImageResource(R.drawable.previous);
			} else {
				voltaButton.setImageResource(R.drawable.previous_cinza);
			}

			if (posicao < listaLooks.size() - 1) {
				proximaButton.setImageResource(R.drawable.next);
			} else {
				proximaButton.setImageResource(R.drawable.next_cinza);
			}
		}
	}
}