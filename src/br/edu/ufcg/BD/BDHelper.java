package br.edu.ufcg.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper{


	private static final String NOME_DB = "mobileCloset";

	private static final int VERSAO_DB = 1;


	private static final String SQL_CRIA_MANEQUIM = "CREATE TABLE manequim (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "path TEXT NOT NULL);";


	public BDHelper(Context context) {
		super(context, NOME_DB, null, VERSAO_DB);
	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		bd.execSQL(SQL_CRIA_MANEQUIM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
		// TODO
	}

}
