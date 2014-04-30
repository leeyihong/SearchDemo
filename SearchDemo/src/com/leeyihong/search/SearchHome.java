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
	Spinner category_spinner;
	SQLiteHelper myDbHelper;
	
	public static final String[] DAYS_OPTIONS  = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_home);
		
		category_spinner = (Spinner) findViewById(R.id.category_spinner);
		
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
        
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (SearchHome.this, android.R.layout.simple_spinner_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);
        

        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_home, menu);
		return true;
	}

}
