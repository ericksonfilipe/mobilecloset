package br.edu.ufcg.BD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import br.edu.ufcg.model.Calibragem;
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
	public void deleteManequins() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.delete("manequim", "1", null);
		banco.close();
	}

	/**
	 * Retorna todos os manequins cadastrados, ordenados por id.
	 * @return List<Manequim>
	 */
	public List<Manequim> getManequins() {
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
		return getManequins().size();
	}

	/**
	 * Método que recupera o maior id da tabela de manequins.
	 * Utilizado pelo método salvarManequimMemoriaExterna.
	 * @return o inteiro representando o maior indice da tabela de manequins.
	 */
	public int getNextIndexManequim() {
		List<Manequim> manequins = getManequins();
		if (manequins.isEmpty()) {
			return 0;
		}
		return manequins.get(manequins.size()-1).getId();
	}
	
	/**
	 * Método que recupera o maior id da tabela de roupas.
	 * Utilizado pelo método salvarRoupaMemoriaExterna.
	 * @return o inteiro representando o maior indice da tabela de manequins.
	 */
	public int getNextIndexRoupa() {
		List<Roupa> roupas = getRoupas();
		if (roupas.isEmpty()) {
			return 0;
		}
		return roupas.get(roupas.size()-1).getId();
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
		String sql = String.format("INSERT INTO roupa(caminho_imagem, categoria) VALUES('%s','%s');", roupa.getCaminhoImagem(), roupa.getCategoria().name());
		banco.execSQL(sql);
		banco.close();
	}

	public void deleteRoupa(Roupa roupa) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("DELETE FROM roupa WHERE caminho_imagem IS '%s';", roupa.getCaminhoImagem());
		banco.execSQL(sql);
		banco.close();
	}

	public List<Roupa> getRoupas() {
		return getRoupas(null);
	}
	
	public List<Roupa> getRoupas(Categoria categoria) {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		List<Roupa> roupas = new ArrayList<Roupa>();
		Cursor c = banco.query("manequim", 
				new String[] {"id", "caminho_imagem"}, null, null, null, null, "id");
		while (c.moveToNext()) {
			if (categoria == null) {
				String cat = c.getString(c.getColumnIndex("categoria"));
				categoria = Categoria.valueOf(cat);
			}
			Roupa roupa = new Roupa(c.getString(c.getColumnIndex("caminho_imagem")), categoria);
			roupa.setId(c.getInt(c.getColumnIndex("id")));
			roupas.add(roupa);
		}
		c.close();
		banco.close();
		return roupas;
	}	

	public Map<Categoria, Calibragem> getCalibragens() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Map<Categoria, Calibragem> calibragens = new HashMap<Categoria, Calibragem>();
		Cursor c = banco.query("calibragem", new String[] {"categoria", "pos_x", "pos_y", "altura", "largura"}, null, null, null, null, null);
		while (c.moveToNext()) {
			String cat = c.getString(c.getColumnIndex("categoria"));
			Categoria categoria = Categoria.valueOf(cat);
			Calibragem calibragem = new Calibragem(categoria, c.getDouble(c.getColumnIndex("pos_x")), c.getDouble(c.getColumnIndex("pos_y")), c.getDouble(c.getColumnIndex("altura")), c.getDouble(c.getColumnIndex("largura")));
			calibragens.put(categoria, calibragem);
		}
		return calibragens;
	}

	public void insertCalibragem(Calibragem calibragem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("INSERT INTO calibragem(categoria, pos_x, pos_y, altura, largura) VALUES('%s', ", calibragem.getCategoria().name())
		+ calibragem.getPosicaoX() + ", " + calibragem.getPosicaoY() + ", " + calibragem.getAltura() + ", " + calibragem.getLargura() + ");";
		banco.execSQL(sql);
	}

	public void deleteCalibragens() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL("DELETE FROM calibragem;");
	}

	//TODO deixar o método abaixo flexível, para que possa salvar qualquer imagem.
	/**
	 * Método responsável por salvar imagens no cartão de memória.
	 * @param imageData
	 */
	public void salvarManequimMemoriaExterna(byte[] imageData) {

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
	
	//TODO deixar o método abaixo flexível, para que possa salvar qualquer imagem.
	/**
	 * Método responsável por salvar imagens no cartão de memória.
	 * @param imageData
	 */
	public void salvarRoupaMemoriaExterna(byte[] imageData, Categoria categoria) {

		File path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		String nomeImagem = String.format("roupa%d.jpg", getNextIndexRoupa() + 1);
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

		Roupa roupa = new Roupa(nomeImagem, categoria);
		inserirRoupa(roupa);
	}

}
