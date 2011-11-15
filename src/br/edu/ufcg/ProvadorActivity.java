package br.edu.ufcg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Roupa;

public class ProvadorActivity extends Activity {

	private boolean DEBUG = false;

	private BDAdapter dao;

	private List<Roupa> roupas;

	private Provador provador;

	private ImageButton voltaSuperiorButton;
	private ImageButton voltaInferiorButton;
	private ImageButton proximaSuperiorButton;
	private ImageButton proximaInferiorButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		if (getIntent().getExtras().get("manequimFaltando").equals(true)) {
//			System.out.println("PARA REDIRECIONAR");
//			Button botaoRedirecionar = new Button(this);
//			botaoRedirecionar.setBackgroundColor(Color.GREEN);
//			botaoRedirecionar.setText("Cadastrar Manequim");
//			botaoRedirecionar.setOnClickListener(new OnClickListener() {
//				public void onClick(View v2) {
//					ProvadorActivity.this.finish();
//					Intent i = new Intent(v2.getContext(), ProvadorActivity.class);
//					startActivity(i);
//				}
//			});
//			addContentView(botaoRedirecionar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			
//		} else {
			dao = new BDAdapter(this);
			this.roupas = dao.getRoupas();

			byte[] imagem = (byte[]) getIntent().getExtras().get("background");

			Bitmap b = null;
			if (DEBUG) {
				b = BitmapFactory.decodeResource(getResources(), R.drawable.background);
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			BitmapDrawable bd = new BitmapDrawable(girado);

			List<Roupa> roupasSuperiores = carregaRoupasSuperiores();
			List<Roupa> roupasInferiores = carregaRoupasInferiores();

			if (roupasSuperiores.isEmpty() && roupasInferiores.isEmpty()) {
				Toast.makeText(this, "Não há roupas cadastradas!", Toast.LENGTH_LONG).show();
			}

			provador = new Provador(this, roupasSuperiores, roupasInferiores);
			provador.setBackgroundDrawable(bd);
			setContentView(provador);

			addContentView(getLayoutBotoesEsquerda(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			addContentView(getLayoutBotoesDireita(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
		
		

	}

	private RelativeLayout getLayoutBotoesEsquerda() {
		RelativeLayout layout = new RelativeLayout(this);

		LinearLayout esquerda = new LinearLayout(this);
		esquerda.setOrientation(LinearLayout.VERTICAL);
		esquerda.setGravity(Gravity.CENTER);

		voltaSuperiorButton = new ImageButton(this);
		voltaSuperiorButton.setImageResource(R.drawable.previous_cinza);
		voltaSuperiorButton.setBackgroundColor(Color.TRANSPARENT);
		voltaSuperiorButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				provador.voltaRoupaSuperior();
			}
		});
		voltaInferiorButton = new ImageButton(this);
		voltaInferiorButton.setImageResource(R.drawable.previous_cinza);
		voltaInferiorButton.setBackgroundColor(Color.TRANSPARENT);
		voltaInferiorButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				provador.voltaRoupaInferior();
			}
		});

		esquerda.addView(voltaSuperiorButton);
		esquerda.addView(voltaInferiorButton);

		layout.addView(esquerda, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return layout;
	}

	private RelativeLayout getLayoutBotoesDireita() {
		RelativeLayout layout = new RelativeLayout(this);

		LinearLayout direita = new LinearLayout(this);
		direita.setOrientation(LinearLayout.VERTICAL);
		direita.setGravity(Gravity.CENTER);

		proximaSuperiorButton = new ImageButton(this);
		if (provador.roupasSuperiores.size() <= 1) {
			proximaSuperiorButton.setImageResource(R.drawable.next_cinza);
		} else {
			proximaSuperiorButton.setImageResource(R.drawable.next);
		}
		proximaSuperiorButton.setBackgroundColor(Color.TRANSPARENT);
		proximaSuperiorButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				provador.proximaRoupaSuperior();
			}
		});

		proximaInferiorButton = new ImageButton(this);
		if (provador.roupasInferiores.size() <= 1) {
			proximaInferiorButton.setImageResource(R.drawable.next_cinza);
		} else {
			proximaInferiorButton.setImageResource(R.drawable.next);
		}
		proximaInferiorButton.setBackgroundColor(Color.TRANSPARENT);
		proximaInferiorButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				provador.proximaRoupaInferior();
			}
		});

		direita.addView(proximaSuperiorButton);
		direita.addView(proximaInferiorButton);

		layout.setGravity(Gravity.RIGHT);
		layout.addView(direita, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return layout;
	}

	private List<Roupa> carregaRoupasSuperiores() {
		List<Categoria> superiores = Arrays.asList(new Categoria[] {Categoria.CAMISA, Categoria.CAMISETA, Categoria.CAMISA_MANGA_LONGA, Categoria.VESTIDO});
		List<Roupa> roupas = new ArrayList<Roupa>();
		for (Roupa roupa : this.roupas) {
			if (superiores.contains(roupa.getCategoria())) {
				roupas.add(roupa);
			}
		}
		return roupas;
	}

	private List<Roupa> carregaRoupasInferiores() {
		List<Categoria> inferiores = Arrays.asList(new Categoria[] {Categoria.BERMUDA, Categoria.CALCA, Categoria.SAIA, Categoria.SHORT});
		List<Roupa> roupas = new ArrayList<Roupa>();
		for (Roupa roupa : this.roupas) {
			if (inferiores.contains(roupa.getCategoria())) {
				roupas.add(roupa);
			}
		}
		return roupas;
	}

	private Drawable carregaDrawable(byte[] imagem) {
		Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		return new BitmapDrawable(girado);
	}

	public class Provador extends View {

		private Map<Categoria,Calibragem> calibragens;

		private List<Roupa> roupasSuperiores;
		private List<Roupa> roupasInferiores;

		private int posicaoRoupaSuperior;
		private int posicaoRoupaInferior;

		private Drawable roupaSuperior;
		private Drawable roupaInferior;

		public Provador(Context context, List<Roupa> roupasSuperiores, List<Roupa> roupasInferiores) {
			super(context);
			this.roupasSuperiores = roupasSuperiores;
			this.roupasInferiores = roupasInferiores;

			calibragens = dao.getCalibragens();

			if (!roupasSuperiores.isEmpty()) {
				Calibragem calibragemS = calibragens.get(roupasSuperiores.get(posicaoRoupaSuperior).getCategoria());
				roupaSuperior = carregaDrawable(roupasSuperiores.get(posicaoRoupaSuperior).getImagem());
				roupaSuperior.setBounds(calibragemS.left, calibragemS.top, calibragemS.right, calibragemS.bottom);
			}

			if (!roupasInferiores.isEmpty()) {
				Calibragem calibragemI = calibragens.get(roupasInferiores.get(posicaoRoupaInferior).getCategoria());
				roupaInferior = carregaDrawable(roupasInferiores.get(posicaoRoupaInferior).getImagem());
				roupaInferior.setBounds(calibragemI.left, calibragemI.top, calibragemI.right, calibragemI.bottom);
			}

			setFocusable(true);
			this.requestFocus();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (roupaInferior != null) {
				roupaInferior.draw(canvas);
			}
			if (roupaSuperior != null) {
				roupaSuperior.draw(canvas);
			}
		}

		public void proximaRoupaSuperior() {
			if (posicaoRoupaSuperior < roupasSuperiores.size()-1) {
				posicaoRoupaSuperior++;
				Roupa roupa = roupasSuperiores.get(posicaoRoupaSuperior);
				roupaSuperior = carregaDrawable(roupa.getImagem());
				Calibragem calibragemS = calibragens.get(roupa.getCategoria());
				roupaSuperior.setBounds(calibragemS.left, calibragemS.top, calibragemS.right, calibragemS.bottom);
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaRoupaSuperior() {
			if (posicaoRoupaSuperior > 0) {
				posicaoRoupaSuperior--;
				Roupa roupa = roupasSuperiores.get(posicaoRoupaSuperior);
				roupaSuperior = carregaDrawable(roupa.getImagem());
				Calibragem calibragemS = calibragens.get(roupa.getCategoria());
				roupaSuperior.setBounds(calibragemS.left, calibragemS.top, calibragemS.right, calibragemS.bottom);
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void proximaRoupaInferior() {
			if (posicaoRoupaInferior < roupasInferiores.size()-1) {
				posicaoRoupaInferior++;
				Roupa roupa = roupasInferiores.get(posicaoRoupaInferior);
				roupaInferior = carregaDrawable(roupa.getImagem());
				Calibragem calibragemI = calibragens.get(roupa.getCategoria());
				roupaInferior.setBounds(calibragemI.left, calibragemI.top, calibragemI.right, calibragemI.bottom);
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaRoupaInferior() {
			if (posicaoRoupaInferior > 0) {
				posicaoRoupaInferior--;
				Roupa roupa = roupasInferiores.get(posicaoRoupaInferior);
				roupaInferior = carregaDrawable(roupa.getImagem());
				Calibragem calibragemI = calibragens.get(roupa.getCategoria());
				roupaInferior.setBounds(calibragemI.left, calibragemI.top, calibragemI.right, calibragemI.bottom);
				invalidate();
			}
			atualizaImagensBotoes();
		}

		private void atualizaImagensBotoes() {
			if (posicaoRoupaInferior > 0) {
				voltaInferiorButton.setImageResource(R.drawable.previous);
			} else {
				voltaInferiorButton.setImageResource(R.drawable.previous_cinza);
			}

			if (posicaoRoupaInferior < roupasInferiores.size() - 1) {
				proximaInferiorButton.setImageResource(R.drawable.next);
			} else {
				proximaInferiorButton.setImageResource(R.drawable.next_cinza);
			}

			if (posicaoRoupaSuperior > 0) {
				voltaSuperiorButton.setImageResource(R.drawable.previous);
			} else {
				voltaSuperiorButton.setImageResource(R.drawable.previous_cinza);
			}

			if (posicaoRoupaSuperior < roupasSuperiores.size() - 1) {
				proximaSuperiorButton.setImageResource(R.drawable.next);
			} else {
				proximaSuperiorButton.setImageResource(R.drawable.next_cinza);
			}
		}
	}
}