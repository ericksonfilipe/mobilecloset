package br.edu.ufcg.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper {

	private static final String NOME_DB = "mobileCloset";
	private static final int VERSAO_DB = 38;
	private final String SQL_CRIA_MANEQUIM = "CREATE TABLE manequim (id INTEGER PRIMARY KEY AUTOINCREMENT, imagem BLOB NOT NULL);";
	private final String SQL_CRIA_MANEQUIM_PADRAO = "CREATE TABLE manequim_padrao (id INTEGER PRIMARY KEY);";
	private final String SQL_CRIA_ROUPA	= "CREATE TABLE roupa (id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT UNIQUE, imagem BLOB NOT NULL, categoria TEXT NOT NULL, loja INTEGER NULL);";
	private final String SQL_CRIA_LOJA	= "CREATE TABLE loja (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT UNIQUE NOT NULL, logo BLOB NOT NULL);";
	private final String SQL_CALIBRAGEM	= "CREATE TABLE calibragem (id INTEGER PRIMARY KEY AUTOINCREMENT, categoria TEXT NOT NULL, left INTEGER NOT NULL, top INTEGER NOT NULL, right INTEGER NOT NULL, bottom INTEGER NOT NULL);";
	private final String SQL_CALIBRAGEM2 = "CREATE TABLE calibragem2 (id INTEGER PRIMARY KEY AUTOINCREMENT, roupa INTEGER NOT NULL, left INTEGER NOT NULL, top INTEGER NOT NULL, right INTEGER NOT NULL, bottom INTEGER NOT NULL);";
	private final String SQL_CRIA_LOOK = "CREATE TABLE look (id INTEGER PRIMARY KEY AUTOINCREMENT, imagem BLOB NOT NULL, logoLojaSuperior BLOB, nomeLojaSuperior TEXT, categoriaRoupaSuperior TEXT, codigoRoupaSuperior TEXT, logoLojaInferior BLOB,  nomeLojaInferior TEXT, categoriaRoupaInferior TEXT, codigoRoupaInferior TEXT);";
	
	public BDHelper(Context context) {
		super(context, NOME_DB, null, VERSAO_DB);
	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		bd.execSQL(SQL_CRIA_MANEQUIM);
		bd.execSQL(SQL_CRIA_MANEQUIM_PADRAO);
		bd.execSQL(SQL_CRIA_LOJA);
		bd.execSQL(SQL_CRIA_ROUPA);
		bd.execSQL(SQL_CALIBRAGEM);
		bd.execSQL(SQL_CALIBRAGEM2);
		bd.execSQL(SQL_CRIA_LOOK);
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
		bd.execSQL("DROP TABLE IF EXISTS manequim_padrao;");
		bd.execSQL("DROP TABLE IF EXISTS manequim;");
		bd.execSQL("DROP TABLE IF EXISTS roupa;");
		bd.execSQL("DROP TABLE IF EXISTS calibragem;");
		bd.execSQL("DROP TABLE IF EXISTS calibragem2;");
		bd.execSQL("DROP TABLE IF EXISTS look;");
		bd.execSQL("DROP TABLE IF EXISTS loja;");
		this.onCreate(bd);
	}
}
