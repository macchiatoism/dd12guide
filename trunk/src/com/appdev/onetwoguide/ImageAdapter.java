package com.appdev.onetwoguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    // more Images later
    public Integer[] mThumbIds = {
            R.drawable.ic_museum, 
            R.drawable.ic_shopping_mall,
            R.drawable.ic_casino, 
    };
    
    public String[] placeCategories ={
    		"museum",
    		"shopping_mall",
    		"casino"
    };
    
    
    
    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
        //mInflater = LayoutInflater.from(c); 
    }
    
    // return search string
    public String getSearchString(int position){
    	return placeCategories[position];
    	
    }
 
    @Override
    public int getCount() {
        return mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	
    	View v = convertView;
    	if (v == null) {
    		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		v = li.inflate(R.layout.grid_item, null);
    		TextView tv = (TextView)v.findViewById(R.id.grid_item_text);
    		tv.setText(placeCategories[position]);
    		ImageView imageView = (ImageView)v.findViewById(R.id.grid_item_image);
    	
    	//ImageView imageView = new ImageView(mContext);
    		imageView.setImageResource(mThumbIds[position]);
    		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
    	}
    	return v;
    }
        
    
}