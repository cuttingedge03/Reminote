package com.nadeem.todos.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.nadeem.todos.R;
import com.nadeem.todos.activities.AddTodoActivity;
import com.nadeem.todos.activities.MainActivity;
import com.nadeem.todos.activities.ShowActivity;
import com.nadeem.todos.service.WidgetService;

public class WidgetProvider extends AppWidgetProvider {
	public static String EXTRA_TITLE = "EXTRA_TITLE";
	public static String EXTRA_ID = "EXTRA_ID";
	public static String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";

	@Override
	public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		for (int i = 0; i < appWidgetIds.length; i++) {
			Intent svcIntent = new Intent(ctxt, WidgetService.class);

			svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			svcIntent.setData(Uri.parse(svcIntent
					.toUri(Intent.URI_INTENT_SCHEME)));

			RemoteViews widget = new RemoteViews(ctxt.getPackageName(),
					R.layout.widget);

			widget.setRemoteAdapter(R.id.list, svcIntent);

			Intent clickIntent = new Intent(ctxt, ShowActivity.class);
			PendingIntent clickPI = PendingIntent.getActivity(ctxt, 0,
					clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			widget.setPendingIntentTemplate(R.id.list, clickPI);

			Intent intent_add = new Intent(ctxt, AddTodoActivity.class);
			intent_add.putExtra(WidgetProvider.EXTRA_TITLE, "1");
			PendingIntent clickaddnew = PendingIntent.getActivity(ctxt, 0,
					intent_add, PendingIntent.FLAG_UPDATE_CURRENT);
			widget.setOnClickPendingIntent(R.id.addNew, clickaddnew);

			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
					R.id.list);
			appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
		}

		super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
	}
}