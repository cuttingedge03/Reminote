package com.nadeem.todos;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;


public class AddTodoActivity extends SherlockActivity implements
		OnClickListener {

	private EditText todoText; //
	private Button addNewButton;
	private EditText title;
	private SQLiteDatabase db;
	private TodoSQLiteHelper dbHelper;
	Context context;
	private TodoDAO dao;
	int idvalue20, idvalue10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_todo);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		dao = new TodoDAO(this);
		title = (EditText) findViewById(R.id.Text1);
		todoText = (EditText) findViewById(R.id.newTodoText);
		addNewButton = (Button) findViewById(R.id.Add);

		addNewButton.setOnClickListener(this);
		Bundle extras = getIntent().getExtras();
		String idvalue = extras.getString("idd1");
		int idvalue1 = extras.getInt("idd2");
		int idvalue4 = extras.getInt("idd5");
		int idvalue2 = extras.getInt("idd3");
		String idvalue3 = extras.getString("idd4");
		if (idvalue2 == 1) {
			title.setText(idvalue3);
			todoText.setText(idvalue);
		}
		abc(idvalue1, idvalue2);
	}

	public void abc(int c, int d) {
		idvalue10 = c;
		idvalue20 = d;
	}

	@Override
	public void onClick(View v) {
		if (idvalue20 == 1)

		{
			if (addNewButton.isPressed()) {
				int b = emptycheck();
				if (b == 0) {
					String todoTextValue = todoText.getText().toString();

					String todotitle = title.getText().toString();
					todoText.setText("");
					title.setText("");
					dbHelper = new TodoSQLiteHelper(this);
					db = dbHelper.getWritableDatabase();
					ContentValues contentValues = new ContentValues();
					contentValues.put("todo", todoTextValue);
					contentValues.put("title", todotitle);
					db.update("todos", contentValues, "_id " + "=" + idvalue10,
							null);
					Toast.makeText(getApplicationContext(), "TODO updated!",
							Toast.LENGTH_SHORT).show();
					db.close();
					Intent intent = new Intent(this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					this.finish();
				} else
					Toast.makeText(getApplicationContext(), "Field is empty!",
							Toast.LENGTH_SHORT).show();
			}
		}

		else {
			if (addNewButton.isPressed()) {
				int a = emptycheck();
				if (a == 0) {
					String todoTextValue = todoText.getText().toString();
					String todotitle = title.getText().toString();
					todoText.setText("");
					title.setText("");

					dao.createTodo(todoTextValue, todotitle);

					Toast.makeText(getApplicationContext(),
							"New TODO added!!!", Toast.LENGTH_SHORT).show();

				} else
					Toast.makeText(getApplicationContext(), "Field is empty!",
							Toast.LENGTH_SHORT).show();
			}
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		this.finish();
	}

	public int emptycheck() {
		String todoTextValue = todoText.getText().toString();
		String todotitle = title.getText().toString();
		if (todoTextValue.trim().equals("") || todotitle.trim().equals("")) {
			return 1;
		} else
			return 0;

	}
}