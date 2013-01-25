package com.appdev.onetwoguide;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Login_Form extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login__form);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login__form, menu);
		return true;
	}

}
