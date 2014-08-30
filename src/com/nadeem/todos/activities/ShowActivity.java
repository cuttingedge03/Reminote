package com.nadeem.todos.activities;

import com.nadeem.todos.R;
import com.nadeem.todos.R.id;
import com.nadeem.todos.R.layout;
import com.nadeem.todos.R.menu;
import com.nadeem.todos.provider.WidgetProvider;

import android.app.ActionBar;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ShowActivity extends Activity {
	public String description, title;
	public int id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shows);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		description = extras.getString(WidgetProvider.EXTRA_DESCRIPTION);
		title = extras.getString(WidgetProvider.EXTRA_TITLE);
		id = extras.getInt(WidgetProvider.EXTRA_ID);
		TextView todoText = (TextView) findViewById(R.id.showit);
		TextView todoTitle = (TextView) findViewById(R.id.textView1);
		todoText.setText(description);
		todoTitle.setText(title);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.update1:
			int b = 1;
			Intent intent3 = new Intent(this, AddTodoActivity.class);
			intent3.putExtra(WidgetProvider.EXTRA_DESCRIPTION, description);
			intent3.putExtra(WidgetProvider.EXTRA_ID, id);
			intent3.putExtra("idd3", b);
			intent3.putExtra(WidgetProvider.EXTRA_TITLE, title);
			startActivity(intent3);
			return true;

		case android.R.id.home:

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {

		Intent intent1 = new Intent(this, WidgetProvider.class);
		intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		int[] mainIds = AppWidgetManager.getInstance(getApplication())
				.getAppWidgetIds(
						new ComponentName(getApplication(),
								WidgetProvider.class));
		intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mainIds);
		sendBroadcast(intent1);
		this.finish();
	}
}