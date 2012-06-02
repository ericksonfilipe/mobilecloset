package br.edu.ufcg.model;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Centralizando texto interno do Toast para melhorar textos com mais de uma linha.
 */
public class ToastComTextoCentralizado {

	private Toast toast;
	private TextView texto;

	public ToastComTextoCentralizado(Context contexto, int duracao, String msg) {
		
		texto = new TextView(contexto);
		texto.setText(msg);
		texto.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
		
		toast = new Toast(contexto);
		toast.setDuration(duracao);
		toast.setView(texto);
		
	}

	public void show() {
		toast.show();
	}
	

}
