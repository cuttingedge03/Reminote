package com.nadeem.todos.utils;

import java.util.ArrayList;
import java.util.List;

import com.nadeem.todos.db.TodoSQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoDAO {

	private SQLiteDatabase db;
	private TodoSQLiteHelper dbHelper;
	Context context1;

	public TodoDAO(Context context) {
		dbHelper = new TodoSQLiteHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	public void createTodo(String todoText, String title) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("todo", todoText);
		contentValues.put("title", title);

		db.insert("todos", null, contentValues);
	}

	public void deleteTodo(int todoId) {

		db.delete("todos", "_id = " + todoId, null);
	}

	public List<Todo> getTodos() {

		List<Todo> todoList = new ArrayList<Todo>();

		String[] tableColumns = new String[] { "_id", "title", "todo", "time" };

		Cursor cursor = db.query("todos", tableColumns, null, null, null, null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Todo todo = new Todo();

			todo.setId(cursor.getInt(0));
			todo.setText(cursor.getString(1), cursor.getString(2));
			todo.setTime(cursor.getString(3));
			todoList.add(todo);
			cursor.moveToNext();
		}
		return todoList;
	}

}
