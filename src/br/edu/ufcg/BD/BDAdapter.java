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
import br.edu.ufcg.model.Calibragem2;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Loja;
import br.edu.ufcg.model.Look;
import br.edu.ufcg.model.Manequim;
import br.edu.ufcg.model.Roupa;


public class BDAdapter {

	private Context context;
	private BDHelper bdHelper;

	public BDAdapter(Context context) {
		this.context = context;
		this.bdHelper = new BDHelper(context);
		
	}

	public void inserirLoja(Loja loja) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("nome", loja.getNome());
		cv.put("logo", loja.getLogo());
		banco.insert("loja", null, cv);
		banco.close();
	}

	public Loja getLoja(String nome) {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Cursor c = banco.rawQuery(String.format("SELECT l.* FROM loja l WHERE l.nome = '%s';", nome), null);
		if (c == null || c.getCount() == 0) {
			return null;
		}
		c.moveToFirst();
		int id = c.getInt(c.getColumnIndex("id"));
		String nomeL = c.getString(c.getColumnIndex("nome"));
		byte[] logo = c.getBlob(c.getColumnIndex("logo"));
		c.close();
		banco.close();
		return new Loja(id, nomeL, logo);
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
	
	public void inserirLook(byte[] imagem) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("imagem", imagem);
		banco.insert("look", null, cv);
		banco.close();
	}

	public void removeLook(Look look) {
		SQLiteDatabase banco = bdHelper.getWritableDatabase();
		banco.execSQL(String.format("DELETE FROM look WHERE id = %d;", look.getId()));
		banco.close();
	}

	/**
	 * Retorna todos os manequins cadastrados, ordenados por id.
	 * @return List<Manequim>
	 */
	public List<Look> getLooks() {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		List<Look> looks = new ArrayList<Look>();
		Cursor c = banco.query("look", 
				new String[] {"id", "imagem"}, null, null, null, null, "id");
		while(c.moveToNext()) {
			looks.add(new Look(c.getInt(c.getColumnIndex("id")), c.getBlob(c.getColumnIndex("imagem"))));
		}
		c.close();
		banco.close();
		return looks;
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
		cv.put("codigo", roupa.getCodigo());
		if (roupa.getLoja() != null) {
			cv.put("loja", roupa.getLoja().getId());
		}
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
                    new String[] {"id"}, null, null, null, null, "id");
		} else {
			c = banco.rawQuery("SELECT r.id FROM roupa r WHERE r.categoria LIKE '%"+categoria.getNome()+"%'", null); //isso funciona?
		}
		List<Integer> ids = new ArrayList<Integer>();
		while (c.moveToNext()) {
			ids.add(c.getInt(c.getColumnIndex("id")));
		}
		c.close();
		banco.close();
		
		for (Integer id : ids) {
			roupas.add(getRoupa(id));
		}
		return roupas;
	}

	public Roupa getRoupa(Integer id) {
		SQLiteDatabase banco = bdHelper.getReadableDatabase();
		Cursor c = banco.rawQuery("SELECT r.* FROM roupa r WHERE r.id = " + id + ";", null);
		c.moveToFirst();
		Roupa roupa = new Roupa(c.getInt(c.getColumnIndex("id")), c.getBlob(c.getColumnIndex("imagem")),Categoria.valueOf(c.getString(c.getColumnIndex("categoria"))));
		roupa.setCodigo(c.getString(c.getColumnIndex("codigo")));
		int cdLoja = c.getInt(c.getColumnIndex("loja")); 
		c.close();
		
		Cursor c2 = banco.rawQuery("SELECT l.* FROM loja l WHERE l.id = " + cdLoja + ";", null);
		c2.moveToFirst();
		if (c2.getCount() != 0) {
			int idL = c2.getInt(c2.getColumnIndex("id"));
			String nomeL = c2.getString(c2.getColumnIndex("nome"));
			byte[] logo = c2.getBlob(c2.getColumnIndex("logo"));
			Loja loja = new Loja(idL, nomeL, logo);
			roupa.setLoja(loja);
		}
		c2.close();
		
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
