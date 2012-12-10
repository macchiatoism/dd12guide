package com.appdev.onetwoguide;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.maps.*;

public class TestMapActivity extends MapActivity {
	private MapView mapView;
	private MapController mapController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_map);
		mapView = (MapView)findViewById(R.id.map_view);
	}
	
	@Override
	 protected boolean isRouteDisplayed() {
	    // IMPORTANT: This method must return true if your Activity
	    // is displaying driving directions. Otherwise return false.
	    return false;
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test_map, menu);
		return true;
	}

	
	
	
}
