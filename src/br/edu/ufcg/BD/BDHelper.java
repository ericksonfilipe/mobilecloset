package br.edu.ufcg.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper {

	private static final String NOME_DB = "mobileCloset";
	private static final int VERSAO_DB = 13;
	private final String SQL_CRIA_MANEQUIM = "CREATE TABLE manequim (id INTEGER PRIMARY KEY AUTOINCREMENT, imagem BLOB NOT NULL);";
	private final String SQL_CRIA_MANEQUIM_PADRAO = "CREATE TABLE manequim_padrao (id INTEGER PRIMARY KEY);";
	private final String SQL_CRIA_ROUPA	= "CREATE TABLE roupa (id INTEGER PRIMARY KEY AUTOINCREMENT, imagem BLOB NOT NULL, categoria TEXT NOT NULL);";
	private final String SQL_CALIBRAGEM	= "CREATE TABLE calibragem (id INTEGER PRIMARY KEY AUTOINCREMENT, categoria TEXT NOT NULL, left INTEGER NOT NULL, top INTEGER NOT NULL, right INTEGER NOT NULL, bottom INTEGER NOT NULL);";

	public BDHelper(Context context) {
		super(context, NOME_DB, null, VERSAO_DB);
	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		bd.execSQL(SQL_CRIA_MANEQUIM);
		bd.execSQL(SQL_CRIA_MANEQUIM_PADRAO);
		bd.execSQL(SQL_CRIA_ROUPA);
		bd.execSQL(SQL_CALIBRAGEM);
		bd.execSQL("CREATE TABLE plecas (id INTEGER PRIMARY KEY AUTOINCREMENT, imagem BLOB NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
		bd.execSQL("DROP TABLE IF EXISTS manequim_padrao;");
		bd.execSQL("DROP TABLE IF EXISTS manequim;");
		bd.execSQL("DROP TABLE IF EXISTS roupa;");
		bd.execSQL("DROP TABLE IF EXISTS calibragem;");
		bd.execSQL("DROP TABLE IF EXISTS plecas;");
		this.onCreate(bd);
	}
}
