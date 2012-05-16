package br.edu.ufcg.BD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import br.edu.ufcg.R;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Calibragem2;
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

	public void removeManequim(Manequim manequim) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL(String.format("DELETE FROM manequim WHERE id = %d;", manequim.getId()));
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
	 * M�todo que recupera o id do manequim padr�o
	 * @return id manequim padr�o, -1 se n�o houver
	 */
	public int getIdManequimPadrao() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Cursor c = banco
				.query("manequim_padrao", new String[] { "id" },
						null, null, null, null, null);
		if (c == null || c.getCount() == 0) { return -1; }
		c.moveToFirst();
		int id = c.getInt(c.getColumnIndex("id"));
		c.close();
		banco.close();
		return id;
	}

	/**
	 * deleta o antigo manequim padr�o e salva o manequim passado por par�metro como padr�o.
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

	public void removeRoupa(Roupa roupa) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL(String.format("DELETE FROM roupa WHERE id = %d;", roupa.getId()));
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

	public Roupa getRoupa(Integer id) {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Cursor c = banco.rawQuery("SELECT r.* FROM roupa r WHERE r.id = " + id + ";", null);
		c.moveToFirst();
		Roupa roupa = new Roupa(c.getInt(c.getColumnIndex("id")), c.getBlob(c.getColumnIndex("imagem")),Categoria.valueOf(c.getString(c.getColumnIndex("categoria"))));
		c.close();
		banco.close();
		return roupa;
		
	}

	//----------------calibragem de categoria -----------------------------------------------------
	
	public Map<Categoria, Calibragem> getCalibragens() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Map<Categoria, Calibragem> calibragens = new HashMap<Categoria, Calibragem>();
		Cursor c = banco.query("calibragem", new String[] {"categoria", "left", "top", "right", "bottom"}, null, null, null, null, null);
		while (c.moveToNext()) {
			String cat = c.getString(c.getColumnIndex("categoria"));
			Categoria categoria = Categoria.valueOf(cat.toUpperCase());
			Calibragem calibragem = new Calibragem(categoria, c.getInt(c.getColumnIndex("left")), c.getInt(c.getColumnIndex("top")),  c.getInt(c.getColumnIndex("right")), c.getInt(c.getColumnIndex("bottom")));
			calibragens.put(categoria, calibragem);
		}
		c.close();
		banco.close();
		return calibragens;
	}

	public void insertCalibragem(Calibragem calibragem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("INSERT INTO calibragem(categoria, left, top, right, bottom) VALUES('%s', %d, %d, %d, %d);",
				calibragem.getCategoria(), calibragem.left, calibragem.top, calibragem.right, calibragem.bottom);
		banco.execSQL(sql);
//		System.out.println(sql);
//		System.out.println("inserindo...: " + calibragem.getCategoria().name());
		banco.close();
	}
	
	public void atualizaCalibragem(Calibragem calibragem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		
		banco.execSQL("DELETE FROM calibragem WHERE categoria = '" + calibragem.getCategoria() + "';");
		String sql = String.format("INSERT INTO calibragem(categoria, left, top, right, bottom) VALUES('%s', %d, %d, %d, %d);",
				calibragem.getCategoria(), calibragem.left, calibragem.top, calibragem.right, calibragem.bottom);
		banco.execSQL(sql);
		banco.close();
	}

	public void deleteCalibragens() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL("DELETE FROM calibragem;");
		banco.close();
	}
	
	//----------------tentativa de calibragem de roupa---------------------------------------------------------------
	
	public Map<Integer, Calibragem2> getCalibragens2() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		Map<Integer, Calibragem2> calibragens = new HashMap<Integer, Calibragem2>();
		Cursor c = banco.query("calibragem2", new String[] {"id", "roupa", "left", "top", "right", "bottom"}, null, null, null, null, null);
		while (c.moveToNext()) {
			int idRoupa = c.getInt(c.getColumnIndex("roupa"));
			
			Calibragem2 calibragem = new Calibragem2(idRoupa, c.getInt(c.getColumnIndex("left")), c.getInt(c.getColumnIndex("top")),  c.getInt(c.getColumnIndex("right")), c.getInt(c.getColumnIndex("bottom")));
			calibragem.setId(c.getInt(c.getColumnIndex("id")));
			calibragens.put(idRoupa, calibragem);
		}
		c.close();
		banco.close();
		return calibragens;
	}

	public void insertCalibragem2(Calibragem2 calibragem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		String sql = String.format("INSERT INTO calibragem2(roupa, left, top, right, bottom) VALUES(%d, %d, %d, %d, %d);",
				calibragem.getRoupa(), calibragem.left, calibragem.top, calibragem.right, calibragem.bottom);
		banco.execSQL(sql);
		banco.close();
	}
	
	public void atualizaCalibragem2(Calibragem2 calibragem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		
		banco.execSQL("DELETE FROM calibragem2 WHERE roupa = " + calibragem.getRoupa() + ";");
		String sql = String.format("INSERT INTO calibragem2(roupa, left, top, right, bottom) VALUES(%d, %d, %d, %d, %d);",
				calibragem.getRoupa(), calibragem.left, calibragem.top, calibragem.right, calibragem.bottom);
		banco.execSQL(sql);
		banco.close();
	}

	public void deleteCalibragens2() {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL("DELETE FROM calibragem2;");
		banco.close();
	}
	
	
	
	
	//----------------------------------------------------------------------------------------------------------------

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
