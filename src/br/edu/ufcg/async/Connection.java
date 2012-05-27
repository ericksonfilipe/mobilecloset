package br.edu.ufcg.async;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 
 * Classe que oferece servi�os de requisi��o
 * ass�ncronas ao servidor.
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

	public static InputStream getStreamFor(String urlRelativa) {
		try {
			return new URL(URL + urlRelativa).openConnection().getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
