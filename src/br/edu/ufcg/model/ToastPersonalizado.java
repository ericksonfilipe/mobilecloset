package br.edu.ufcg.model;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.R;

/**
 * Centralizando texto interno do Toast para melhorar textos com mais de uma linha.
 * Personalizando Toast com arquivo toast.xml
 */
public class ToastPersonalizado {

	private Toast toast;
	private TextView texto;

	public ToastPersonalizado(Context contexto, int duracao, String msg) {
		
		texto = new TextView(contexto);
		texto.setText(msg);
		texto.setTextColor(Color.WHITE);
		texto.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
		texto.setBackgroundDrawable(contexto.getResources().getDrawable(R.drawable.toast));
		
		toast = new Toast(contexto);
		toast.setDuration(duracao);
		toast.setView(texto);
		
	}

	public void show() {
		toast.show();
	}
	

}
