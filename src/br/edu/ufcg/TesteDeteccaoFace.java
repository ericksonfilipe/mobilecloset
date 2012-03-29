package br.edu.ufcg;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Roupa;

public class TesteDeteccaoFace extends Activity {

//	private BDAdapter dao;

	private Provador provador;

	private ImageButton voltaSuperiorButton;
	private ImageButton voltaInferiorButton;
	private ImageButton proximaSuperiorButton;
	private ImageButton proximaInferiorButton;

	public Integer leftSuperior;
	public Integer topSuperior;
	public Integer rightSuperior;
	public Integer bottomSuperior;

//	private Face[] arrayFaces;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		dao = new BDAdapter(this);

		Bitmap b = BitmapFactory.decodeResource(getResources(),
				R.drawable.pessoa2);

//		FaceDetector f = new FaceDetector(b.getWidth(), b.getHeight(), 1);
//		arrayFaces = new Face[1];
//		int numFace = f.findFaces(b, arrayFaces);
//
//		Face face = arrayFaces[0];
//		PointF point = new PointF();
//		face.getMidPoint(point);

//		System.out.println("x: " + point.x);
//		System.out.println((int) point.x);
//		System.out.println("y: " + point.y);
//		System.out.println((int) point.y);
		
		
		//ponto da esquerda
		leftSuperior = (int) (10); //ponto central da face 130; 130-120=10 [leftSuperior]  130+120=250 [rightSuperior]
		//ponto da direita
		rightSuperior = (int) (250);
		//ponto do topo
		topSuperior = (int) 80;
		//ponto do chao
		bottomSuperior = (int) 210;

//		System.err.println("-------------- aqui: numero de faces: " + numFace);

		Matrix matrix = new Matrix();
		// matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
				b.getHeight(), matrix, true);
		BitmapDrawable bd = new BitmapDrawable(girado);

		List<Roupa> roupasSuperiores = carregaRoupasSuperiores();
		List<Roupa> roupasInferiores = carregaRoupasInferiores();

		if (roupasSuperiores.isEmpty() && roupasInferiores.isEmpty()) {
			Toast.makeText(this, "Não há roupas cadastradas!",
					Toast.LENGTH_LONG).show();
		}

		provador = new Provador(this, roupasSuperiores, roupasInferiores);
		provador.setBackgroundDrawable(bd);
		setContentView(provador);

		addContentView(getLayoutBotoesEsquerda(), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addContentView(getLayoutBotoesDireita(), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

		layout.addView(esquerda, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
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
		layout.addView(direita, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		return layout;
	}

	private List<Roupa> carregaRoupasSuperiores() {
		List<Categoria> superiores = Arrays.asList(new Categoria[] {
				Categoria.CAMISA, Categoria.CAMISETA,
				Categoria.CAMISA_MANGA_LONGA, Categoria.VESTIDO });
		List<Roupa> roupas = new ArrayList<Roupa>();

		Bitmap b = BitmapFactory.decodeResource(getResources(),
				R.drawable.camisa_preta);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		b.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		
		Roupa r = new Roupa(2, bitmapdata , Categoria.CAMISA);
//		for (Roupa roupa : dao.getRoupas()) {
//			if (superiores.contains(roupa.getCategoria())) {
//				roupas.add(roupa);
//			}
//		}
		roupas.add(r);
		return roupas;
	}

	private List<Roupa> carregaRoupasInferiores() {
		List<Categoria> inferiores = Arrays.asList(new Categoria[] {
				Categoria.BERMUDA, Categoria.CALCA, Categoria.SAIA,
				Categoria.SHORT });
		List<Roupa> roupas = new ArrayList<Roupa>();
//		for (Roupa roupa : dao.getRoupas()) {
//			if (inferiores.contains(roupa.getCategoria())) {
//				roupas.add(roupa);
//			}
//		}
		return roupas;
	}

	private Drawable carregaDrawable(byte[] imagem) {
		Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
		Matrix matrix = new Matrix();
//		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
				b.getHeight(), matrix, true);
		return new BitmapDrawable(girado);
	}

	public class Provador extends View {

		private List<Roupa> roupasSuperiores;
		private List<Roupa> roupasInferiores;

		private int posicaoRoupaSuperior;
		private int posicaoRoupaInferior;

		private Drawable roupaSuperior;
		private Drawable roupaInferior;

		public Provador(Context context, List<Roupa> roupasSuperiores,
				List<Roupa> roupasInferiores) {
			super(context);
			this.roupasSuperiores = roupasSuperiores;
			this.roupasInferiores = roupasInferiores;

			if (!roupasSuperiores.isEmpty()) {
				roupaSuperior = carregaDrawable(roupasSuperiores.get(
						posicaoRoupaSuperior).getImagem());
				roupaSuperior.setBounds(leftSuperior, topSuperior, rightSuperior, bottomSuperior);
			}

			if (!roupasInferiores.isEmpty()) {
				roupaInferior = carregaDrawable(roupasInferiores.get(
						posicaoRoupaInferior).getImagem());
				roupaInferior.setBounds(10, 10, 10, 10);
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
			if (posicaoRoupaSuperior < roupasSuperiores.size() - 1) {
				posicaoRoupaSuperior++;
				Roupa roupa = roupasSuperiores.get(posicaoRoupaSuperior);
				roupaSuperior = carregaDrawable(roupa.getImagem());
				roupaSuperior.setBounds(10, 10, 10, 10);
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaRoupaSuperior() {
			if (posicaoRoupaSuperior > 0) {
				posicaoRoupaSuperior--;
				Roupa roupa = roupasSuperiores.get(posicaoRoupaSuperior);
				roupaSuperior = carregaDrawable(roupa.getImagem());
				roupaSuperior.setBounds(18, 19, 10, 10);
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void proximaRoupaInferior() {
			if (posicaoRoupaInferior < roupasInferiores.size() - 1) {
				posicaoRoupaInferior++;
				Roupa roupa = roupasInferiores.get(posicaoRoupaInferior);
				roupaInferior = carregaDrawable(roupa.getImagem());
				roupaInferior.setBounds(10, 10, 20, 20);
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaRoupaInferior() {
			if (posicaoRoupaInferior > 0) {
				posicaoRoupaInferior--;
				Roupa roupa = roupasInferiores.get(posicaoRoupaInferior);
				roupaInferior = carregaDrawable(roupa.getImagem());
				roupaInferior.setBounds(10, 10, 20, 20);
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