package br.edu.ufcg;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.async.Connection;
import br.edu.ufcg.async.GetRoupasHandler;
import br.edu.ufcg.async.Listener;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Roupa;

public class LojasActivity extends Activity {
	private GetRoupasHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button requisicao = new Button(this);
		requisicao.setText("Marisa");
		requisicao.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				handler = new GetRoupasHandler(new RetornoListener());
				Connection.get("roupa/getRoupas/Marisa", handler);				
			}

		});

		Button loja = new Button(this);
		loja.setText("C&A");
		loja.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				handler = new GetRoupasHandler(new RetornoListener());
				Connection.get("roupa/getRoupas/C&A", handler);				
			}

		});

		LinearLayout l = new LinearLayout(this);
		l.addView(requisicao);
		l.addView(loja);
		addContentView(l, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	public class RetornoListener implements Listener {

		public void notifica() {
			List<Roupa> roupas = handler.getRoupas();

			BDAdapter dao = new BDAdapter(LojasActivity.this);

			if (roupas != null && !roupas.isEmpty()) {
				Loja l = roupas.get(0).getLoja();
				Loja loja = dao.getLoja(l.getNome());
				if (loja == null) {
					dao.inserirLoja(l);
				}
				loja = dao.getLoja(l.getNome());
				for (Roupa roupa : roupas) {
					roupa.setLoja(loja);
					dao.inserirRoupa(roupa);
				}
				System.out.println(dao.getRoupas());

			}
		}

	}
}
