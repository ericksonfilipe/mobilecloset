package br.edu.ufcg.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper{


	private static final String NOME_DB = "mobileCloset";

	private static final int VERSAO_DB = 3;

	private final String SQL_CRIA_MANEQUIM = "CREATE TABLE manequim (id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "path TEXT NOT NULL);";
	private final String SQL_CRIA_MANEQUIM_PADRAO = "CREATE TABLE manequim_padrao (caminho_manequim TEXT PRIMARY KEY);";


	public BDHelper(Context context) {
		super(context, NOME_DB, null, VERSAO_DB);
	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		bd.execSQL(SQL_CRIA_MANEQUIM);
		bd.execSQL(SQL_CRIA_MANEQUIM_PADRAO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
		bd.execSQL("DROP TABLE IF EXISTS manequim_padrao;");
		bd.execSQL("DROP TABLE IF EXISTS manequim;");
		this.onCreate(bd);
	}
}
