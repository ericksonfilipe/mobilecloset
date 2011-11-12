package br.edu.ufcg.BD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

	public void inserirManequim(byte[] imagem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("imagem", imagem);
		banco.insert("manequim", null, cv);
		banco.close();
	}

	/**
	 * Retorna todos os manequins cadastrados, ordenados por id.
	 * @return List<Manequim>
	 */
	public List<Manequim> getManequins() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		List<Manequim> manequins = new ArrayList<Manequim>();
		Cursor c = banco.query("manequim", 
				new String[] {"id", "imagem"}, null, null, null, null, "id");
		while(c.moveToNext()) {
			manequins.add(new Manequim(c.getInt(c.getColumnIndex("id")), c.getBlob(c.getColumnIndex("imagem"))));
		}
		c.close();
		banco.close();
		return manequins;
	}
	
	/**
	 * Método que recupera o id do manequim padrão
	 * @return manequim padrão
	 */
	public int getIdManequimPadrao() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Cursor c = banco
				.query("manequim_padrao", new String[] { "id" },
						null, null, null, null, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("id"));
	}

	/**
	 * deleta o antigo manequim padrão e salva o manequim passado por parâmetro como padrão.
	 */
	public void inserirManequimPadrao(Manequim manequim) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL("DELETE FROM manequim_padrao;");
		String sql = String.format("INSERT INTO manequim_padrao VALUES('%d');", manequim.getId());
		banco.execSQL(sql);
		banco.close();
	}

	public void inserirRoupa(Roupa roupa) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("imagem", roupa.getImagem());
		cv.put("categoria", roupa.getCategoria().name());
		banco.insert("roupa", null, cv);
		banco.close();
	}

	public List<Roupa> getRoupas() {
		return getRoupas(null);
	}
	
	public List<Roupa> getRoupas(Categoria categoria) {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		List<Roupa> roupas = new ArrayList<Roupa>();
		
		Cursor c;
		
		if (categoria == null) {
			c = banco.query("roupa", 
                    new String[] {"id", "imagem", "categoria"}, null, null, null, null, "id");
		} else {
			c = banco.rawQuery("SELECT r.* FROM roupa r WHERE r.categoria LIKE '%"+categoria.getNome()+"%'", null); //isso funciona?
		}
		while (c.moveToNext()) {
			Roupa roupa = new Roupa(c.getInt(c.getColumnIndex("id")), c.getBlob(c.getColumnIndex("imagem")),Categoria.valueOf(c.getString(c.getColumnIndex("categoria"))));
			roupas.add(roupa);
		}
		c.close();
		banco.close();
		return roupas;
	}	

	public Map<Categoria, Calibragem> getCalibragens() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Map<Categoria, Calibragem> calibragens = new HashMap<Categoria, Calibragem>();
		Cursor c = banco.query("calibragem", new String[] {"categoria", "left", "top", "right", "bottom"}, null, null, null, null, null);
		while (c.moveToNext()) {
			String cat = c.getString(c.getColumnIndex("categoria"));
			Categoria categoria = Categoria.valueOf(cat);
			Calibragem calibragem = new Calibragem(categoria, c.getInt(c.getColumnIndex("left")), c.getInt(c.getColumnIndex("top")),  c.getInt(c.getColumnIndex("right")), c.getInt(c.getColumnIndex("bottom")));
			calibragens.put(categoria, calibragem);
		}
		return calibragens;
	}

	public void insertCalibragem(Calibragem calibragem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("INSERT INTO calibragem(categoria, left, top, right, bottom) VALUES('%s', %d, %d, %d, %d);",
				calibragem.getCategoria().name(), calibragem.left, calibragem.top, calibragem.right, calibragem.bottom);
		banco.execSQL(sql);
		banco.close();
	}
	
	public void inserePlecas(byte[] imagem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("imagem", imagem);
		banco.insert("plecas", null, cv);
//		banco.execSQL("INSERT INTO plecas(imagem) VALUES(\"" + imagem + "\");");
		banco.close();
	}
	
	public byte[] getPlecas() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Cursor c = banco.query("plecas", new String[] {"imagem"}, null, null, null, null, null);
		c.moveToFirst();
		byte[] r = c.getBlob(c.getColumnIndex("imagem"));
		c.close();
		banco.close();
		return r;
		
	}

	public void deleteCalibragens() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL("DELETE FROM calibragem;");
	}

	public byte[] getManequimPadrao() {
		int id = getIdManequimPadrao();
		for (Manequim manequim : getManequins()) {
			if (manequim.getId() == id) {
				return manequim.getImagem();
			}
		}
		return null;
	}

}
