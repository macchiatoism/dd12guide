package com.appdev.onetwoguide;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class MainMapActivity extends MapActivity {

	
    Boolean isInternetPresent = false;// flag for Internet connection status
    ConnectionDetector cd;// Connection detector class
    AlertDialogManager alert = new AlertDialogManager();// Alert Dialog Manager
    
	private MapView mapView = null;
	private MapController mapController = null;
    private MyLocationOverlay whereAmI = null;
    
    private LocationManager locationManager; // gives location data
    private Location previousLocation; // previous reported location
    private RouteOverlay routeOverlay; // Overlay that shows route on map
    private long distanceTraveled; // total distance the user traveled
    private BearingFrameLayout bearingFrameLayout; // rotates the MapView
    private boolean tracking; // whether app is currently tracking
    private long startTime; // time (in milliseconds) when tracking starts
    private PowerManager.WakeLock wakeLock; // used to prevent device sleep
    private boolean gpsFix; // whether we have a GPS fix for accurate data
	
    
    private static final double MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
    private static final double MILES_PER_KILOMETER = 0.621371192;
    private static final int MAP_ZOOM = 15; // Google Maps supports 1-21
    
    public static final int BUTTON_POSITIVE = -1;
    public static final int BUTTON_NEGATIVE = -2;
    public static final int BUTTON_NEUTRAL = -3;
    
    int request_Code =1;
    
    Button btnShowOnMap;
    // field for searching nearest places
    PlacesList nearPlaces; // Nearest places
    // Map overlay items
    List<Overlay> mapOverlays;
    AddItemizedOverlay itemizedOverlay;
    double latitude;
    double longitude;
    OverlayItem overlayitem;
    GeoPoint geoPoint;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view_demo);
		
		// Getting intent data
        Intent i = getIntent();
		
		cd = new ConnectionDetector(getApplicationContext());
		
		// Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(MainMapActivity.this, "Internet Connection Not Found",
                    "Please enable Internet connection", false,BUTTON_NEUTRAL);
            // stop executing code by return
            //return;
        }
         
		
		// create new MapView using your Google Maps API key
	    bearingFrameLayout = new BearingFrameLayout(this, 
	         getResources().getString(R.string.googlemapapikey_windows));
		
	 // add bearingFrameLayout to mainLayout
	      FrameLayout mainLayout = 
	         (FrameLayout) findViewById(R.id.activity_map_view_demo);
	      mainLayout.addView(bearingFrameLayout, 0);  
		
	   // get the MapView and MapController
	    mapView = bearingFrameLayout.getMapview();
		
	    //mapView.enableBuiltInZoom();
		//mapView.displayZoomControls(true);
		
		mapController = mapView.getController();
        mapController.setZoom(MAP_ZOOM);
        
        // add current location overlay
        whereAmI = new MyCustomLocationOverlay(this, mapView);
		mapView.getOverlays().add(whereAmI);
		// Map overlay item
       //overlayitem = new OverlayItem(whereAmI.getMyLocation(), "Your Location",
                //"You're here!");
        
		
		
		
		mapView.postInvalidate();
		
		// create map Overlay
	    routeOverlay = new RouteOverlay(); 

	      // add the RouteOverlay overlay
	    mapView.getOverlays().add(routeOverlay);

	    distanceTraveled = 0; // initialize distanceTraveled to 0

	    // register listener for trackingToggleButton
	    ToggleButton trackingToggleButton = (ToggleButton) findViewById(R.id.trackingToggleButton);
	    trackingToggleButton.setOnCheckedChangeListener( trackingToggleButtonListener);
	    
	    Bundle extras = i.getExtras();
	    
	    if(extras!=null){
	    	// Users current geo location
	        String user_latitude = i.getStringExtra("user_latitude");
	        String user_longitude = i.getStringExtra("user_longitude");
	     // Nearplaces list
	        nearPlaces = (PlacesList) i.getSerializableExtra("near_places");
	        mapOverlays = mapView.getOverlays();
	        
	        // Geopoint to place on map
	        geoPoint = new GeoPoint((int) (Double.parseDouble(user_latitude) * 1E6),
	                (int) (Double.parseDouble(user_longitude) * 1E6));
	 
	        // Drawable marker icon
//	        Drawable drawable_user = this.getResources()
//	                .getDrawable(R.drawable.map_marker_pink);
//	 
	       // itemizedOverlay = new AddItemizedOverlay( this);
	 
	        // Map overlay item
//	        overlayitem = new OverlayItem(geoPoint, "Your Location",
//	                "You're here!");
//	 
//	        itemizedOverlay.addOverlay(overlayitem);
//	 
//	        mapOverlays.add(itemizedOverlay);
//	        itemizedOverlay.populateNow();
	 
	        // Drawable marker icon
	        Drawable drawable = this.getResources()
	                .getDrawable(R.drawable.map_marker_green);
	 
	        itemizedOverlay = new AddItemizedOverlay(drawable, this);
	 
	        mapController = mapView.getController();
	 
	        // These values are used to get map boundary area
	        // The area where you can see all the markers on screen
	        int minLat = Integer.MAX_VALUE;
	        int minLong = Integer.MAX_VALUE;
	        int maxLat = Integer.MIN_VALUE;
	        int maxLong = Integer.MIN_VALUE;
	 
	        // check for null in case it is null
	        if (nearPlaces.results != null) {
	            // loop through all the places
	            for (Place place : nearPlaces.results) {
	                latitude = place.geometry.location.lat; // latitude
	                longitude = place.geometry.location.lng; // longitude
	 
	                // Geopoint to place on map
	                geoPoint = new GeoPoint((int) (latitude * 1E6),
	                        (int) (longitude * 1E6));
	 
	                // Map overlay item
	                overlayitem = new OverlayItem(geoPoint, place.name,
	                        place.vicinity);
	 
	                itemizedOverlay.addOverlay(overlayitem);
	 
	                // calculating map boundary area
	                minLat  = (int) Math.min( geoPoint.getLatitudeE6(), minLat );
	                minLong = (int) Math.min( geoPoint.getLongitudeE6(), minLong);
	                maxLat  = (int) Math.max( geoPoint.getLatitudeE6(), maxLat );
	                maxLong = (int) Math.max( geoPoint.getLongitudeE6(), maxLong );
	            }
	            mapOverlays.add(itemizedOverlay);
	 
	            // showing all overlay items
	            itemizedOverlay.populateNow();
	        }
	 
	        // Adjusting the zoom level so that you can see all the markers on map
	        mapView.getController().zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));
	 
	        // Showing the center of the map
	        mapController.animateTo(new GeoPoint((maxLat + minLat)/2, (maxLong + minLong)/2 ));
	        mapView.postInvalidate();
	    }
	    
	  
		
	}
	
	// Method for create menu option
	private void CreateMenu(Menu menu){
		MenuItem mnu0 = menu.add(0,0,0,"Place Categories");
		{
			mnu0.setIcon(android.R.drawable.ic_menu_myplaces);
		}
	}
	
	/*checked again*/
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode==request_Code){
			if(resultCode==RESULT_OK){
				//String search ="";
				//Bundle extras = getIntent().getExtras();
				//if(extras !=null){
				//	search = extras.getString("category");
				//}
				Toast.makeText(this,data.getData().toString(),Toast.LENGTH_SHORT).show();
				
			}
		}
		
	}
	
	//Method for specify each choice's operation
	private boolean MenuChoice(MenuItem item){
		switch( item.getItemId()){
		case 0: 
			startActivity(new Intent("com.appdev.onetwoguide.PLACEMENUGRIDACTIVITY") ); // start menu
			Log.d("Events","start menu"); // for debugging
			return true;
		}
		return false;

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    CreateMenu(menu);
	    return true;
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return MenuChoice(item);
	}
	

	// called when Activity becoming visible to the user
	   @Override
	public void onStart() 
	   {
	      super.onStart(); // call super's onStart method
	      
	      // create Criteria object to specify location provider's settings
	      Criteria criteria = new Criteria(); 
	      criteria.setAccuracy(Criteria.ACCURACY_FINE); // fine location data
	      criteria.setBearingRequired(true); // need bearing to rotate map
	      criteria.setCostAllowed(true); // OK to incur monetary cost
	      criteria.setPowerRequirement(Criteria.POWER_LOW); // try to conserve
	      criteria.setAltitudeRequired(false); // don't need altitude data

	      // get the LocationManager
	      locationManager = 
	         (LocationManager) getSystemService(LOCATION_SERVICE);
	      
	      // register listener to determine whether we have a GPS fix
	      locationManager.addGpsStatusListener(gpsStatusListener);
	      
	      // get the best provider based on our Criteria
	      String provider = locationManager.getBestProvider(criteria, true);

	      // listen for changes in location as often as possible
	      locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
	     
	      // get the app's power manager
	      PowerManager powerManager = 
	         (PowerManager) getSystemService(Context.POWER_SERVICE);
	      
	      // get a wakelock preventing the device from sleeping
	      wakeLock = powerManager.newWakeLock(
	         PowerManager.PARTIAL_WAKE_LOCK, "No sleep");
	      wakeLock.acquire(); // acquire the wake lock

	      bearingFrameLayout.invalidate(); // redraw the BearingFrameLayout
	   } // end method onStart
	
	// called when Activity is no longer visible to the user
	   @Override
	   public void onStop()
	   {
	      super.onStop(); // call the super method
	      wakeLock.release(); // release the wakelock
	   } // end method onStop
	   
	// responds to events from the LocationManager
	   private final LocationListener locationListener = 
	      new LocationListener() 
	   {
	      // when the location is changed
	      public void onLocationChanged(Location location) 
	      {
	         gpsFix = true; // if getting Locations, then we have a GPS fix
	         
	         if (tracking) // if we're currently tracking
	            updateLocation(location); // update the location
	      } // end onLocationChanged

	      public void onProviderDisabled(String provider) 
	      {
	      } // end onProviderDisabled

	      public void onProviderEnabled(String provider) 
	      {
	      } // end onProviderEnabled

	      public void onStatusChanged(String provider, 
	         int status, Bundle extras) 
	      {
	      } // end onStatusChanged
	   }; // end locationListener
	   
	   // update location on map   
	   
	   public void updateLocation(Location location) 
	   {
	      if (location != null && gpsFix) // location not null; have GPS fix
	      {
	         // add the given Location to the route
	         routeOverlay.addPoint(location); 

	         // if there is a previous location
	         if (previousLocation != null) 
	         {
	            // add to the total distanceTraveled
	            distanceTraveled += location.distanceTo(previousLocation);
	         } // end if

	         // get the latitude and longitude
	         Double latitude = location.getLatitude() * 1E6;
	         Double longitude = location.getLongitude() * 1E6;

	         // create GeoPoint representing the given Locations
	         GeoPoint point = 
	            new GeoPoint(latitude.intValue(), longitude.intValue());

	         // move the map to the current location
	         mapController.animateTo(point);

	         // update the compass bearing
	         bearingFrameLayout.setBearing(location.getBearing());
	         bearingFrameLayout.invalidate(); // redraw based on bearing
	      } // end if
	      
	      previousLocation = location;
	   } // end method updateLocation
	      
	// determine whether we have GPS fix
	   GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() 
	   {
	      public void onGpsStatusChanged(int event) 
	      {
	         if (event == GpsStatus.GPS_EVENT_FIRST_FIX) 
	         {
	            gpsFix = true;
	            Toast results = Toast.makeText(MainMapActivity.this, 
	               getResources().getString(R.string.toast_signal_acquired), 
	               Toast.LENGTH_SHORT);
	            
	            // center the Toast in the screen
	            results.setGravity(Gravity.CENTER, 
	               results.getXOffset() / 2, results.getYOffset() / 2);     
	            results.show(); // display the results
	         } // end if
	      } // end method on GpsStatusChanged
	   }; // end anonymous inner class
	
	// register map view button
	public void myClickHandler(View target) {
        switch(target.getId()) {
        case R.id.sat:
            mapView.setSatellite(true);
            break;
        case R.id.traffic:
            mapView.setTraffic(true);
            break;
        case R.id.map:
            mapView.setSatellite(false);
            mapView.setTraffic(false);
            break;
        default:
        	break;
        }

        // The following line should not be required but it is,
        // through to Froyo.
        mapView.postInvalidateDelayed(2000);
    }
	
//listener for trackingToggleButton's events
	OnCheckedChangeListener trackingToggleButtonListener = 
			new OnCheckedChangeListener()
   {
      // called when user toggles tracking state
      @Override
      public void onCheckedChanged(CompoundButton buttonView,
         boolean isChecked)
      { 
         // if app is currently tracking
         if (!isChecked)
         {
            tracking = false; // just stopped tracking locations
         
            // compute the total time we were tracking
            long milliseconds = System.currentTimeMillis() - startTime;
            double totalHours = milliseconds / MILLISECONDS_PER_HOUR;
            
            // create a dialog displaying the results
            AlertDialog.Builder dialogBuilder = 
               new AlertDialog.Builder(MainMapActivity.this);
            dialogBuilder.setTitle(R.string.results);
      
            double distanceKM = distanceTraveled / 1000.0;
            double speedKM = distanceKM / totalHours;
            double distanceMI = distanceKM * MILES_PER_KILOMETER;
            double speedMI = distanceMI / totalHours;
            
            // display distanceTraveled traveled and average speed
            dialogBuilder.setMessage(String.format(
               getResources().getString(R.string.results_format), 
               distanceKM, distanceMI, speedKM, speedMI));
            dialogBuilder.setPositiveButton(
               R.string.button_ok, null);
            dialogBuilder.show(); // display the dialog
         } // end if
         else  
         {   
            tracking = true; // app is now tracking
            startTime = System.currentTimeMillis(); // get current time
            routeOverlay.reset(); // reset for new route
            bearingFrameLayout.invalidate(); // clear the route
            previousLocation = null; // starting a new route
         } // end else
      } // end method onCheckChanged
   }; // end anonymous inner class   
   
   @Override 
	public void onResume()
	{
		super.onResume();
		whereAmI.enableMyLocation();
		whereAmI.runOnFirstFix(new Runnable() {
			public void run() {
				mapController.setCenter(whereAmI.getMyLocation());
			}
		}
				);
			
	}

	@Override
	public void onPause()
	{
		super.onPause();
		whereAmI.disableMyLocation();
	}
	
	@Override
	protected boolean isLocationDisplayed() {
		// TODO Auto-generated method stub
		return whereAmI.isMyLocationEnabled();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	

}