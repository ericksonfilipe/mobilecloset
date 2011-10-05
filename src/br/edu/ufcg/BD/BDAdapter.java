package br.edu.ufcg.BD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class BDAdapter {

	private Context context;
	private BDHelper bdHelper;
	private SQLiteDatabase banco;

	public BDAdapter(Context context) {
		this.context = context;
		this.bdHelper = new BDHelper(context);
		this.banco = bdHelper.getWritableDatabase();
	}

	public void close() {
		bdHelper.close();
	}

	public void inserirManequim(String path) {
		String sql = String.format("INSERT INTO manequim(path) VALUES('%s');", path);
		banco.execSQL(sql);
	}

	public boolean delete(String path) {
		return banco.delete("manequim", "path IS '" + path + "' ", null) > 0;
	}

	public int deleteAllManequins() {
		return banco.delete("manequim", "1", null);
	}


	public Cursor getAllManequins() {
		Log.e("plecas", "tá null? " + banco);
		return banco.query("manequim", 
				null, null, null, null, null, null);
	}

	public int getNumManequins() {
		return getAllManequins().getCount();
	}

	public void salvarImagemMemoriaExterna(byte[] imageData) {

		File path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		String nomeImagem = String.format("manequim%d.jpg", getNumManequins() + 1);
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
