package br.edu.ufcg;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Calibragem2;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Look;
import br.edu.ufcg.model.Roupa;

public class ProvadorActivity extends Activity {

	private boolean DEBUG = false;

	private BDAdapter dao;

	private Map<Categoria, Calibragem> calibragens;

	private Provador provador;

	private ImageButton voltaSuperiorButton;
	private ImageButton voltaInferiorButton;
	private ImageButton proximaSuperiorButton;
	private ImageButton proximaInferiorButton;
	
	public ImageButton favoritoButton, menuButton;

	private Map<Integer, Calibragem2> calibragens2;

	private LinearLayout informacaoCima;

	private ImageButton logoLojaCima;

	private LinearLayout informacaoBaixo;

	private ImageButton logoLojaBaixo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			dao = new BDAdapter(this);
			this.calibragens = dao.getCalibragens();
			this.calibragens2 = dao.getCalibragens2();

			byte[] imagem = (byte[]) getIntent().getExtras().get("background");

			Bitmap b = null;
			//if (DEBUG) {
			if (imagem == null) {
				b = BitmapFactory.decodeResource(getResources(), R.drawable.manequim_padrao);
			} else {
				b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			}

			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			BitmapDrawable bd = new BitmapDrawable(girado);

			List<Roupa> roupasSuperiores = carregaRoupasSuperiores();
			List<Roupa> roupasInferiores = carregaRoupasInferiores();

			if (roupasSuperiores.isEmpty() && roupasInferiores.isEmpty()) {
				Toast.makeText(this, "Não há roupas cadastradas!", Toast.LENGTH_SHORT).show();
			}
			
			Bitmap roupaTransparente = BitmapFactory.decodeResource(getResources(), R.drawable.blank);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			roupaTransparente.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
			byte[] bitmapRoupaTransparente = bos.toByteArray();
			
			Roupa rTransparente = new Roupa(-1, bitmapRoupaTransparente, Categoria.VESTIDO);
			roupasSuperiores.add(rTransparente);
			roupasInferiores.add(rTransparente);
			
			provador = new Provador(this, roupasSuperiores, roupasInferiores);
			provador.setBackgroundDrawable(bd);
			setContentView(provador);
			
			favoritoButton = new ImageButton(this);
			favoritoButton.setImageResource(R.drawable.estrela);
			favoritoButton.setBackgroundColor(Color.TRANSPARENT);
			favoritoButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Bitmap bitmap = loadBitmapFromView(provador);
					salvaLookBanco(bitmap);
				}
			});
			
			RelativeLayout layoutFavorito = new RelativeLayout(this);
			LinearLayout linearFavorito = new LinearLayout(this);
			linearFavorito.addView(favoritoButton);
			linearFavorito.setGravity(Gravity.RIGHT);
			linearFavorito.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layoutFavorito.addView(linearFavorito);
			layoutFavorito.setGravity(Gravity.BOTTOM);

			menuButton = new ImageButton(this);
			menuButton.setImageResource(R.drawable.options);
			menuButton.setBackgroundColor(Color.TRANSPARENT);
			menuButton.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					startActivity(new Intent(arg0.getContext(), OpcoesActivity.class));//TODO
				}
			});
			
			RelativeLayout layoutEsquerda = new RelativeLayout(this);
			LinearLayout linearEsq = new LinearLayout(this);
			linearEsq.addView(menuButton);
			linearEsq.setGravity(Gravity.LEFT);
			linearEsq.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layoutEsquerda.addView(linearEsq);
			layoutEsquerda.setGravity(Gravity.BOTTOM);
			
			infoRoupaSuperior();
			infoRoupaInferior();
			
			addContentView(getLayoutBotoesEsquerda(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			addContentView(getLayoutBotoesDireita(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			addContentView(layoutEsquerda, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			addContentView(layoutFavorito, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void infoRoupaSuperior() {
		informacaoCima = new LinearLayout(ProvadorActivity.this);
		informacaoCima.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		informacaoCima.setOrientation(LinearLayout.VERTICAL);
		LinearLayout imagem = new LinearLayout(ProvadorActivity.this);
		imagem.setLayoutParams(new LayoutParams(50, 50));
		
		logoLojaCima = new ImageButton(ProvadorActivity.this);
		logoLojaCima.setBackgroundColor(Color.TRANSPARENT);
		logoLojaCima.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(arg0.getContext(), InformacoesActivity.class);
				i.putExtra("roupaLoja", provador.getRoupaSuperiorAtual());
				startActivity(i);
			}
		});
		
		imagem.addView(logoLojaCima);
		informacaoCima.addView(imagem);
		addContentView(informacaoCima, informacaoCima.getLayoutParams());
	}

	private void infoRoupaInferior() {
		informacaoBaixo = new LinearLayout(ProvadorActivity.this);
		informacaoBaixo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		informacaoBaixo.setOrientation(LinearLayout.VERTICAL);
		informacaoBaixo.setGravity(Gravity.RIGHT);
		LinearLayout imagem = new LinearLayout(ProvadorActivity.this);
		imagem.setLayoutParams(new LayoutParams(50, 50));
		
		logoLojaBaixo = new ImageButton(ProvadorActivity.this);
		logoLojaBaixo.setBackgroundColor(Color.TRANSPARENT);
		logoLojaBaixo.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(arg0.getContext(), InformacoesActivity.class);
				i.putExtra("roupaLoja", provador.getRoupaInferiorAtual());
				startActivity(i);
			}
		});
		
		imagem.addView(logoLojaBaixo);
		informacaoBaixo.addView(imagem);
		addContentView(informacaoBaixo, informacaoBaixo.getLayoutParams());
	}
	
	private void salvaLookBanco(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		bitmap.compress(CompressFormat.PNG, 0 /*IGNORED FOR PNG*/, bos); 
		byte[] bitmapData = bos.toByteArray();
		
		Look look = new Look();
		look.setImagem(bitmapData);
		
		if (provador.getRoupaInferiorAtual().getLoja() != null) {
			look.setLogoLojaInferior(provador.getRoupaInferiorAtual().getLoja().getLogo());
			look.setCodigoRoupaInferior(provador.getRoupaInferiorAtual().getCodigo());			
		}
		
		if (provador.getRoupaSuperiorAtual().getLoja() != null) {
			look.setLogoLojaSuperior(provador.getRoupaSuperiorAtual().getLoja().getLogo());
			look.setCodigoRoupaSuperior(provador.getRoupaSuperiorAtual().getCodigo());			
		}
		
		dao.inserirLook(look);
		
		Toast.makeText(this, "Look salvo com sucesso!", Toast.LENGTH_SHORT).show();
	}
	
//	Metodo que Ed disse que salvava o canvas
	public static Bitmap loadBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(),
				v.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.draw(c);
		return b;
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
		List<Categoria> superiores = Arrays.asList(new Categoria[] {Categoria.CAMISA, Categoria.CAMISA_MANGA_LONGA, Categoria.CAMISETA, Categoria.VESTIDO});
		List<Roupa> roupas = new ArrayList<Roupa>();
		for (Roupa roupa : dao.getRoupas()) {
			if (superiores.contains(roupa.getCategoria())) {
				roupas.add(roupa);
			}
		}
		return roupas;
	}

	private List<Roupa> carregaRoupasInferiores() {
		List<Categoria> inferiores = Arrays.asList(new Categoria[] {Categoria.SHORT, Categoria.CALCA, Categoria.SAIA});
		List<Roupa> roupas = new ArrayList<Roupa>();
		for (Roupa roupa : dao.getRoupas()) {
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

	/*private void setLayout(LinearLayout l) {
		addContentView(l, l.getLayoutParams());
		
	}*/

	public class Provador extends View {

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

			if (!roupasSuperiores.isEmpty()) {
				Roupa roupa = roupasSuperiores.get(posicaoRoupaSuperior);
				Calibragem2 cS = calibragens2.get(roupa.getId());
				roupaSuperior = carregaDrawable(roupa.getImagem());
				if(cS != null) {
					roupaSuperior.setBounds(cS.left, cS.top, cS.right, cS.bottom);
				} else {
					Calibragem calibragemS = calibragens.get(roupa.getCategoria());
					if (calibragemS == null) {
						roupaSuperior.setBounds(0, 0,  100, 100);					
					} else {
						roupaSuperior.setBounds(calibragemS.left, calibragemS.top, calibragemS.right, calibragemS.bottom);					
					}
				}
				
			}

			if (!roupasInferiores.isEmpty()) {
				Roupa roupa = roupasInferiores.get(posicaoRoupaInferior);
				Calibragem2 cI = calibragens2.get(roupa.getId());
				roupaInferior = carregaDrawable(roupa.getImagem());
				if(cI != null) {
					roupaInferior.setBounds(cI.left, cI.top, cI.right, cI.bottom);
				} else {
					Calibragem calibragemI = calibragens.get(roupa.getCategoria());
					if (calibragemI == null) {
						roupaInferior.setBounds(0, 0,  100, 100);
					} else {
						roupaInferior.setBounds(calibragemI.left, calibragemI.top, calibragemI.right, calibragemI.bottom);
						
					}
				}
			}

			setFocusable(true);
			this.requestFocus();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (roupaInferior != null) {
				roupaInferior.draw(canvas);
				modificaInfoRoupa(roupasInferiores.get(posicaoRoupaInferior));
			}
			if (roupaSuperior != null) {
				roupaSuperior.draw(canvas);
				modificaInfoRoupa(roupasSuperiores.get(posicaoRoupaSuperior));
			}
		}

		private void modificaInfoRoupa(Roupa roupa) {
			if (roupa.getLoja() != null) {
				BitmapDrawable logo = new BitmapDrawable(BitmapFactory.decodeByteArray(roupa.getLoja().getLogo(), 0, roupa.getLoja().getLogo().length));
				if (roupa.isInferior()) {
					logoLojaBaixo.setBackgroundDrawable(logo);
				} else {
					logoLojaCima.setBackgroundDrawable(logo);
				}
			} else { 
				if (roupa.isInferior()) {
					logoLojaBaixo.setBackgroundDrawable(null);
				} else {
					logoLojaCima.setBackgroundDrawable(null);
				}
			}
		}

		
		public void proximaRoupaSuperior() {
			if (posicaoRoupaSuperior < roupasSuperiores.size()-1) {
				posicaoRoupaSuperior++;
				Roupa roupa = roupasSuperiores.get(posicaoRoupaSuperior);
				roupaSuperior = carregaDrawable(roupa.getImagem());
				Calibragem calibragemS = calibragens.get(roupa.getCategoria());
				Calibragem2 calibragem2S = calibragens2.get(roupa.getId());
				if(calibragem2S != null) {
					roupaSuperior.setBounds(calibragem2S.left, calibragem2S.top, calibragem2S.right, calibragem2S.bottom);
					
				} else {
					if (calibragemS == null) {
						roupaSuperior.setBounds(0, 0,  100, 100);					
					} else {
						roupaSuperior.setBounds(calibragemS.left, calibragemS.top, calibragemS.right, calibragemS.bottom);					
					}
					
				}
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
				Calibragem2 calibragem2S = calibragens2.get(roupa.getId());
				if(calibragem2S != null) {
					roupaSuperior.setBounds(calibragem2S.left, calibragem2S.top, calibragem2S.right, calibragem2S.bottom);
					
				} else {
					if (calibragemS == null) {
						roupaSuperior.setBounds(0, 0,  100, 100);					
					} else {
						roupaSuperior.setBounds(calibragemS.left, calibragemS.top, calibragemS.right, calibragemS.bottom);					
					}
				}
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
				Calibragem2 calibragem2S = calibragens2.get(roupa.getId());
				if(calibragem2S != null) {
					roupaInferior.setBounds(calibragem2S.left, calibragem2S.top, calibragem2S.right, calibragem2S.bottom);
					
				} else {
					if (calibragemI == null) {
						roupaInferior.setBounds(0, 0,  100, 100);
					} else {
						roupaInferior.setBounds(calibragemI.left, calibragemI.top, calibragemI.right, calibragemI.bottom);
					}
				}
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
				Calibragem2 calibragem2S = calibragens2.get(roupa.getId());
				if(calibragem2S != null) {
					roupaInferior.setBounds(calibragem2S.left, calibragem2S.top, calibragem2S.right, calibragem2S.bottom);
					
				} else {
					if (calibragemI == null) {
						roupaInferior.setBounds(0, 0,  100, 100);
					} else {
						roupaInferior.setBounds(calibragemI.left, calibragemI.top, calibragemI.right, calibragemI.bottom);
						
					}
				}
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
		
		public Roupa getRoupaSuperiorAtual() {
			return roupasSuperiores.get(posicaoRoupaSuperior);
		}
		
		public Roupa getRoupaInferiorAtual() {
			return roupasInferiores.get(posicaoRoupaInferior);
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflate = new MenuInflater(this);
//		inflate.inflate(R.menu.provador_menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.colecoes:
//			startActivity(new Intent(this, LojasActivity.class));
//			Toast.makeText(this, "Falta implementar!", Toast.LENGTH_LONG).show();
//			break;
//		case R.id.opcoes:
//			startActivity(new Intent(this, OpcoesActivity.class));
//			break;
//		}
//		return true;
//	}
	
}