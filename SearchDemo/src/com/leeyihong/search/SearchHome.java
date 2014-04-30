package com.leeyihong.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.leeyihong.search.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchHome extends Activity {
	
	float screenWidth = 0;
	int imageHeightPixel = 0;
	int lastSelectedCategory=0, lastSelectedLocation=0, lastSelectedSorting =0;
	ItineraryListAdapter itineraryListAdapter;
	ListView itinerary_list;
	ProgressDialog pd;
	Spinner category_spinner, location_spinner, sorting_preferences_spinner;
	SQLiteHelper myDbHelper;
	
	//public static final String[] CATEGORY_OPTIONS  = getResources().getStringArray(R.array.category_option);
	public static final String[] CATEGORY_OPTIONS  = {"ALL CATEGORY", "Food and Beverage", "Island", "Nature", "Museum", "Religion", "To-Do"};
	public static final String[] LOCATION_OPTIONS  = {"EVERYWHERE", "Central", "North", "South", "East", "West"};
	public static final String[] SORT_OPTIONS  = {"AI SORTING","Highest Rating", "Alphabet", "Latest Updated"};	// Other possible sort Distance 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_home);
		
		category_spinner = (Spinner) findViewById(R.id.category_spinner);
		location_spinner = (Spinner) findViewById(R.id.location_spinner);
		sorting_preferences_spinner = (Spinner) findViewById(R.id.sorting_preferences_spinner);
		itinerary_list = (ListView) findViewById(R.id.itinerary_list);
		
	 	ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String> (SearchHome.this, R.layout.spinner_item, CATEGORY_OPTIONS);
	 	categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	category_spinner.setAdapter(categoryAdapter);
	 	
	 	ArrayAdapter<String> locationAdapter = new ArrayAdapter<String> (SearchHome.this, R.layout.spinner_item, LOCATION_OPTIONS);
	 	locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	location_spinner.setAdapter(locationAdapter);
	 	
	 	ArrayAdapter<String> sortingAdapter = new ArrayAdapter<String> (SearchHome.this, R.layout.spinner_item, SORT_OPTIONS);
		sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sorting_preferences_spinner.setAdapter(sortingAdapter);
        
        getImageWidthDimension();
        
        //DATABASE QUERY
		myDbHelper = new SQLiteHelper(this);
 
        try {
        	myDbHelper.createDataBase();
        	Log.i("DB created", "Database created at Search Home");
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
        
	 	try {
	 		myDbHelper.openDataBase();
	 		
			pd = ProgressDialog.show(SearchHome.this, "","Loading...", true, true);

    		List<Itinerary> iti = myDbHelper.getAllItineraryList();
            itineraryListAdapter = new ItineraryListAdapter(new ArrayList<Itinerary>(iti));
            itinerary_list.setAdapter(itineraryListAdapter);
            itineraryListAdapter.notifyDataSetChanged();
            
            pd.dismiss();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}
	 	
	 	category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	 		@Override
	 		public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
	 			lastSelectedCategory = position;
	 			filterItinerary();
	 		}
	 		
	 		@Override
	 		public void onNothingSelected(AdapterView<?> arg0) {}
	 	});
	 	
	 	location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	 		@Override
	 		public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
	 			lastSelectedLocation = position;
	 			filterItinerary();
	 		}
	 		
	 		@Override
	 		public void onNothingSelected(AdapterView<?> arg0) {}
	 	});
	 	
	 	sorting_preferences_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				lastSelectedSorting = position;
				filterItinerary();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}

	public void getImageWidthDimension(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = (int) metrics.widthPixels;
		
		imageHeightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150 , getResources().getDisplayMetrics());
	}
	
	public void filterItinerary(){
		try {
	 		myDbHelper.openDataBase();
	 		
			pd = ProgressDialog.show(SearchHome.this, "","Loading...", true, true);

    		List<Itinerary> iti = myDbHelper.filterItineraryList(lastSelectedCategory, lastSelectedLocation, lastSelectedSorting);
            itineraryListAdapter = new ItineraryListAdapter(new ArrayList<Itinerary>(iti));
            itinerary_list.setAdapter(itineraryListAdapter);
            itineraryListAdapter.notifyDataSetChanged();
            
            pd.dismiss();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//TODO add Search Icon to search by Name
		getMenuInflater().inflate(R.menu.search_home, menu);
		return true;
	}

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
