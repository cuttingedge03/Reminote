package com.nadeem.todos;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;



public class MainActivity extends SherlockListActivity {

	private TodoDAO dao;
	private SQLiteDatabase db;
	private TodoSQLiteHelper dbHelper;
	public static int hour = 0, minute = 0, currentHour = 0, currentMinute = 0,
			currentSecond = 0;
	public static int setSeconds, id_alarm, s = 0, m = 0;
	AlarmManager am;
	public static String subject, content_alarm;
	MenuItem hide;
	MenuItem reminder;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new TodoDAO(this);
		tv = new TextView(this);
		final ListView lv = getListView();

		registerForContextMenu(lv);
//		setListAdapter(new ListAdapter(this, dao.getTodos()));
		AnimationAdapter swingBottomInAnimationAdapter = new SwingRightInAnimationAdapter(
				new ListAdapter(this, dao.getTodos()));
		// SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new
		// SwingBottomInAnimationAdapter(swingRightInAnimationAdapter);

		swingBottomInAnimationAdapter.setAbsListView(lv);

		tv.setTextColor(Color.BLACK);
		tv.setGravity(Gravity.CENTER);
		lv.addFooterView(tv);
		lv.setAdapter(swingBottomInAnimationAdapter);
		lv.setDivider(null);
		lv.setBackgroundColor(Color.parseColor("#ececec"));
		tv.setOnClickListener(new OnClickListener() {
			TextView t=(TextView)lv.findViewById(R.id.textView1);
			@Override
			public void onClick(View v) {
			t.setVisibility(View.GONE);
				
			}
		});
		// Toast.makeText(getApplicationContext(),
		// "Long press on any title for options", Toast.LENGTH_SHORT)
		// .show();

	}

	@Override
	protected void onResume() {
		super.onResume();
		int item_count = getListView().getCount();
//		Toast.makeText(getApplicationContext(), String.valueOf(item_count), Toast.LENGTH_SHORT).show();
		if ((item_count-1) == 0)
			tv.setText("Add new todo items");
		else
			tv.setText("Long press on any title for options.");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Todo todo = (Todo) getListView().getItemAtPosition(position);
		int b = todo.getId();
		String textshow = todo.getText1();
		String textshow1 = todo.getText();
		Intent intent2 = new Intent(this, ShowActivity1.class);
		intent2.putExtra("iddd", textshow);
		intent2.putExtra("iddd1", textshow1);
		intent2.putExtra("iddd2", b);
		startActivity(intent2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu) {

			Intent intent = new Intent(this, AddTodoActivity.class);
			intent.putExtra("idd5", 1);
			startActivity(intent);
			dao.close();
		} else if (item.getItemId() == R.id.feedback) {

			Intent intent = new Intent(this, FeedBack.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.About) {
			Intent intent = new Intent(this, About.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.menu, menu);
		menu.setHeaderTitle("Select an action");
		menu.setHeaderIcon(R.drawable.forget);
	}

	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			Todo todo = (Todo) getListView().getItemAtPosition(
					menuInfo.position);
			dao.deleteTodo(todo.getId());
			setListAdapter(new ListAdapter(this, dao.getTodos()));
			Toast.makeText(getApplicationContext(), "Deleted!",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.show:
			// Todo todo2 = (Todo)
			// getListView().getItemAtPosition(menuInfo.position);
			Todo todo2 = (Todo) getListView().getItemAtPosition(
					menuInfo.position);
			int c = todo2.getId();
			String textshow = todo2.getText1();
			String textshow3 = todo2.getText();
			Intent intent2 = new Intent(this, ShowActivity1.class);
			intent2.putExtra("iddd", textshow);
			intent2.putExtra("iddd1", textshow3);
			intent2.putExtra("iddd2", c);
			startActivity(intent2);
			break;
		case R.id.reminder:
			showDialog(99);
			// Todo todo5 = (Todo)
			// getListView().getItemAtPosition(menuInfo.position);
			Todo todo5 = (Todo) getListView().getItemAtPosition(
					menuInfo.position);
			subject = todo5.getText();
			id_alarm = todo5.getId();
			content_alarm = todo5.getText1();
			break;
		case R.id.priority:
			// Todo todo4 = (Todo)
			// getListView().getItemAtPosition(menuInfo.position);
			Todo todo4 = (Todo) getListView().getItemAtPosition(
					menuInfo.position);
			dbHelper = new TodoSQLiteHelper(this);
			db = dbHelper.getWritableDatabase();
			String[] tableColumns = new String[] { "_id", "title", "todo",
					"time" };
			Cursor cursor = db.query("todos", tableColumns, null, null, null,
					null, null);
			Cursor cursor1 = db.query("todos", tableColumns, null, null, null,
					null, null);
			cursor.moveToFirst();
			cursor1.moveToFirst();
			int id = cursor.getInt(0);
			int a1 = todo4.getId();
			String title2 = todo4.getText();
			String content2 = todo4.getText1();
			String time2 = todo4.getTime();
			String title = cursor.getString(1);
			String content = cursor.getString(2);
			String time = cursor.getString(3);
			String[] title3 = new String[50];
			String[] content3 = new String[50];
			String[] time3 = new String[50];
			/*
			 * ContentValues contentValues = new ContentValues();
			 * contentValues.put("todo", content); contentValues.put("title",
			 * title); db.update("todos", contentValues, "_id " + "=" + a1,
			 * null);
			 */
			if (a1 != id) {
				ContentValues contentValue = new ContentValues();
				contentValue.put("todo", content2);
				contentValue.put("title", title2);// put the values of selected
													// row to first row
				contentValue.put("time", time2);
				db.update("todos", contentValue, "_id " + "=" + id, null);// update
																			// first
																			// row
				cursor.moveToNext();
				int i = 0;
				while (!cursor.isAfterLast()) {
					title3[i] = cursor.getString(1);
					content3[i] = cursor.getString(2);
					time3[i] = cursor.getString(3);
					int id1 = cursor.getInt(0);// gives id of second row
					if (i == 0) {
						ContentValues contentValues = new ContentValues();
						contentValues.put("todo", content);
						contentValues.put("title", title);// put the value of
															// 1st row to second
						contentValues.put("time", time);
						db.update("todos", contentValues, "_id " + "=" + id1,
								null);// update second row
					} else {
						if (a1 == cursor1.getInt(0)) {
							db.close();
							break;// as soon as cursor reaches selected row
									// break the loop
						}
						ContentValues contentValues = new ContentValues();
						contentValues.put("todo", content3[i - 1]);
						contentValues.put("title", title3[i - 1]);
						contentValues.put("time", time3[i - 1]);
						db.update("todos", contentValues, "_id " + "=" + id1,
								null);

					}
					cursor.moveToNext();
					cursor1.moveToNext();
					i++;
				}
				db.close();
				setListAdapter(new ListAdapter(this, dao.getTodos()));
			} else {
				Toast.makeText(getApplicationContext(),
						"It's already your priority!", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.update:
			// Todo todo3 = (Todo)
			// getListView().getItemAtPosition(menuInfo.position);
			Todo todo3 = (Todo) getListView().getItemAtPosition(
					menuInfo.position);
			int a = todo3.getId();
			String textshow1 = todo3.getText1();
			String textshow2 = todo3.getText();
			int b = 1;
			Intent intent3 = new Intent(this, AddTodoActivity.class);
			intent3.putExtra("idd1", textshow1);
			intent3.putExtra("idd2", a);
			intent3.putExtra("idd3", b);
			intent3.putExtra("idd4", textshow2);
			startActivity(intent3);
			break;

		}

		return true;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, todo_widget.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int[] ids = AppWidgetManager.getInstance(getApplication())
				.getAppWidgetIds(
						new ComponentName(getApplication(), todo_widget.class));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		sendBroadcast(intent);
		this.finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 99:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			calculate(hour, minute);
		}
	};

	public void calculate(int a, int b) {
		hour = a;
		minute = b;
		setOneTimeAlarm();
	}

	public void setOneTimeAlarm() {
		try {
			am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			final Calendar c = Calendar.getInstance();
			currentHour = c.get(Calendar.HOUR_OF_DAY);
			currentMinute = c.get(Calendar.MINUTE);
			currentSecond = c.get(Calendar.SECOND);
			setSeconds = (hour * 60 + minute) * 60 * 1000;
			int currentSeconds = ((currentHour * 60 + currentMinute) * 60 + currentSecond) * 1000;
			int finalSeconds = setSeconds - currentSeconds;
			dbHelper = new TodoSQLiteHelper(this);
			db = dbHelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			if (minute < 10) {
				contentValues.put("time", "Set on " + Integer.toString(hour)
						+ ":0" + Integer.toString(minute));
			} else {
				contentValues.put("time", "Set on " + Integer.toString(hour)
						+ ":" + Integer.toString(minute));
			}
			db.update("todos", contentValues, "_id " + "=" + id_alarm, null);
			db.close();

			// time.setText("Set on "+Integer.toString(hour)+":"+Integer.toString(minute));
			if (setSeconds > currentSeconds) {

				Intent intent = new Intent(this, TimeAlarm.class);
				intent.setData(Uri.parse("timer:" + subject));
				intent.putExtra("subject", subject);
				intent.putExtra("hour", hour);
				intent.putExtra("minute", minute);
				intent.putExtra("id_alarm", id_alarm);
				intent.putExtra("content_alarm", content_alarm);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
						0, intent, PendingIntent.FLAG_ONE_SHOT);
				am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
						+ finalSeconds, pendingIntent);
				s = finalSeconds / 1000 / 3600;
				m = finalSeconds / 1000 / 60 - (s * 60);

				Toast.makeText(
						getApplicationContext(),
						"Reminder set for " + subject + " " + s
								+ " hour(s) and " + (m + 1)
								+ " minutes from now", Toast.LENGTH_SHORT)
						.show();
			} else {
				finalSeconds = 24 * 60 * 60 * 1000 + finalSeconds;
				Intent intent = new Intent(this, TimeAlarm.class);
				intent.setData(Uri.parse("timer:" + subject));
				intent.putExtra("subject", subject);
				intent.putExtra("hour", hour);
				intent.putExtra("minute", minute);
				intent.putExtra("id_alarm", id_alarm);
				intent.putExtra("content_alarm", content_alarm);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
						0, intent, PendingIntent.FLAG_ONE_SHOT);
				am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
						+ finalSeconds, pendingIntent);
				s = finalSeconds / 1000 / 3600;
				m = finalSeconds / 1000 / 60 - (s * 60);
				if (s == 23 && m == 59) {
					Toast.makeText(
							getApplicationContext(),
							"Reminder set for " + subject
									+ " 24 hours and 0 minutes from now ",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Reminder set for " + subject + " " + s
									+ " hour(s) and " + (m + 1)
									+ " minutes from now", Toast.LENGTH_LONG)
							.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		setListAdapter(new ListAdapter(this, dao.getTodos()));
	}
}