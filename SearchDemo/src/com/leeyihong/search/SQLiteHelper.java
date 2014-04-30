package com.leeyihong.search;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private static String DB_PATH = "/data/data/com.leeyihong.search/databases/";
    private static String DB_NAME = "db_test"; //itinerary
    
    private SQLiteDatabase myDatabase; 
 
    private final Context myContext;
    
    private String[] allColumns = { COLUMN_ID, COLUMN_POI, COLUMN_CATEGORY, COLUMN_SUBCATEGORY, COLUMN_RATING };
	 
	
	private static final String DATABASE_NAME = "commments.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_ITINEARY = "itinerary";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_POI = "poi";
	public static final String COLUMN_CATEGORY = "category";//Might be ARRAY
	public static final String COLUMN_SUBCATEGORY = "subcategory";//Might be ARRAY
	public static final String COLUMN_RATING = "rating";	// 0 - 5
	public static final String COLUMN_IMAGE = "image";		//Do note that images had been download to phone. Might be ARRAY
	
	public static final String COLUMN_GENERAL_LOCATION = "general location";	// estimate area
	public static final String COLUMN_LAST_MOTIFIED = "lastModified";
	public static final String COLUMN_CREATED_DATE = "dateCreated";

	//DB DATA includes
	/*
	 * ID
	 * Name
	 * Category
	 * Rating => 1-5
	 * Image
	 * 
	 * FUTURE IMPROVEMENTs
	 * General Location
	 * Date updated
	 * Date created
	 */
	
	public SQLiteHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
	
	public void createDataBase() throws IOException{
		 
    	boolean dbExist = checkDataBase();
    	Log.i("", "create data base ********");
    	if(dbExist){
    		Log.i("SQLiteHelper", "Database exist");
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
 
    }
	
	private boolean checkDataBase(){
    	Log.i("", "check data base ********");
		 
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    	}catch(SQLiteException e){
    		Log.e("SQLiteHelper", "Database doesn't exist");
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
 
    	return checkDB != null ? true : false;
    }
	
	private void copyDataBase() throws IOException{
    	Log.i("", "copy data base ********");
		 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//CLOSE STREAMS 
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }

    public void openDataBase() throws SQLException{
    	Log.i("", "open data base ********");
        String myPath = DB_PATH + DB_NAME;
    	myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    
    @Override
	public synchronized void close() {
    	Log.i("", "close data base ********");
    	if(myDatabase != null)
    		myDatabase.close();
    	
    	super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	
	public Cursor getAllRecord() throws SQLException{
	   Cursor cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_ITINEARY, null);
	   
	   return cursor;
	}
	
	public List<Itinerary> getAllItineraryList() {
	    List<Itinerary> itineraryList = new ArrayList<Itinerary>();
	    String selectQuery = "SELECT  * FROM " + TABLE_ITINEARY + " LIMIT 1";
	    //LIMIT <count> OFFSET <skip>
	    //LIMIT <skip>, <count>
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = myDatabase.query(SQLiteHelper.TABLE_ITINEARY, allColumns, null, null, null, null, null);
	    //Cursor cursor = myDatabase.rawQuery(selectQuery, null);
	 
	    if (cursor.moveToFirst()) {
//	    	try{
	    	
		    	do {
		        	Itinerary itinerary = new Itinerary();
		        	itinerary.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
		        	itinerary.setPoi(cursor.getString(cursor.getColumnIndex(COLUMN_POI)));
		        	itinerary.setRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RATING))));
		        	itinerary.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
		        	itinerary.setSubCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SUBCATEGORY)));
		        	//itinerary.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
		        	
		            itineraryList.add(itinerary);
		            Log.i("Info","TEST:  " + itinerary.getPoi());
		    	}while(cursor.moveToNext());
//		    	cursor.close();
		    	
//	    	} catch(Exception e) {
//	    		Log.e("","ERROR : " + e.toString());
//	    	
//	    	} finally {
//	    		cursor.close();
//	    	}
	    	
	    }
	    cursor.close();
	    return itineraryList;
	}
	

}
