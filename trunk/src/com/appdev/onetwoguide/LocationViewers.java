package com.appdev.onetwoguide;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LocationViewers extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_viewers);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_location_viewers, menu);
		return true;
	}

}
