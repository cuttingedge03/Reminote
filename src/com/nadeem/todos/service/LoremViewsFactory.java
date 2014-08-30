package com.nadeem.todos.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nadeem.todos.R;
import com.nadeem.todos.db.TodoSQLiteHelper;
import com.nadeem.todos.provider.WidgetProvider;

public class LoremViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private TodoSQLiteHelper dbHelper;
	Cursor cursor;
	private Context ctxt = null;
	private int appWidgetId;

	public LoremViewsFactory(Context ctxt, Intent intent) {
		this.ctxt = ctxt;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	@Override
	public void onCreate() {
		System.out.println("create");
		dbHelper = new TodoSQLiteHelper(ctxt); // no-op
		String[] tableColumns = new String[] { "_id", "title", "todo" };
		cursor = dbHelper.getReadableDatabase().query("todos", tableColumns,
				null, null, null, null, null);
	}

	@Override
	public void onDestroy() {
		dbHelper.close(); // no-op
	}

	@Override
	public int getCount() {
		return (cursor.getCount());
	}

	@Override
	public RemoteViews getViewAt(int position) {
		System.out.println("getview");
		RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.row);
		cursor.moveToPosition(position);
		row.setTextViewText(R.id.title,
				cursor.getString(cursor.getColumnIndex("title")));
		row.setTextViewText(R.id.description,
				cursor.getString(cursor.getColumnIndex("todo")));

		Intent i = new Intent();
		Bundle extras = new Bundle();

		extras.putString(WidgetProvider.EXTRA_TITLE,
				cursor.getString(cursor.getColumnIndex("title")));
		extras.putString(WidgetProvider.EXTRA_DESCRIPTION,
				cursor.getString(cursor.getColumnIndex("todo")));
		extras.putInt(WidgetProvider.EXTRA_ID,
				cursor.getInt(cursor.getColumnIndex("_id")));
		i.putExtras(extras);
		row.setOnClickFillInIntent(R.id.row_layout, i);

		return (row);
	}

	@Override
	public RemoteViews getLoadingView() {
		return (null);
	}

	@Override
	public int getViewTypeCount() {
		return (1);
	}

	@Override
	public long getItemId(int position) {
		return (position);
	}

	@Override
	public boolean hasStableIds() {
		return (true);
	}

	@Override
	public void onDataSetChanged() {
		dbHelper = new TodoSQLiteHelper(ctxt); // no-op
		String[] tableColumns = new String[] { "_id", "title", "todo" };
		cursor = dbHelper.getReadableDatabase().query("todos", tableColumns,
				null, null, null, null, null);
	}
}