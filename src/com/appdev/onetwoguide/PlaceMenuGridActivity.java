package com.appdev.onetwoguide;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
 
public class PlaceMenuGridActivity extends Activity {
 
	private GridviewAdapter mAdapter;
    private ArrayList<String> listCategory;
    private ArrayList<Integer> listPlaceImage;
    private ArrayList<String> searchStrings;
 
    private GridView gridView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_menu_grid);
 
        prepareList();
        
     // prepared arraylist and passed it to the Adapter class
        mAdapter = new GridviewAdapter(this,listCategory, listPlaceImage);
        // set textview attribute
        //TextView MenuText =(TextView) findViewById(R.id.place_menu_text);
        //MenuText.setTextSize(size)
        
     // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.place_menu_grid_view);
        gridView.setBackgroundColor(Color.LTGRAY);
        gridView.setAdapter(mAdapter);
 
//        GridView gridView = (GridView) findViewById(R.id.place_menu_grid_view);
//        
//        final ImageAdapter ia =new ImageAdapter(this);
//        // Instance of ImageAdapter Class
//        gridView.setAdapter(ia);
//        registerForContextMenu(gridView);
    
    
    /**
     * On Click event for Single Gridview Item
     * */
        gridView.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
        	
        	Intent i = new Intent(getApplicationContext(),NearPlaceActivity.class);
        	 
////         i.putExtra("user_longitude", Double.toString(gps.getLongitude()));
        	
        	
        	//Bundle extras =new Bundle();
            //Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
            // passing array index
        	String search = searchStrings.get(position);
        	i.putExtra("category", search);
        	//extras.putString("category",search);
            //i.putExtras(extras);
            //startActivity(i);
            //setResult(RESULT_OK,i);
            //finish();
        	startActivity(i);
        }
        }
        );
    }
    
    // add place image and text here
    public void prepareList(){
    	listCategory =new ArrayList<String>();
    	listCategory.add("Museum");
    	listCategory.add("Park");
    	listCategory.add("Art Gallery");
    	listCategory.add("Amusement Park");
    	listCategory.add("Stadium");
    	listCategory.add("Shopping Mall");
    	listCategory.add("Cafe");
    	listCategory.add("Restaurant");
    	listCategory.add("Night Club");
    	listCategory.add("Casino");   	
    	listCategory.add("ATM");
    	listCategory.add("Bank");
    	listCategory.add("Car Rental");
    	listCategory.add("Train Station");
    	listCategory.add("Bus Station");
    	
    	listPlaceImage = new ArrayList<Integer>();
    	listPlaceImage.add(R.drawable.ic_museum);
    	listPlaceImage.add(R.drawable.ic_park);
    	listPlaceImage.add(R.drawable.ic_art_gallery);
    	listPlaceImage.add(R.drawable.ic_amusement_park);
    	listPlaceImage.add(R.drawable.ic_stadium);
    	listPlaceImage.add(R.drawable.ic_shopping_mall);
    	listPlaceImage.add(R.drawable.ic_cafe);
    	listPlaceImage.add(R.drawable.ic_restaurant);
    	listPlaceImage.add(R.drawable.ic_night_club);
    	listPlaceImage.add(R.drawable.ic_casino);
    	listPlaceImage.add(R.drawable.ic_atm);
    	listPlaceImage.add(R.drawable.ic_bank);
    	listPlaceImage.add(R.drawable.ic_car_rental);
    	listPlaceImage.add(R.drawable.ic_train_station);
    	listPlaceImage.add(R.drawable.ic_bus_station);
    	
    	searchStrings = new ArrayList<String>();
    	searchStrings.add("museum");
    	searchStrings.add("park");
    	searchStrings.add("art_gallery");
    	searchStrings.add("amusement_park");
    	searchStrings.add("stadium");
    	searchStrings.add("shopping_mall");
    	searchStrings.add("cafe");
    	searchStrings.add("restaurant");
    	searchStrings.add("night_club");
    	searchStrings.add("casino");
    	searchStrings.add("atm");
    	searchStrings.add("bank");
    	searchStrings.add("car_rental");
    	searchStrings.add("train_station");
    	searchStrings.add("bus_station");
    	
    	
    }
}