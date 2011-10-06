package br.edu.ufcg.BD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import br.edu.ufcg.model.Manequim;

public class BDAdapter {

	private Context context;
	private BDHelper bdHelper;

	public BDAdapter(Context context) {
		this.context = context;
		this.bdHelper = new BDHelper(context);
	}

	public void close() {
		bdHelper.close();
	}

	public void inserirManequim(String path) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("INSERT INTO manequim(path) VALUES('%s');", path);
		banco.execSQL(sql);
		banco.close();
	}

	public boolean deletarManequim(String path) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		boolean b = banco.delete("manequim", "path IS '" + path + "' ", null) > 0;
		banco.close();
		return b;
	}

	/**
	 * deleta todos os manequins do banco de dados.
	 */
	public void deleteAllManequins() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.delete("manequim", "1", null);
		banco.close();
	}

	/**
	 * Retorna todos os manequins cadastrados, ordenados por id.
	 * @return List<Manequim>
	 */
	public List<Manequim> getAllManequins() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		List<Manequim> manequins = new ArrayList<Manequim>();
		Cursor c = banco.query("manequim", 
				new String[] {"id", "path"}, null, null, null, null, "id");
		while(c.moveToNext()) {
			manequins.add(new Manequim(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("path"))));
		}
		c.close();
		banco.close();
		return manequins;
	}

	/**
	 * Retorna a quantidade de manequins cadastrados
	 */
	public int getNumManequins() {
		return getAllManequins().size();
	}

	/**
	 * Método que recupera o maior id da tabela de manequins.
	 * Utilizado pelo método salvarImagemMemoriaExterna.
	 * @return o inteiro representando o maior indice da tabela de manequins.
	 */
	public int getNextIndexManequim() {
		List<Manequim> manequins = getAllManequins();
		if (manequins.isEmpty()) {
			return 0;
		}
		return manequins.get(manequins.size()-1).getId();
	}

	/**
	 * Método responsável por salvar imagens no cartão de memória.
	 * @param imageData
	 */
	public void salvarImagemMemoriaExterna(byte[] imageData) {

		File path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		String nomeImagem = String.format("manequim%d.jpg", getNextIndexManequim() + 1);
		File file = new File (path, nomeImagem);

		try {
			//InputStream is = this.context.getResources().openRawResource(R.drawable.botao_provar);
			OutputStream os = new FileOutputStream(file);
			byte[] data = imageData;
			//is.read(data);
			os.write(data);
			//is.close();
			os.close();

			// verifica se ele foi criado com sucesso
			MediaScannerConnection.scanFile(this.context, new String[] { file.toString() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
				public void onScanCompleted(String path, Uri uri) {
					Log.i("ExternalStorage", "Scanned " + path + ":");
					Log.i("ExternalStorage", "-> uri=" + uri);
				}
			});
		} catch (IOException e) {
			Log.w("ExternalStorage", "Error writing " + file, e);
		}
		inserirManequim(nomeImagem);
	}

}
