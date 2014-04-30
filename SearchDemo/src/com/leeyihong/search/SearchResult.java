package com.leeyihong.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResult extends Activity {

	float screenWidth = 0;
	int imageHeightPixel = 0;
	ItineraryListAdapter itineraryListAdapter;
	ListView itinerary_list;
	ProgressDialog pd;
	SQLiteHelper myDbHelper;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
 
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        itinerary_list = (ListView) findViewById(R.id.itinerary_list);
        getImageWidthDimension();
        
        myDbHelper = new SQLiteHelper(this);
        try {
        	myDbHelper.createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
        
        handleIntent(getIntent());
    }	

	public void getImageWidthDimension(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = (int) metrics.widthPixels;
		
		imageHeightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150 , getResources().getDisplayMetrics());
	}

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
 
            try {
    	 		myDbHelper.openDataBase();
    	 		
    			pd = ProgressDialog.show(SearchResult.this, "","Loading...", true, true);

        		List<Itinerary> iti = myDbHelper.searchByName(query);
                itineraryListAdapter = new ItineraryListAdapter(new ArrayList<Itinerary>(iti));
                itinerary_list.setAdapter(itineraryListAdapter);
                itineraryListAdapter.notifyDataSetChanged();
                
                pd.dismiss();
    	 	}catch(SQLException sqle){
    	 		throw sqle;
    	 	}
        }
    }

    //TODO To Create a Combine Class with Search Home
	public class ItineraryListAdapter extends BaseAdapter {
		ArrayList<Itinerary> itinerary_list;

		public ItineraryListAdapter(ArrayList<Itinerary> itinerary_list) {
			this.itinerary_list = itinerary_list;
		}

		@Override
		public int getCount() {
			return itinerary_list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public Bitmap resizeBitmap(Bitmap originalBitmap){
			int width = originalBitmap.getWidth();
		    int height = originalBitmap.getHeight();
		    float scaleWidth = screenWidth / width;
		    int newHeight = (int) (height * scaleWidth);
		    
		    Matrix matrix = new Matrix();
		    matrix.postScale(scaleWidth, scaleWidth);
		    
		    Bitmap scaledBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, false);
		    
		    if(newHeight < imageHeightPixel) {
		    	return scaledBitmap;
		    } else {
		    	return Bitmap.createBitmap(scaledBitmap, 0, 0, (int) screenWidth, imageHeightPixel);
		    }
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = getLayoutInflater().inflate(R.layout.itinerary_sublist, null);
			TextView categories_text = (TextView) v.findViewById(R.id.categories_text);
			TextView poi_text = (TextView) v.findViewById(R.id.poi_text);
			TextView location_text = (TextView) v.findViewById(R.id.location_text);
			ImageView itinerary_img = (ImageView) v.findViewById(R.id.itinerary_img);
			ImageView rating_img = (ImageView) v.findViewById(R.id.rating_img);
			
			if (itinerary_list.size() > 0) {
				itinerary_img.setImageBitmap(resizeBitmap(BitmapFactory.decodeByteArray(itinerary_list.get(position).image, 0, itinerary_list.get(position).image.length)));
				
				poi_text.setText(itinerary_list.get(position).getPoi());
				if(itinerary_list.get(position).getSubCategory().equalsIgnoreCase("null")){
					categories_text.setText(itinerary_list.get(position).getCategory());
				} else {
					categories_text.setText(itinerary_list.get(position).getCategory() + "   " +itinerary_list.get(position).getSubCategory() );
				}
				location_text.setText(itinerary_list.get(position).getLocation());
				
				switch (itinerary_list.get(position).getRating()){
				case 1:
					rating_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.one_star));
					break;
				case 2:
					rating_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.two_star));
					break;
				case 3:
					rating_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.three_star));
					break;
				case 4:
					rating_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.four_star));
					break;
				case 5:
				default:
					rating_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.five_star));
					break;
				}
				
			}
			return v;
		}
		
	}
}
