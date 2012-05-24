package br.edu.ufcg;


import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.async.Connection;
import br.edu.ufcg.async.GetRoupasHandler;
import br.edu.ufcg.async.Listener;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Roupa;

public class LojasActivity extends ListActivity {
	private GetRoupasHandler handler;
	
	static final String[] LOJAS = new String[] { "Riachuelo", "C&A", "Marisa", "Colcci"};
	
	public class MobileArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
	 
		public MobileArrayAdapter(Context context, String[] values) {
			super(context, R.layout.lojas, values);
			this.context = context;
			this.values = values;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.lojas, parent, false);
			//TextView textView = (TextView) rowView.findViewById(R.id.label);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
			imageView.setBackgroundColor(Color.WHITE);
			textView.setText(values[position]);
	 
			// Change icon based on name
			String s = values[position];
	 
			//System.out.println(s);
	 
			if (s.equals("Riachuelo")) {
				imageView.setImageResource(R.drawable.delete);
			} else if (s.equals("C&A")) {
				imageView.setImageResource(R.drawable.add);
			} else if (s.equals("Marisa")) {
				imageView.setImageResource(R.drawable.save);
			} else {
				imageView.setImageResource(R.drawable.camera);
			}
	 
			return rowView;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*Button botao1 = new Button(this);
		botao1.setText("Marisa");
		botao1.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				handler = new GetRoupasHandler(new RetornoListener());
				Connection.get("roupa/getRoupas/Marisa", handler);				
			}

		});

		Button botao2 = new Button(this);
		botao2.setText("C&A");
		botao2.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				handler = new GetRoupasHandler(new RetornoListener());
				Connection.get("roupa/getRoupas/C&A", handler);				
			}

		});
		
		TextView inicio = new TextView(this);
		inicio.setText("Coleções disponíveis:");
		

		LinearLayout l = new LinearLayout(this);
		l.addView(botao1);
		l.addView(botao2);
		l.addView(inicio);
		addContentView(l, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/
		
		setListAdapter(new MobileArrayAdapter(this, LOJAS));
		
		/*ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    //Toast.makeText(getApplicationContext(),
				//((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(LojasActivity.this, ConfirmarDownload.class);
				//i.putExtra("listener", new RetornoListener()); //TODO
				startActivity(i);
				
			}
		});*/
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
 
		//get selected items
		//TODO
		String selectedValue = (String) getListAdapter().getItem(position);
		System.out.println("selectedValue " + selectedValue);
		if (selectedValue.equals("Riachuelo")){
			handler = new GetRoupasHandler(new RetornoListener());
			Connection.get("roupa/getRoupas/Riachuelo", handler);
		}else if(selectedValue.equals("Marisa")){
			handler = new GetRoupasHandler(new RetornoListener());
			Connection.get("roupa/getRoupas/Marisa", handler);
		}else if(selectedValue.equals("C&A")){
			handler = new GetRoupasHandler(new RetornoListener());
			Connection.get("roupa/getRoupas/C&A", handler);
		}else{
			handler = new GetRoupasHandler(new RetornoListener());
			Connection.get("roupa/getRoupas/Colcci", handler);
		}
		
		Toast.makeText(this, "Coleção baixada com sucesso!", Toast.LENGTH_SHORT).show();
 
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
