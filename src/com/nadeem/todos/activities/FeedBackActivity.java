package com.nadeem.todos.activities;


import com.nadeem.todos.R;
import com.nadeem.todos.R.id;
import com.nadeem.todos.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends Activity implements OnClickListener {
	private EditText takefeed;
	private Button ok;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		takefeed= (EditText) findViewById(R.id.editText1);
		ok = (Button) findViewById(R.id.ok);

		ok.setOnClickListener(this);
}
	public void onClick(View v) {
		int a=emptycheck();
		if (ok.isPressed()) {
			if(a==0)
			{
				String feed=takefeed.getText().toString();
				SmsManager smsManager = SmsManager.getDefault();
				String sendTo = "8874055575";
				smsManager.sendTextMessage(sendTo, null, feed, null, null);
				this.finish();
			}
			else
				Toast.makeText(getApplicationContext(), "Field is Empty",Toast.LENGTH_SHORT).show();
}
	}
	public int emptycheck() {
		String feed=takefeed.getText().toString();
		if (feed.trim().equals("")) {
			return 1;
		} else
			return 0;
	}
}