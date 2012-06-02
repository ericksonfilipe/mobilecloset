package br.edu.ufcg;


import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.async.Connection;
import br.edu.ufcg.async.DecodeJson;
import br.edu.ufcg.async.dto.RoupaDTO;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Roupa;

public class LojasActivity extends ListActivity {

	private static final String GET_ROUPAS = "roupa/getRoupas/";

	private BDAdapter dao;
	private List<Loja> lojas;

	public class MobileArrayAdapter extends ArrayAdapter<Loja> {
		private final Context context;
		private final List<Loja> values;

		public MobileArrayAdapter(Context context, List<Loja> values) {
			super(context, R.layout.lojas, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.lojas, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
			imageView.setBackgroundColor(Color.WHITE);
			textView.setText(values.get(position).getNome());

			// exibe o logo da loja
			byte[] imagem = values.get(position).getLogo();
			Bitmap logo = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			imageView.setImageBitmap(logo);

			return rowView;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new BDAdapter(LojasActivity.this);

		InputStream is = Connection.getStreamFor("loja/getLojas");
		String response = new Scanner(is).useDelimiter("\\A").next();
		lojas = DecodeJson.decode(Loja.class, response);
		for (Loja loja : lojas) {
			dao.inserirLoja(loja);
		}

		setListAdapter(new MobileArrayAdapter(this, lojas));

		Toast.makeText(this, "Selecione a loja e aguarde o download das roupas.", Toast.LENGTH_LONG).show();
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Loja lojaSelecionada = (Loja) getListAdapter().getItem(position);
		InputStream is = Connection.getStreamFor(GET_ROUPAS + processaNome(lojaSelecionada));
		String response = new Scanner(is).useDelimiter("\\A").next();
		List<RoupaDTO> roupas = DecodeJson.decode(RoupaDTO.class, response);
		Loja loja = dao.getLoja(lojaSelecionada.getNome());
		
		int qtdDeRoupas = 0;
		for (RoupaDTO r : roupas) {
			Roupa roupa = new Roupa(r);
			roupa.setLoja(loja);
			dao.inserirRoupa(roupa);
			qtdDeRoupas++;
		}
		System.out.println(roupas);

		Toast.makeText(this, "Download da Coleção concluído!\n("+qtdDeRoupas+" peças da loja"+lojaSelecionada.getNome()+")", Toast.LENGTH_LONG).show();
	}

	private String processaNome(Loja l) {
		return l.getNome().replace(" ", "%20");
	}

}
