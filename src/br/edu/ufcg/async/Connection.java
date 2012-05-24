package br.edu.ufcg.async;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 
 * Classe que oferece serviços de requisição
 * assíncronas ao servidor.
 * 
 * @author Erickson Santos
 * @since Projeto 2
 *
 */
public class Connection {

	private final static String URL = "http://177.71.252.66:80/";

	public static void get(String urlRelativa, AsyncHttpResponseHandler a ) {
    	AsyncHttpClient async = new AsyncHttpClient();
    	async.get(URL + urlRelativa, a);
    }

}
