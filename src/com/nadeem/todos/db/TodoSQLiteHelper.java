package com.nadeem.todos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoSQLiteHelper extends SQLiteOpenHelper {

	public TodoSQLiteHelper(Context context) {
		
		super(context, "todos_db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL,todo TEXT NOT NULL,time TEXT);");
		
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		
		db.execSQL("DROP TABLE IF EXISTS todos");
		
		onCreate(db);
	}
	
}
