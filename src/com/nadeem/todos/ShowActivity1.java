package com.nadeem.todos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ShowActivity1 extends SherlockActivity {
	public String idvalue,idvalue1;
	public int idvalue2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shows);
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		idvalue = extras.getString("iddd");
		idvalue1 = extras.getString("iddd1");
		idvalue2 = extras.getInt("iddd2");
		TextView todoText = (TextView) findViewById(R.id.showit);
		todoText.setText(idvalue);
}
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.update, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.update1:
	    	int b=1;
			Intent intent3 = new Intent(this, AddTodoActivity.class);
			intent3.putExtra("idd1", idvalue);
			intent3.putExtra("idd2", idvalue2);
			intent3.putExtra("idd3",b);
			intent3.putExtra("idd4",idvalue1);
			startActivity(intent3);
			return true;
			
			
	    	
	        case android.R.id.home:
	            
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
}
}