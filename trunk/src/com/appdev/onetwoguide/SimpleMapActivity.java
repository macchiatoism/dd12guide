package com.appdev.onetwoguide;

import java.io.IOException;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SimpleMapActivity extends MapActivity implements LocationListener  {

	private static final String TAG = "LocationActivity";
	
	LocationManager locationManager;
	Geocoder geocoder; 
	TextView locationText;

	private MapView mapView;
	private MapController mapController;

	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_map);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		locationText = (TextView)this.findViewById(R.id.TextView01);
		mapView=(MapView)findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		mapController.setZoom(10);
		
		locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE); //<2>
		    
		geocoder = new Geocoder(this); //<3>
		    
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //<5>
		if (location != null) {
			Log.d(TAG, location.toString());
		}
		this.onLocationChanged(location); //<6>
		
		mapView.setSatellite(true);
		
		
		
		//mapController.setCenter(point);
	}
	
	
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this); //<7>
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this); //<8>
	  }

	  @Override
	  public void onLocationChanged(Location location) { //<9>
	    Log.d(TAG, "onLocationChanged with location " + location.toString());
	    String text = String.format("Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f", location.getLatitude(), 
	                  location.getLongitude(), location.getAltitude(), location.getBearing());
	    this.locationText.setText(text);
	    
	    try {
	      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10); //<10>
	      for (Address address : addresses) {
	        this.locationText.append("\n" + address.getAddressLine(0));
	      }
	      
	      int latitude = (int)(location.getLatitude() * 1000000);
	      int longitude = (int)(location.getLongitude() * 1000000);

	      GeoPoint point = new GeoPoint(latitude,longitude);
	      mapController.animateTo(point); //<11>
	      
	    } catch (IOException e) {
	      Log.e("LocateMe", "Could not get Geocoder data", e);
	    }
	  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_simple_map, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			// This ID represents the Home or Up button. In the case of this
//			// activity, the Up button is shown. Use NavUtils to allow users
//			// to navigate up one level in the application structure. For
//			// more details, see the Navigation pattern on Android Design:
//			//
//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
//			//
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
//	@Override
//	protected boolean isLocationDisplayed(){
//		return false;
//	}

}
