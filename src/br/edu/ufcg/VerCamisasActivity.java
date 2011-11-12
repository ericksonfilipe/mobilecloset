package br.edu.ufcg;

import java.io.Serializable;

import br.edu.ufcg.model.Categoria;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VerCamisasActivity extends Activity implements OnClickListener {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_camisas);
        
		Button bCamisasMangaComprida = (Button) findViewById(R.id.button_camisas_manga_comprida);
		bCamisasMangaComprida.setOnClickListener(this);

		Button bCamisasMangaCurta = (Button) findViewById(R.id.button_camisas_manga_curta);
		bCamisasMangaCurta.setOnClickListener(this);
		
		Button bCamisasSemManga = (Button) findViewById(R.id.button_camisas_sem_manga);
		bCamisasSemManga.setOnClickListener(this);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
    }

	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.button_camisas_manga_comprida:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.CAMISA_MANGA_LONGA);
			startActivity(i);
			break;
		case R.id.button_camisas_manga_curta:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.CAMISA);
			startActivity(i);
			break;
		case R.id.button_camisas_sem_manga:
			i = new Intent(v.getContext(), VisualizacaoDeRoupas.class);
			i.putExtra("categoria", (Serializable) Categoria.CAMISETA);
			startActivity(i);
			break;
		}
		
		
	}

}
