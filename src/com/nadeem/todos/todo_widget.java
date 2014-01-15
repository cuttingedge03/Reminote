package com.nadeem.todos;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

public class todo_widget extends AppWidgetProvider {
	private SQLiteDatabase db;
	private TodoSQLiteHelper dbHelper;
	String[] widget_title = new String[50];
	String[] widget_content = new String[50];
	public static String ACTION_WIDGET_RIGHT = "ActionReceiverRight";
	public static String ACTION_WIDGET_LEFT = "ActionReceiverLeft";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				"any", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt("flag", 0);
		editor.apply();

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.contentw1, pendingIntent);
		views.setOnClickPendingIntent(R.id.contentw2, pendingIntent);
		views.setOnClickPendingIntent(R.id.contentw3, pendingIntent);
		
		Intent i = new Intent(context, todo_widget.class);
		i.setAction(ACTION_WIDGET_LEFT);
		PendingIntent p = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.left, p);
		
		Intent i1 = new Intent(context, todo_widget.class);
		i1.setAction(ACTION_WIDGET_RIGHT);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.right, pi);
		
		Intent i2 = new Intent(context, AddTodoActivity.class);
		i2.putExtra("idd5", 1);
		PendingIntent pii = PendingIntent.getActivity(context, 0, i2,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.Addnew, pii);
		
		dbHelper = new TodoSQLiteHelper(context);
		db = dbHelper.getWritableDatabase();
		String[] tableColumns = new String[] { "_id", "title", "todo" };

		int j = 0;

		Cursor cursor = db.query("todos", tableColumns, null, null, null, null,
				null);
		cursor.moveToFirst();
		int noOfRows=cursor.getCount();
		while (!cursor.isAfterLast()) {

			if (j == 3) {
				break;
			}
			widget_title[j] = cursor.getString(1);
			widget_content[j] = cursor.getString(2);
			j++;

			cursor.moveToNext();
		}
		if(noOfRows<=3)
		{
			views.setViewVisibility(R.id.right, View.GONE);
		}
		db.close();
		views.setViewVisibility(R.id.left, View.GONE);
		views.setTextViewText(R.id.titlew1, widget_title[0]);
		views.setTextViewText(R.id.titlew2, widget_title[1]);
		views.setTextViewText(R.id.titlew3, widget_title[2]);
		views.setTextViewText(R.id.contentw1, widget_content[0]);
		views.setTextViewText(R.id.contentw2, widget_content[1]);
		views.setTextViewText(R.id.contentw3, widget_content[2]);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
				new ComponentName(context, todo_widget.class));
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		Intent intent2 = new Intent(context, MainActivity.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent2.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.contentw1, pendingIntent);
		views.setOnClickPendingIntent(R.id.contentw2, pendingIntent);
		views.setOnClickPendingIntent(R.id.contentw3, pendingIntent);
		Intent i = new Intent(context, todo_widget.class);
		i.setAction(ACTION_WIDGET_LEFT);
		PendingIntent p = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.left, p);
		
		Intent i1 = new Intent(context, todo_widget.class);
		i1.setAction(ACTION_WIDGET_RIGHT);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.right, pi);
		
		Intent i2 = new Intent(context, AddTodoActivity.class);
		PendingIntent pii = PendingIntent.getBroadcast(context, 0, i2,
				PendingIntent.FLAG_UPDATE_CURRENT);
		i2.putExtra("idd5", 1);
		views.setOnClickPendingIntent(R.id.Addnew, pii);
		
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				"any", Activity.MODE_PRIVATE);
		int flag = mySharedPreferences.getInt("flag", 15);

		if (intent.getAction().equals(ACTION_WIDGET_RIGHT)) {
			views.setViewVisibility(R.id.left, View.VISIBLE);
			dbHelper = new TodoSQLiteHelper(context);
			db = dbHelper.getWritableDatabase();

			String[] tableColumns = new String[] { "_id", "title", "todo" };

			int j = 0;

			Cursor cursor = db.query("todos", tableColumns, null, null, null,
					null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				widget_title[j] = cursor.getString(1);
				widget_content[j] = cursor.getString(2);
				j++;

				cursor.moveToNext();
			}
			db.close();
			if (flag < ((int) (j / 3.1))) {//to check the number of todos to show
				if (flag == 0) {
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);//to remove the right widget btn
					}
					views.setTextViewText(R.id.titlew1, widget_title[3]);
					views.setTextViewText(R.id.contentw1, widget_content[3]);
					views.setTextViewText(R.id.titlew2, widget_title[4]);
					views.setTextViewText(R.id.contentw2, widget_content[4]);
					views.setTextViewText(R.id.titlew3, widget_title[5]);
					views.setTextViewText(R.id.contentw3, widget_content[5]);
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 1) {
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					views.setTextViewText(R.id.titlew1, widget_title[6]);
					views.setTextViewText(R.id.contentw1, widget_content[6]);
					views.setTextViewText(R.id.titlew2, widget_title[7]);
					views.setTextViewText(R.id.contentw2, widget_content[7]);
					views.setTextViewText(R.id.titlew3, widget_title[8]);
					views.setTextViewText(R.id.contentw3, widget_content[8]);
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 2) {
					views.setTextViewText(R.id.titlew1, widget_title[9]);
					views.setTextViewText(R.id.contentw1, widget_content[9]);
					views.setTextViewText(R.id.titlew2, widget_title[10]);
					views.setTextViewText(R.id.contentw2, widget_content[10]);
					views.setTextViewText(R.id.titlew3, widget_title[11]);
					views.setTextViewText(R.id.contentw3, widget_content[11]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 3) {
					views.setTextViewText(R.id.titlew1, widget_title[12]);
					views.setTextViewText(R.id.contentw1, widget_content[12]);
					views.setTextViewText(R.id.titlew2, widget_title[13]);
					views.setTextViewText(R.id.contentw2, widget_content[13]);
					views.setTextViewText(R.id.titlew3, widget_title[14]);
					views.setTextViewText(R.id.contentw3, widget_content[14]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 4) {
					views.setTextViewText(R.id.titlew1, widget_title[15]);
					views.setTextViewText(R.id.contentw1, widget_content[15]);
					views.setTextViewText(R.id.titlew2, widget_title[16]);
					views.setTextViewText(R.id.contentw2, widget_content[16]);
					views.setTextViewText(R.id.titlew3, widget_title[17]);
					views.setTextViewText(R.id.contentw3, widget_content[17]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 5) {
					views.setTextViewText(R.id.titlew1, widget_title[18]);
					views.setTextViewText(R.id.contentw1, widget_content[18]);
					views.setTextViewText(R.id.titlew2, widget_title[19]);
					views.setTextViewText(R.id.contentw2, widget_content[19]);
					views.setTextViewText(R.id.titlew3, widget_title[20]);
					views.setTextViewText(R.id.contentw3, widget_content[20]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 6) {
					views.setTextViewText(R.id.titlew1, widget_title[21]);
					views.setTextViewText(R.id.contentw1, widget_content[21]);
					views.setTextViewText(R.id.titlew2, widget_title[22]);
					views.setTextViewText(R.id.contentw2, widget_content[22]);
					views.setTextViewText(R.id.titlew3, widget_title[23]);
					views.setTextViewText(R.id.contentw3, widget_content[23]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 7) {
					views.setTextViewText(R.id.titlew1, widget_title[24]);
					views.setTextViewText(R.id.contentw1, widget_content[24]);
					views.setTextViewText(R.id.titlew2, widget_title[25]);
					views.setTextViewText(R.id.contentw2, widget_content[25]);
					views.setTextViewText(R.id.titlew3, widget_title[26]);
					views.setTextViewText(R.id.contentw3, widget_content[26]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 8) {
					views.setTextViewText(R.id.titlew1, widget_title[27]);
					views.setTextViewText(R.id.contentw1, widget_content[27]);
					views.setTextViewText(R.id.titlew2, widget_title[28]);
					views.setTextViewText(R.id.contentw2, widget_content[28]);
					views.setTextViewText(R.id.titlew3, widget_title[29]);
					views.setTextViewText(R.id.contentw3, widget_content[29]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 9) {
					views.setTextViewText(R.id.titlew1, widget_title[30]);
					views.setTextViewText(R.id.contentw1, widget_content[30]);
					views.setTextViewText(R.id.titlew2, widget_title[31]);
					views.setTextViewText(R.id.contentw2, widget_content[31]);
					views.setTextViewText(R.id.titlew3, widget_title[32]);
					views.setTextViewText(R.id.contentw3, widget_content[32]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 10) {
					views.setTextViewText(R.id.titlew1, widget_title[33]);
					views.setTextViewText(R.id.contentw1, widget_content[33]);
					views.setTextViewText(R.id.titlew2, widget_title[34]);
					views.setTextViewText(R.id.contentw2, widget_content[34]);
					views.setTextViewText(R.id.titlew3, widget_title[35]);
					views.setTextViewText(R.id.contentw3, widget_content[35]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 11) {
					views.setTextViewText(R.id.titlew1, widget_title[36]);
					views.setTextViewText(R.id.contentw1, widget_content[36]);
					views.setTextViewText(R.id.titlew2, widget_title[37]);
					views.setTextViewText(R.id.contentw2, widget_content[37]);
					views.setTextViewText(R.id.titlew3, widget_title[38]);
					views.setTextViewText(R.id.contentw3, widget_content[38]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 12) {
					views.setTextViewText(R.id.titlew1, widget_title[39]);
					views.setTextViewText(R.id.contentw1, widget_content[39]);
					views.setTextViewText(R.id.titlew2, widget_title[40]);
					views.setTextViewText(R.id.contentw2, widget_content[40]);
					views.setTextViewText(R.id.titlew3, widget_title[41]);
					views.setTextViewText(R.id.contentw3, widget_content[41]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 13) {
					views.setTextViewText(R.id.titlew1, widget_title[42]);
					views.setTextViewText(R.id.contentw1, widget_content[42]);
					views.setTextViewText(R.id.titlew2, widget_title[43]);
					views.setTextViewText(R.id.contentw2, widget_content[43]);
					views.setTextViewText(R.id.titlew3, widget_title[44]);
					views.setTextViewText(R.id.contentw3, widget_content[44]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 14) {
					views.setTextViewText(R.id.titlew1, widget_title[45]);
					views.setTextViewText(R.id.contentw1, widget_content[45]);
					views.setTextViewText(R.id.titlew2, widget_title[46]);
					views.setTextViewText(R.id.contentw2, widget_content[46]);
					views.setTextViewText(R.id.titlew3, widget_title[47]);
					views.setTextViewText(R.id.contentw3, widget_content[47]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				} else if (flag == 15) {
					views.setTextViewText(R.id.titlew1, widget_title[48]);
					views.setTextViewText(R.id.contentw1, widget_content[48]);
					views.setTextViewText(R.id.titlew2, widget_title[49]);
					views.setTextViewText(R.id.contentw2, widget_content[49]);
					views.setTextViewText(R.id.titlew3, widget_title[50]);
					views.setTextViewText(R.id.contentw3, widget_content[50]);
					if (flag == ((int) (j / 3.1-1))) {
						views.setViewVisibility(R.id.right, View.GONE);
					}
					appWidgetManager.updateAppWidget(ids, views);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putInt("flag", ++flag);
					editor.apply();
				}
			}
		}
		if (intent.getAction().equals(ACTION_WIDGET_LEFT)) {
			views.setViewVisibility(R.id.right, View.VISIBLE);
			dbHelper = new TodoSQLiteHelper(context);
			db = dbHelper.getWritableDatabase();

			String[] tableColumns = new String[] { "_id", "title", "todo" };

			int j = 0;

			Cursor cursor = db.query("todos", tableColumns, null, null, null,
					null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				widget_title[j] = cursor.getString(1);
				widget_content[j] = cursor.getString(2);
				j++;

				cursor.moveToNext();
			}
			db.close();
			if (flag >= 1) {
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putInt("flag", --flag);
				editor.apply();
				if (flag == 0) {
					views.setViewVisibility(R.id.left, View.GONE);
					views.setTextViewText(R.id.titlew1, widget_title[0]);
					views.setTextViewText(R.id.contentw1, widget_content[0]);
					views.setTextViewText(R.id.titlew2, widget_title[1]);
					views.setTextViewText(R.id.contentw2, widget_content[1]);
					views.setTextViewText(R.id.titlew3, widget_title[2]);
					views.setTextViewText(R.id.contentw3, widget_content[2]);
					appWidgetManager.updateAppWidget(ids, views);
				} else if (flag == 1) {
					views.setTextViewText(R.id.titlew1, widget_title[3]);
					views.setTextViewText(R.id.contentw1, widget_content[3]);
					views.setTextViewText(R.id.titlew2, widget_title[4]);
					views.setTextViewText(R.id.contentw2, widget_content[4]);
					views.setTextViewText(R.id.titlew3, widget_title[5]);
					views.setTextViewText(R.id.contentw3, widget_content[5]);
					appWidgetManager.updateAppWidget(ids, views);
				} else if (flag == 2) {
					views.setTextViewText(R.id.titlew1, widget_title[6]);
					views.setTextViewText(R.id.contentw1, widget_content[6]);
					views.setTextViewText(R.id.titlew2, widget_title[7]);
					views.setTextViewText(R.id.contentw2, widget_content[7]);
					views.setTextViewText(R.id.titlew3, widget_title[8]);
					views.setTextViewText(R.id.contentw3, widget_content[8]);
					appWidgetManager.updateAppWidget(ids, views);
				} else if (flag == 3) {
					views.setTextViewText(R.id.titlew1, widget_title[9]);
					views.setTextViewText(R.id.contentw1, widget_content[9]);
					views.setTextViewText(R.id.titlew2, widget_title[10]);
					views.setTextViewText(R.id.contentw2, widget_content[10]);
					views.setTextViewText(R.id.titlew3, widget_title[11]);
					views.setTextViewText(R.id.contentw3, widget_content[11]);
					appWidgetManager.updateAppWidget(ids, views);
				} else if (flag == 4) {
					views.setTextViewText(R.id.titlew1, widget_title[12]);
					views.setTextViewText(R.id.contentw1, widget_content[12]);
					views.setTextViewText(R.id.titlew2, widget_title[13]);
					views.setTextViewText(R.id.contentw2, widget_content[13]);
					views.setTextViewText(R.id.titlew3, widget_title[14]);
					views.setTextViewText(R.id.contentw3, widget_content[14]);
					appWidgetManager.updateAppWidget(ids, views);
				} else if (flag == 5) {
					views.setTextViewText(R.id.titlew1, widget_title[15]);
					views.setTextViewText(R.id.contentw1, widget_content[15]);
					views.setTextViewText(R.id.titlew2, widget_title[16]);
					views.setTextViewText(R.id.contentw2, widget_content[16]);
					views.setTextViewText(R.id.titlew3, widget_title[17]);
					views.setTextViewText(R.id.contentw3, widget_content[17]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 6) {
					views.setTextViewText(R.id.titlew1, widget_title[18]);
					views.setTextViewText(R.id.contentw1, widget_content[18]);
					views.setTextViewText(R.id.titlew2, widget_title[19]);
					views.setTextViewText(R.id.contentw2, widget_content[19]);
					views.setTextViewText(R.id.titlew3, widget_title[20]);
					views.setTextViewText(R.id.contentw3, widget_content[20]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 7) {
					views.setTextViewText(R.id.titlew1, widget_title[21]);
					views.setTextViewText(R.id.contentw1, widget_content[21]);
					views.setTextViewText(R.id.titlew2, widget_title[22]);
					views.setTextViewText(R.id.contentw2, widget_content[22]);
					views.setTextViewText(R.id.titlew3, widget_title[23]);
					views.setTextViewText(R.id.contentw3, widget_content[23]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 8) {
					views.setTextViewText(R.id.titlew1, widget_title[24]);
					views.setTextViewText(R.id.contentw1, widget_content[24]);
					views.setTextViewText(R.id.titlew2, widget_title[25]);
					views.setTextViewText(R.id.contentw2, widget_content[25]);
					views.setTextViewText(R.id.titlew3, widget_title[26]);
					views.setTextViewText(R.id.contentw3, widget_content[26]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 9) {
					views.setTextViewText(R.id.titlew1, widget_title[27]);
					views.setTextViewText(R.id.contentw1, widget_content[27]);
					views.setTextViewText(R.id.titlew2, widget_title[28]);
					views.setTextViewText(R.id.contentw2, widget_content[28]);
					views.setTextViewText(R.id.titlew3, widget_title[29]);
					views.setTextViewText(R.id.contentw3, widget_content[29]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 10) {
					views.setTextViewText(R.id.titlew1, widget_title[30]);
					views.setTextViewText(R.id.contentw1, widget_content[30]);
					views.setTextViewText(R.id.titlew2, widget_title[31]);
					views.setTextViewText(R.id.contentw2, widget_content[31]);
					views.setTextViewText(R.id.titlew3, widget_title[32]);
					views.setTextViewText(R.id.contentw3, widget_content[32]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 11) {
					views.setTextViewText(R.id.titlew1, widget_title[33]);
					views.setTextViewText(R.id.contentw1, widget_content[33]);
					views.setTextViewText(R.id.titlew2, widget_title[34]);
					views.setTextViewText(R.id.contentw2, widget_content[34]);
					views.setTextViewText(R.id.titlew3, widget_title[35]);
					views.setTextViewText(R.id.contentw3, widget_content[35]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 12) {
					views.setTextViewText(R.id.titlew1, widget_title[36]);
					views.setTextViewText(R.id.contentw1, widget_content[36]);
					views.setTextViewText(R.id.titlew2, widget_title[37]);
					views.setTextViewText(R.id.contentw2, widget_content[37]);
					views.setTextViewText(R.id.titlew3, widget_title[38]);
					views.setTextViewText(R.id.contentw3, widget_content[38]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 13) {
					views.setTextViewText(R.id.titlew1, widget_title[39]);
					views.setTextViewText(R.id.contentw1, widget_content[39]);
					views.setTextViewText(R.id.titlew2, widget_title[40]);
					views.setTextViewText(R.id.contentw2, widget_content[40]);
					views.setTextViewText(R.id.titlew3, widget_title[41]);
					views.setTextViewText(R.id.contentw3, widget_content[41]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 14) {
					views.setTextViewText(R.id.titlew1, widget_title[42]);
					views.setTextViewText(R.id.contentw1, widget_content[42]);
					views.setTextViewText(R.id.titlew2, widget_title[43]);
					views.setTextViewText(R.id.contentw2, widget_content[43]);
					views.setTextViewText(R.id.titlew3, widget_title[44]);
					views.setTextViewText(R.id.contentw3, widget_content[44]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 15) {
					views.setTextViewText(R.id.titlew1, widget_title[45]);
					views.setTextViewText(R.id.contentw1, widget_content[45]);
					views.setTextViewText(R.id.titlew2, widget_title[46]);
					views.setTextViewText(R.id.contentw2, widget_content[46]);
					views.setTextViewText(R.id.titlew3, widget_title[47]);
					views.setTextViewText(R.id.contentw3, widget_content[47]);
					appWidgetManager.updateAppWidget(ids, views);

				} else if (flag == 16) {
					views.setTextViewText(R.id.titlew1, widget_title[48]);
					views.setTextViewText(R.id.contentw1, widget_content[48]);
					views.setTextViewText(R.id.titlew2, widget_title[49]);
					views.setTextViewText(R.id.contentw2, widget_content[49]);
					views.setTextViewText(R.id.titlew3, widget_title[50]);
					views.setTextViewText(R.id.contentw3, widget_content[50]);
					appWidgetManager.updateAppWidget(ids, views);

				}
			}
		} else if (intent.getAction().equals(
				AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
			views.setViewVisibility(R.id.left, View.GONE);
			
			views.setViewVisibility(R.id.right, View.VISIBLE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putInt("flag", 0);
			editor.apply();
			dbHelper = new TodoSQLiteHelper(context);
			db = dbHelper.getWritableDatabase();
			String[] tableColumns = new String[] { "_id", "title", "todo" };

			int j = 0;

			Cursor cursor = db.query("todos", tableColumns, null, null, null,
					null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				if (j == 3) {
					break;}
					widget_title[j] = cursor.getString(1);
					widget_content[j] = cursor.getString(2);
				j++;

				cursor.moveToNext();
			}
			db.close();
			views.setTextViewText(R.id.titlew1, widget_title[0]);
			views.setTextViewText(R.id.titlew2, widget_title[1]);
			views.setTextViewText(R.id.titlew3, widget_title[2]);
			views.setTextViewText(R.id.contentw1, widget_content[0]);
			views.setTextViewText(R.id.contentw2, widget_content[1]);
			views.setTextViewText(R.id.contentw3, widget_content[2]);
			appWidgetManager.updateAppWidget(ids, views);

		}
		super.onReceive(context, intent);
	}
}