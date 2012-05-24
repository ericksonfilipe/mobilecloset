package br.edu.ufcg.async;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Roupa;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 
 * Classe responsável por transformar a resposta
 * da requisição de recuperar roupas ao servidor
 * em uma lista de roupas que poderá ser usada
 * pela aplicação. 
 * 
 * @author Erickson Santos
 * @since Projeto 2
 *
 */
public class GetRoupasHandler extends AsyncHttpResponseHandler {

	private static final String CODIGO = "codigo";
	private static final String NOME_LOJA = "nomeloja";
	private static final String CATEGORIA = "categoria";
	private static final String IMAGEM = "imagem";
	private static final String LOGO_LOJA = "logoloja";

	private List<Roupa> roupas;
	private Listener listener;

	public GetRoupasHandler(Listener r) {
		this.listener = r;
	}

	@Override
	public void onSuccess(String response) {
		List<Roupa> roupas = new ArrayList<Roupa>();
		try {
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				roupas.add(recuperaRoupa(item));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.roupas = roupas;
		listener.notifica();
	}

	@Override
	public void onFailure(Throwable t, String arg1) {
		t.printStackTrace();
	}

	private Roupa recuperaRoupa(JSONObject item) throws JSONException {
		byte[] imagem = Base64.decode(item.getString(IMAGEM), Base64.DEFAULT);
		Categoria categoria = Categoria.valueOf(item.getString(CATEGORIA));
		String nomeLoja = item.getString(NOME_LOJA);
		byte[] logoLoja = Base64.decode(item.getString(LOGO_LOJA), Base64.DEFAULT);
		String codigo = item.getString(CODIGO);
		Loja loja = new Loja(0, nomeLoja, logoLoja);
		Roupa roupa = new Roupa(0, imagem, categoria);
		roupa.setLoja(loja);
		roupa.setCodigo(codigo);
		return roupa;
	}

	public List<Roupa> getRoupas() {
		return this.roupas;
	}

}
