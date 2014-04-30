package com.leeyihong.search;

import java.io.IOException;
import java.util.List;

import com.leeyihong.search.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SearchHome extends Activity {

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
            for(int i = 0; i < iti.size(); i++) {
            	Log.i("info", "data" +iti.get(i).toString());
            }
            
            pd.dismiss();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}
        
	 	//ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String> (SearchHome.this, android.R.layout.simple_spinner_item, CATEGORY_OPTIONS);
	 	ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String> (SearchHome.this, R.layout.spinner_item, CATEGORY_OPTIONS);
	 	categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	category_spinner.setAdapter(categoryAdapter);
	 	
	 	//ArrayAdapter<String> locationAdapter = new ArrayAdapter<String> (SearchHome.this, android.R.layout.simple_spinner_item, LOCATION_OPTIONS);
	 	ArrayAdapter<String> locationAdapter = new ArrayAdapter<String> (SearchHome.this, R.layout.spinner_item, LOCATION_OPTIONS);
	 	locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	location_spinner.setAdapter(locationAdapter);
	 	
	 	//ArrayAdapter<String> sortingAdapter = new ArrayAdapter<String> (SearchHome.this, android.R.layout.simple_spinner_item, SORT_OPTIONS);
		ArrayAdapter<String> sortingAdapter = new ArrayAdapter<String> (SearchHome.this, R.layout.spinner_item, SORT_OPTIONS);
		sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sorting_preferences_spinner.setAdapter(sortingAdapter);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_home, menu);
		return true;
	}

}
