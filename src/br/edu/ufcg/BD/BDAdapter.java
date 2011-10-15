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
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Manequim;
import br.edu.ufcg.model.Roupa;

public class BDAdapter {

	private Context context;
	private BDHelper bdHelper;

	public BDAdapter(Context context) {
		this.context = context;
		this.bdHelper = new BDHelper(context);
	}

	public void inserirManequim(String caminho_imagem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("INSERT INTO manequim(caminho_imagem) VALUES('%s');", caminho_imagem);
		banco.execSQL(sql);
		banco.close();
	}

	public boolean deletarManequim(String caminho_imagem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		boolean b = banco.delete("manequim", "caminho_imagem IS '" + caminho_imagem + "' ", null) > 0;
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
				new String[] {"id", "caminho_imagem"}, null, null, null, null, "id");
		while(c.moveToNext()) {
			manequins.add(new Manequim(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("caminho_imagem"))));
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
	 * Método que recupera o manequim padrão
	 * @return manequim padrão
	 */
	public Manequim getManequimPadrao() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Manequim manequim;
		Cursor c = banco
				.query("manequim_padrao", new String[] { "caminho_manequim" },
						null, null, null, null, null);
		c.moveToFirst();
		manequim = (new Manequim(0,
				c.getString(c.getColumnIndex("caminho_manequim"))));
		c.close();
		banco.close();
		return manequim;
	}

	/**
	 * deleta o antigo manequim padrão e salva o manequim passado por parâmetro como padrão.
	 */
	public void inserirManequimPadrao(Manequim manequim) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL("DELETE FROM manequim_padrao;");
		String sql = String.format("INSERT INTO manequim_padrao VALUES('%s');", manequim.getCaminhoImagem());
		banco.execSQL(sql);
		banco.close();
	}

	public void inserirRoupa(Roupa roupa) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("INSERT INTO roupa(caminho_imagem, categoria) VALUES('%s,%s');", roupa.getCaminhoImagem(), roupa.getCategoria());
		banco.execSQL(sql);
		banco.close();
	}

	public void deleteRoupa(Roupa roupa) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("DELETE FROM roupa WHERE caminho_imagem IS '%s';", roupa.getCaminhoImagem());
		banco.execSQL(sql);
		banco.close();
	}

	public List<Roupa> getAllRoupas() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		List<Roupa> roupas = new ArrayList<Roupa>();
		Cursor c = banco.query("manequim", 
				new String[] {"id", "caminho_imagem"}, null, null, null, null, "id");
		while(c.moveToNext()) {
			String cat = c.getString(c.getColumnIndex("categoria"));
			Categoria categoria = Categoria.valueOf(cat);
			Roupa roupa = new Roupa(c.getString(c.getColumnIndex("caminho_imagem")), categoria);
			roupa.setId(c.getInt(c.getColumnIndex("id")));
			roupas.add(roupa);
		}
		c.close();
		banco.close();
		return roupas;
	}

	//TODO deixar o método abaixo flexível, para que possa salvar qualquer imagem.
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
