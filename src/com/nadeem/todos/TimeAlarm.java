package com.nadeem.todos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class TimeAlarm extends BroadcastReceiver {
	NotificationManager nm;
	private SQLiteDatabase db;
	private TodoSQLiteHelper dbHelper;
	public int id;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle extras = intent.getExtras();
			String subject = extras.getString("subject");
			int hour = extras.getInt("hour");
			int minute = extras.getInt("minute");
			String content_alarm = extras.getString("content_alarm");
			int id_alarm = extras.getInt("id_alarm");
			dbHelper = new TodoSQLiteHelper(context);
			db = dbHelper.getWritableDatabase();
			String[] tableColumns = new String[] { "_id", "title", "todo",
					"time" };
			String set1 = "Set on " + Integer.toString(hour) + ":0"
					+ Integer.toString(minute);
			String set2 = "Set on " + Integer.toString(hour) + ":"
					+ Integer.toString(minute);
			Cursor cursor = db.query("todos", tableColumns, null, null, null,
					null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				if (set1.equals(cursor.getString(3))
						|| set2.equals(cursor.getString(3))) {
					ContentValues contentValues = new ContentValues();
					contentValues.put("time", "");
					id = cursor.getInt(0);
					db.update("todos", contentValues, "_id " + "=" + id, null);
				}
				cursor.moveToNext();
			}
			Toast.makeText(context, "Reminder received!", Toast.LENGTH_SHORT)
					.show();
			nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Intent intent2 = new Intent(context, ShowActivity1.class);
			intent2.putExtra("iddd", content_alarm);
			intent2.putExtra("iddd1", subject);
			intent2.putExtra("iddd2", id);
			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent intents = PendingIntent
					.getActivity(context, 0, intent2, 0);

			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					context).setContentTitle("Reminder")
					.setContentText("It's time to do " + subject)
					.setSmallIcon(R.drawable.reminder)
					.addAction(R.drawable.reminder, content_alarm, intents)
					.setContentIntent(intents)
					.setWhen(System.currentTimeMillis())
					.setDefaults(Notification.DEFAULT_ALL);
			builder.setAutoCancel(true);
			Notification notification = builder.build();
			nm.notify(id_alarm, notification);db.close();
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
	}
}
