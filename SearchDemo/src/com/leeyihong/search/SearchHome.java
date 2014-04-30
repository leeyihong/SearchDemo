package com.leeyihong.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.leeyihong.search.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchHome extends Activity {
	
	ItineraryListAdapter itineraryListAdapter;
	ListView itinerary_list;
	ProgressDialog pd;
	Spinner category_spinner, location_spinner, sorting_preferences_spinner;
	SQLiteHelper myDbHelper;
	
	public static final String[] CATEGORY_OPTIONS  = {"ALL CATEGORY", "Food and Beverage", "Island", "Nature", "Museum", "Religion", "To-Do"};
	public static final String[] LOCATION_OPTIONS  = {"EVERYWHERE", "Central", "North", "South", "East", "West"};
	public static final String[] SORT_OPTIONS  = {"AI SORTING","Highest Rating", "Lowest Rating", "Latest Updated", "Alphabet"};	// Other possible sort Distance 
	
	
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
        
        //TODO List/Table View Itinerary
        //TODO Set Adapter Change Listeners
        
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
//            for(int i = 0; i < iti.size(); i++) {
//            	//Set Adapter Information
//            	Log.i("info", "data" +iti.get(i).toString());
//            }
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
		getMenuInflater().inflate(R.menu.search_home, menu);
		return true;
	}

	//TODO
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = getLayoutInflater().inflate(R.layout.itinerary_sublist, null);
			TextView categories_text = (TextView) v.findViewById(R.id.categories_text);
			TextView poi_text = (TextView) v.findViewById(R.id.poi_text);
			TextView location_text = (TextView) v.findViewById(R.id.location_text);
			
			if (itinerary_list.size() > 0) {
				poi_text.setText(itinerary_list.get(position).getPoi());
				if(itinerary_list.get(position).getSubCategory().equalsIgnoreCase("null")){
					categories_text.setText(itinerary_list.get(position).getCategory());
				} else {
					categories_text.setText(itinerary_list.get(position).getCategory() + " " +itinerary_list.get(position).getSubCategory() );
				}
				location_text.setText(itinerary_list.get(position).getLocation());
				
			}
			return v;
		}
		
	}
}
