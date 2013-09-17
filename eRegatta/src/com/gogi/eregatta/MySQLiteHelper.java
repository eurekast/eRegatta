package com.gogi.eregatta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import com.google.api.services.bookingendpoint.model.Booking;
import com.google.api.services.regattaclassendpoint.model.RegattaClass;
import com.google.api.services.regattaendpoint.model.Regatta;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_REGATTAS = "regattas";
	public static final String COLUMN_ID = "_id";
	
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_COUNTRY = "country";
	public static final String COLUMN_STARTDATE = "startDate";
	public static final String COLUMN_ENDDATE = "endDate";
	public static final String COLUMN_ENTRYADDRESS = "entryAddress";
	public static final String COLUMN_VENUE = "venue";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_ENTRYNAME = "entryName";
	public static final String COLUMN_FAX = "fax";
	public static final String COLUMN_TELEPHONE = "telephone";
	public static final String COLUMN_WEBSITE = "website";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String[] ALL_REGATTA_COLUMNS = {
		COLUMN_ID, COLUMN_NAME, COLUMN_COUNTRY, COLUMN_STARTDATE, COLUMN_ENDDATE, COLUMN_ENTRYADDRESS, COLUMN_VENUE, COLUMN_EMAIL,
		COLUMN_ENTRYNAME, COLUMN_FAX, COLUMN_TELEPHONE, COLUMN_WEBSITE, COLUMN_LATITUDE, COLUMN_LONGITUDE};
	
	public static final String TABLE_CLASSES = "classes";
	public static final String COLUMN_REGATTAID = "regattaId";
	public static final String COLUMN_REGATTACLASS = "regattaClass";
	public static final String COLUMN_DISCIPLINE = "discipline";
	public static final String COLUMN_GRADE = "grade";
	public static final String COLUMN_TYPE = "type";
	
	public static final String TABLE_BOOKINGS = "bookings";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_URL = "url";
	
	public static final String TABLE_DAILYWEATHER = "dailyWeather";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_ICON = "icon";
	public static final String COLUMN_TEMPERATUREMAX = "temperatureMin";
	public static final String COLUMN_TEMPERATUREMIN = "temperatureMax";
	public static final String COLUMN_WINDSPEED = "windSpeed";
	public static final String COLUMN_WINDDIR = "windDir";
	public static final String COLUMN_MM = "mm";
	public static final String COLUMN_REFRESHED = "refreshed";
	
	public static final String TABLE_HOURLYWEATHER = "hourlyWeather";
	public static final String COLUMN_TEMPERATURE = "temperature";
	
	public static final String TABLE_REFRESH ="refresh";
	public static final String COLUMN_REFRESHTIME = "refreshTime";
	public static final int VALUE_REFRESH_ID = 1;
	
	public static final String FIELD_NAME = COLUMN_NAME;
	public static final String FIELD_VENUE = COLUMN_VENUE;
	public static final String FIELD_COUNTRY = COLUMN_COUNTRY;
	public static final String FIELD_DISCIPLINE = COLUMN_DISCIPLINE;
	public static final String FIELD_TYPE = COLUMN_TYPE;
	public static final String FIELD_REGATTACLASS = COLUMN_REGATTACLASS;
	public static final String FIELD_GRADE = COLUMN_GRADE;
	public static final String FIELD_MONTH = "month";
	
	
	public static final String[] ALL_REGATTA_FIELDS = {FIELD_NAME, FIELD_VENUE, FIELD_COUNTRY, FIELD_MONTH};
	public static final String[] ALL_REGATTACLASS_FIELDS = {FIELD_DISCIPLINE, FIELD_TYPE, FIELD_REGATTACLASS, FIELD_GRADE};


	private static final String DATABASE_NAME = "eregatta.db";
	private static final int DATABASE_VERSION = 11;
	
	

	// Database creation sql statement
	private static final String REGATTA_CREATE = "create table "
	    + TABLE_REGATTAS + "("
	    	+ COLUMN_ID + " integer primary key, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_COUNTRY + " text not null, "
			+ COLUMN_STARTDATE + " text not null, "
			+ COLUMN_ENDDATE + " text not null, "
			+ COLUMN_ENTRYADDRESS + " text, "
			+ COLUMN_VENUE + " text, "
			+ COLUMN_EMAIL + " text, "
			+ COLUMN_ENTRYNAME + " text, "
			+ COLUMN_FAX + " fax, "
			+ COLUMN_TELEPHONE + " text, "
			+ COLUMN_WEBSITE + " text, "
			+ COLUMN_LATITUDE + " real, "
			+ COLUMN_LONGITUDE + " real);";
	
	private static final String REGATTACLASS_CREATE = "create table "
		    + TABLE_CLASSES + "("
		    	+ COLUMN_ID + " integer primary key autoincrement, "
		    	+ COLUMN_REGATTAID + " integer, "
		    	+ COLUMN_REGATTACLASS + " text, "
		    	+ COLUMN_DISCIPLINE + " text, "
				+ COLUMN_GRADE + " text, "
				+ COLUMN_TYPE + " text);";
	
	private static final String BOOKING_CREATE = "create table "
		    + TABLE_BOOKINGS + "("
		    	+ COLUMN_ID + " integer primary key, "
		    	+ COLUMN_PRICE + " text, "
				+ COLUMN_URL + " text);";
	
	private static final String DAILYWEATHER_CREATE = "create table "
		    + TABLE_DAILYWEATHER+ "("
		    	+ COLUMN_ID + " integer primary key autoincrement, "
		    	+ COLUMN_REGATTAID + " integer, "
				+ COLUMN_TIME + " integer not null, "
				+ COLUMN_ICON + " text not null, "
				+ COLUMN_TEMPERATUREMIN + " real not null, "
				+ COLUMN_TEMPERATUREMAX + " real not null, "
				+ COLUMN_WINDSPEED + " real not null, "
				+ COLUMN_WINDDIR + " integer not null, "
				+ COLUMN_MM + " real not null, "
				+ COLUMN_REFRESHED + " text not null);";
	
	private static final String HOURLYWEATHER_CREATE = "create table "
		    + TABLE_HOURLYWEATHER+ "("
		    	+ COLUMN_ID + " integer primary key, "
		    	+ COLUMN_REGATTAID + " integer, "
				+ COLUMN_TIME + " integer not null, "
				+ COLUMN_ICON + " text not null, "
				+ COLUMN_TEMPERATURE + " real not null, "
				+ COLUMN_WINDSPEED + " real not null, "
				+ COLUMN_WINDDIR + " integer not null, "
				+ COLUMN_MM + " real not null, "
				+ COLUMN_REFRESHED + " text not null);";
	
	private static final String REFRESH_CREATE = "create table "
		    + TABLE_REFRESH + "("
		    	+ COLUMN_ID + " integer primary key, "
		    	+ COLUMN_REFRESHTIME + " text not null);";
				

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(REGATTA_CREATE);
		database.execSQL(REGATTACLASS_CREATE);
		database.execSQL(BOOKING_CREATE);
		database.execSQL(DAILYWEATHER_CREATE);
		database.execSQL(HOURLYWEATHER_CREATE);
		database.execSQL(REFRESH_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
	        + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGATTAS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILYWEATHER);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOURLYWEATHER);
	    onCreate(db);
	}
	
	public void refreshRegattas(ArrayList<Regatta> regattas)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_REGATTAS, null, null);
		
		Iterator<Regatta> regattaIterator = regattas.iterator();
    	while (regattaIterator.hasNext()){
    		Regatta regatta = (Regatta) regattaIterator.next();
			if (regatta != null){
				ContentValues values = new ContentValues();
				values.put(COLUMN_ID, regatta.getId());
			    values.put(COLUMN_NAME, regatta.getName());
			    values.put(COLUMN_COUNTRY, regatta.getCountry());
			    values.put(COLUMN_STARTDATE, regatta.getStartDate());
			    values.put(COLUMN_ENDDATE, regatta.getEndDate());
			    values.put(COLUMN_ENTRYADDRESS, regatta.getEntryAddress());
			    values.put(COLUMN_VENUE, regatta.getVenue());
			    values.put(COLUMN_EMAIL, regatta.getEmail());
			    values.put(COLUMN_ENTRYNAME, regatta.getEntryName());
			    values.put(COLUMN_FAX, regatta.getFax());
			    values.put(COLUMN_TELEPHONE, regatta.getTelephone());
			    values.put(COLUMN_WEBSITE, regatta.getWebsite());
			    values.put(COLUMN_LATITUDE, regatta.getLatitude());
			    values.put(COLUMN_LONGITUDE, regatta.getLongitude());
			    
			    db.insert(TABLE_REGATTAS, null, values);			
		    }
    	}
		
		db.close(); // Closing database connection
		setRefresh();
	}
	
	public boolean isDatabaseEmpty() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_REGATTAS;
		
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
        	int count = cursor.getInt(0);
        	Log.d("DEBUG", "Num of regattas in database: "+count);
        	cursor.close();
        	db.close();
        	if (count>0){
	        	return false;
        	}
	        else{
	        	return true;
	        }
        }
        else{
        	cursor.close();
        	db.close();
        	return true;
        }
	}
	
	public void refreshRegattaClasses(ArrayList<RegattaClass> regattaClasses) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CLASSES, null, null);
		
		Iterator<RegattaClass> regattaClassIterator = regattaClasses.iterator();
    	while (regattaClassIterator.hasNext()){
    		RegattaClass regattaClass = (RegattaClass) regattaClassIterator.next();
			if (regattaClass != null){
				ContentValues values = new ContentValues();
			    values.put(COLUMN_REGATTAID, regattaClass.getRegattaId());
			    values.put(COLUMN_REGATTACLASS, regattaClass.getRegattaClass());
			    values.put(COLUMN_DISCIPLINE, regattaClass.getDiscipline());
			    values.put(COLUMN_GRADE, regattaClass.getGrade());
			    values.put(COLUMN_TYPE, regattaClass.getType());

			    db.insert(TABLE_CLASSES, null, values);			}
    	}
		db.close(); // Closing database connection		
	}
	
	public void refreshBookings(ArrayList<Booking> bookings) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BOOKINGS, null, null);
		
		Iterator<Booking> bookingIterator = bookings.iterator();
    	while (bookingIterator.hasNext()){
    		Booking booking = (Booking) bookingIterator.next();
			if (booking != null){
				ContentValues values = new ContentValues();
			    values.put(COLUMN_ID, booking.getId());
			    values.put(COLUMN_PRICE, booking.getPrice());
			    values.put(COLUMN_URL, booking.getUrl());
			    db.insert(TABLE_BOOKINGS, null, values);			}
    	}
		db.close(); // Closing database connection		
	}
	
	public void addRegatta(Regatta regatta) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_ID, regatta.getId());
	    values.put(COLUMN_NAME, regatta.getName());
	    values.put(COLUMN_COUNTRY, regatta.getCountry());
	    values.put(COLUMN_STARTDATE, regatta.getStartDate());
	    values.put(COLUMN_ENDDATE, regatta.getEndDate());
	    values.put(COLUMN_ENTRYADDRESS, regatta.getEntryAddress());
	    values.put(COLUMN_VENUE, regatta.getVenue());
	    values.put(COLUMN_EMAIL, regatta.getEmail());
	    values.put(COLUMN_ENTRYNAME, regatta.getEntryName());
	    values.put(COLUMN_FAX, regatta.getFax());
	    values.put(COLUMN_TELEPHONE, regatta.getTelephone());
	    values.put(COLUMN_WEBSITE, regatta.getWebsite());
	    values.put(COLUMN_LATITUDE, regatta.getLatitude());
	    values.put(COLUMN_LONGITUDE, regatta.getLongitude());
	    
	    // Inserting Row
	    String[] WHERE_ARGS = {regatta.getId().toString()};
	    if (db.update(TABLE_REGATTAS, values, COLUMN_ID + " = ?", WHERE_ARGS)==0){
	    	db.insert(TABLE_REGATTAS, null, values);
	    }
	    
	    db.close(); // Closing database connection
	}
	
	
	public List<Regatta> getAllRegattas() {
        List<Regatta> regattaList = new ArrayList<Regatta>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REGATTAS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("DEBUG", "Num of regatta columns: " + cursor.getColumnCount());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Regatta regatta = new Regatta();
                regatta.setId(Integer.parseInt(cursor.getString(0)));
    			regatta.setName(cursor.getString(1));
    			regatta.setCountry(cursor.getString(2));
    			regatta.setStartDate(cursor.getString(3));
    			regatta.setEndDate(cursor.getString(4));
    			regatta.setEntryAddress(cursor.getString(5));
    			regatta.setVenue(cursor.getString(6));
    			regatta.setEmail(cursor.getString(7));
    			regatta.setEntryName(cursor.getString(8));
    			regatta.setFax(cursor.getString(9));
    			regatta.setTelephone(cursor.getString(10));
    			regatta.setWebsite(cursor.getString(11));
    			regatta.setLatitude(Float.valueOf(cursor.getFloat(12)));
    			regatta.setLongitude(Float.valueOf(cursor.getFloat(13)));
    			
                // Adding regatta to list
                regattaList.add(regatta);
            } while (cursor.moveToNext());
        }
        db.close();
        // return regatta list
        return regattaList;
    }
	
	public Regatta getRegatta(int id) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REGATTAS + " WHERE "+ COLUMN_ID +" = "+id;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("DEBUG", "Num of regatta columns: " + cursor.getColumnCount());
        // looping through all rows and adding to list
        Regatta regatta = null;
        if (cursor.moveToFirst()) {
        	regatta = new Regatta();
            regatta.setId(Integer.parseInt(cursor.getString(0)));
			regatta.setName(cursor.getString(1));
			regatta.setCountry(cursor.getString(2));
			regatta.setStartDate(cursor.getString(3));
			regatta.setEndDate(cursor.getString(4));
			regatta.setEntryAddress(cursor.getString(5));
			regatta.setVenue(cursor.getString(6));
			regatta.setEmail(cursor.getString(7));
			regatta.setEntryName(cursor.getString(8));
			regatta.setFax(cursor.getString(9));
			regatta.setTelephone(cursor.getString(10));
			regatta.setWebsite(cursor.getString(11));
			regatta.setLatitude(Float.valueOf(cursor.getFloat(12)));
			regatta.setLongitude(Float.valueOf(cursor.getFloat(13)));
        }
        // return regatta list
        db.close();
        return regatta;
    }
	
	public void setRefresh(){
		DateFormat dateFormat = new SimpleDateFormat("d.M.yyyy");
		DateFormat timeFormat = new SimpleDateFormat("H:mm:ss");
		Date date = new Date();
		
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_ID, VALUE_REFRESH_ID);
	    values.put(COLUMN_REFRESHTIME, dateFormat.format(date)+" at "+timeFormat.format(date));
	    
	    // Inserting Row
	    String[] WHERE_ARGS = {Integer.toString(VALUE_REFRESH_ID)};
	    if (db.update(TABLE_REFRESH, values, COLUMN_ID + " = ?", WHERE_ARGS)==0){
	    	db.insert(TABLE_REFRESH, null, values);
	    }
	    
	    db.close(); // Closing database connection
	}
	
	public String getRefresh() {
        // Select All Query
        String selectQuery = "SELECT "+COLUMN_REFRESHTIME +" FROM " + TABLE_REFRESH + " WHERE "+ COLUMN_ID +" = "+VALUE_REFRESH_ID;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String refreshDate = "never";
        if (cursor.moveToFirst()) {
        	refreshDate = cursor.getString(0);
        }
        // return regatta list
        db.close();
        return refreshDate;
    }
	
	public String[] getRegattaClasses(int id) {
        // Select All Query
        String selectQuery = "SELECT DISTINCT "+COLUMN_REGATTACLASS+" FROM " + TABLE_CLASSES + " WHERE "+ COLUMN_REGATTAID +" = "+id;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("DEBUG", "Num of regatta columns: " + cursor.getColumnCount());
        // looping through all rows and adding to list
        String[] regattaClasses = new String[cursor.getCount()];
        int rowCount = 0;
        if (cursor.moveToFirst()) {
	        do {
	        	regattaClasses[rowCount] = cursor.getString(0);
	        	rowCount++;
	        } while (cursor.moveToNext());
        }
        // return regatta list
        db.close();
        return regattaClasses;
    }
	public String[] getDisciplines(int id) {
        // Select All Query
        String selectQuery = "SELECT DISTINCT "+COLUMN_DISCIPLINE+" FROM " + TABLE_CLASSES + " WHERE "+ COLUMN_REGATTAID +" = "+id;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("DEBUG", "Num of regatta columns: " + cursor.getColumnCount());
        // looping through all rows and adding to list
        String[] regattaDisciplines = new String[cursor.getCount()];
        int rowCount = 0;
        if (cursor.moveToFirst()) {
	        do {
	        	regattaDisciplines[rowCount]=cursor.getString(0);
	        	rowCount++;
	        } while (cursor.moveToNext());
        }
        // return regatta list
        db.close();
        return regattaDisciplines;
    }
	public String[] getGrades(int id) {
        // Select All Query
        String selectQuery = "SELECT DISTINCT "+COLUMN_GRADE+" FROM " + TABLE_CLASSES + " WHERE "+ COLUMN_REGATTAID +" = "+id;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("DEBUG", "Num of regatta columns: " + cursor.getColumnCount());
        // looping through all rows and adding to list
        String[] regattaGrades = new String[cursor.getCount()];
        int rowCount = 0;
        if (cursor.moveToFirst()) {
	        do {
	        	regattaGrades[rowCount]=cursor.getString(0);
	        	rowCount++;
	        } while (cursor.moveToNext());
        }
        // return regatta list
        db.close();
        return regattaGrades;
    }
	public String[] getTypes(int id) {
        // Select All Query
        String selectQuery = "SELECT DISTINCT "+COLUMN_TYPE+" FROM " + TABLE_CLASSES + " WHERE "+ COLUMN_REGATTAID +" = "+id;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("DEBUG", "Num of regatta columns: " + cursor.getColumnCount());
        // looping through all rows and adding to list
        String[] regattaTypes = new String[cursor.getCount()];
        int rowCount = 0;
        if (cursor.moveToFirst()) {
	        do {
	        	regattaTypes[rowCount]=cursor.getString(0);
	        	rowCount++;
	        } while (cursor.moveToNext());
        }
        db.close();
        // return regatta list
        return regattaTypes;
    }	
	public int[] searchRegattas(ArrayList<HashMap<String, String>> params) {

		HashMap<String, String> regattaParams = params.get(0);
		
    	
        // Select All Query
        
        String selectRegattaClassQuery = "SELECT " + COLUMN_REGATTAID +" FROM " + TABLE_CLASSES;
        
        HashMap<String, String> regattaClassParams = params.get(1);
        int validFields = 0;
        for (int i = 0; i<ALL_REGATTACLASS_FIELDS.length; i++){
        	String field = ALL_REGATTACLASS_FIELDS[i];
        	String value = regattaClassParams.get(field);
        	
        	//èe je bilo v searchu kaj za ta stolpec
        	if(value != null && value!="" && !value.isEmpty()){
        		validFields += 1;
        		// += regattaClass = J/24
        		if (validFields == 1){
        			selectRegattaClassQuery += " WHERE";
        		}
        		else {
        			selectRegattaClassQuery+=" AND";
        		}
        		
        		selectRegattaClassQuery+=" "+ALL_REGATTACLASS_FIELDS[i] + " = '" + value +"'";
        		
        	}
        }
        Log.d("DEBUG", "query end: " + selectRegattaClassQuery);
        
        boolean noClassFilters = false;
        if (validFields == 0){
        	noClassFilters = true;
        }
        String selectRegattaQuery = "SELECT " + COLUMN_ID + " FROM " + TABLE_REGATTAS;
        if (!noClassFilters){
        	selectRegattaQuery += " WHERE " + COLUMN_ID + " IN ("+selectRegattaClassQuery+")";
        }
        // doda WHERE stavek glede na vnose v search
        validFields = 0;
        for (int i = 0; i<ALL_REGATTA_FIELDS.length; i++){
        	String field = ALL_REGATTA_FIELDS[i];
        	String value = regattaParams.get(field);
        	
        	//èe je bilo v searchu kaj za ta stolpec
        	if(value != null && value!="" && !value.isEmpty()){
        		validFields += 1;
        		// += regattaClass = J/24
        		if (noClassFilters && validFields == 1){
        			selectRegattaQuery += " WHERE";
        		}
        		else {
        			selectRegattaQuery+=" AND";
        		}
        		
        		if (field == FIELD_NAME || field == FIELD_VENUE){
        			value=value.replaceAll("'","''");        			
        			selectRegattaQuery+=" "+field+" LIKE '%"+value+"%'";
        			Log.d("DEBUG", "query: " + selectRegattaQuery);
        		}
        		else if (field == FIELD_MONTH){
        			String oldDateString = value;
        			final String OLD_FORMAT = "MMMMM yyyy";
        			final String NEW_FORMAT = "yyyy-MM";

        			// August 12, 2010
        			String newDateString = "";

        			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        			try{
        				Date d = sdf.parse(oldDateString);
        				sdf.applyPattern(NEW_FORMAT);
        				newDateString = sdf.format(d);
        			}
        			catch (Exception e){
        				// obvestit o napaki
        			}
        			
        			selectRegattaQuery+=" "+COLUMN_STARTDATE+" <= '"+newDateString+"-31' AND "+COLUMN_ENDDATE+ " >= '" + newDateString + "-01'" ;
        			Log.d("DEBUG", "after date: "+ selectRegattaQuery);
        			
        			
        			// tuki spremenit month v datume in omejit search
        			
        		}
        		else if (field == FIELD_COUNTRY){
        			String[] countryAndTag = value.split(" - ");
        			String country = countryAndTag[1];
        			selectRegattaQuery+=" "+field + " = '" + country +"'";
        		}
        	}
        }
        selectRegattaQuery+= " ORDER BY "+COLUMN_STARTDATE;
        Log.d("DEBUG", "query: " + selectRegattaQuery);
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        ArrayList<Integer> ar = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery(selectRegattaQuery, null);
        Log.d("DEBUG", "Num of results: " + cursor.getCount());
        // looping through all rows and adding to list
        
        int[] result = new int[cursor.getCount()];
        int rowCount = 0;
        if (cursor.moveToFirst()) {
            do {
        		int id = cursor.getInt(0);
        		result[rowCount] = id;
            	
            	rowCount++;
            	Log.d("DEBUG",""+cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        db.close();
        // return regatta list
        return result;
    }
	
	public Booking getBooking(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_BOOKINGS, new String[] { COLUMN_ID,
                COLUMN_PRICE, COLUMN_URL }, COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        
        if (cursor.getCount()>0){
        	 cursor.moveToFirst();
        	 Booking booking = new Booking();
             booking.setId(cursor.getInt(0));
             booking.setPrice(cursor.getString(1));
             booking.setUrl(cursor.getString(2));
             
             return booking;
        }
        return null;
        
        
    }
	
	public void addRegattaClass(RegattaClass regattaClass) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_REGATTAID, regattaClass.getRegattaId());
	    values.put(COLUMN_REGATTACLASS, regattaClass.getRegattaClass());
	    values.put(COLUMN_DISCIPLINE, regattaClass.getDiscipline());
	    values.put(COLUMN_GRADE, regattaClass.getGrade());
	    values.put(COLUMN_TYPE, regattaClass.getType());
	 
	    // Inserting Row
	    String WHERE_CLAUSE = 
    		COLUMN_REGATTAID + " = ? AND " +
    		COLUMN_REGATTACLASS + " = ? AND " +
    		COLUMN_DISCIPLINE + " = ? AND " +
    		COLUMN_TYPE + " = ?" ;
	    String[] WHERE_ARGS = {
	    	regattaClass.getRegattaId(),
	    	regattaClass.getRegattaClass(),
	    	regattaClass.getDiscipline(),
	    	regattaClass.getType()
	    };
	    if (db.update(TABLE_CLASSES, values, WHERE_CLAUSE, WHERE_ARGS)==0){
	    	db.insert(TABLE_CLASSES, null, values);
	    }
	    db.close(); // Closing database connection
	}
	
	
	
	public void addBooking(Booking booking) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_ID, booking.getId());
	    values.put(COLUMN_PRICE, booking.getPrice());
	    values.put(COLUMN_URL, booking.getUrl());
	 
	    // Inserting Row
	    String[] WHERE_ARGS = {booking.getId().toString()};
	    if (db.update(TABLE_BOOKINGS, values, COLUMN_ID+" = ?", WHERE_ARGS)==0){
	    	db.insert(TABLE_BOOKINGS, null, values);
	    }
	    db.close(); // Closing database connection
	}
	
	public void addDailyWeather(ArrayList<DailyWeather> dailyWeatherArray) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    if (dailyWeatherArray != null){
		    // zbriše vreme za tisto regato, ki jo refreshaš
		    String[] DELETE_ARGS = {Integer.toString(dailyWeatherArray.get(0).regattaId)};
		    db.delete(TABLE_DAILYWEATHER, COLUMN_REGATTAID + " = ?", DELETE_ARGS);
		 
		    for(int i = 0; i<dailyWeatherArray.size(); i++){
		    	DailyWeather dailyWeather = dailyWeatherArray.get(i);
		    	ContentValues values = new ContentValues();
		    	Date refreshTime = new Date();
		    	
			    values.put(COLUMN_REGATTAID, dailyWeather.regattaId);
			    values.put(COLUMN_TIME, dailyWeather.time);
			    values.put(COLUMN_ICON, dailyWeather.icon);
			    values.put(COLUMN_TEMPERATUREMIN, dailyWeather.temperatureMin);
			    values.put(COLUMN_TEMPERATUREMAX, dailyWeather.temperatureMax);
			    values.put(COLUMN_WINDSPEED, dailyWeather.windSpeed);
			    values.put(COLUMN_WINDDIR, dailyWeather.windDir);
			    values.put(COLUMN_MM, dailyWeather.mm);
			    values.put(COLUMN_REFRESHED, Long.toString(refreshTime.getTime()));
			    
			    // Inserting Row
		    	db.insert(TABLE_DAILYWEATHER, null, values);	    	
		    }
	    }
	    db.close(); // Closing database connection*/
	}
	
	public void addHourlyWeather(ArrayList<HourlyWeather> hourlyWeatherArray) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    // zbriše vreme za tisto regato, ki jo refreshaš
	    if (hourlyWeatherArray != null){
		    String[] DELETE_ARGS = {Integer.toString(hourlyWeatherArray.get(0).regattaId)};
		    db.delete(TABLE_HOURLYWEATHER, COLUMN_REGATTAID + " = ?", DELETE_ARGS);
		 
		    for(int i = 0; i<hourlyWeatherArray.size(); i++){
		    	HourlyWeather hourlyWeather = hourlyWeatherArray.get(i);
		    	ContentValues values = new ContentValues();
		    	Date refreshTime = new Date();
		    			
			    values.put(COLUMN_REGATTAID, hourlyWeather.regattaId);
			    values.put(COLUMN_TIME, hourlyWeather.time);
			    values.put(COLUMN_ICON, hourlyWeather.icon);
			    values.put(COLUMN_TEMPERATURE, hourlyWeather.temperature);
			    values.put(COLUMN_WINDSPEED, hourlyWeather.windSpeed);
			    values.put(COLUMN_WINDDIR, hourlyWeather.windDir);
			    values.put(COLUMN_MM, hourlyWeather.mm);
			    values.put(COLUMN_REFRESHED, Long.toString(refreshTime.getTime()));
			    
			    // Inserting Row
		    	db.insert(TABLE_HOURLYWEATHER, null, values);	    	
		    }
	    }
	    
	    db.close(); // Closing database connection*/
	}
	
	public DailyWeather[] getDailyWeather(int regattaid) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_DAILYWEATHER, new String[] {
        		COLUMN_TIME,
        		COLUMN_ICON,
        		COLUMN_TEMPERATUREMIN,
        		COLUMN_TEMPERATUREMAX,
        		COLUMN_WINDSPEED,
        		COLUMN_WINDDIR,
        		COLUMN_MM },
        		COLUMN_REGATTAID + " = ?",
                new String[] { String.valueOf(regattaid) }, null, null, COLUMN_TIME, null);
        
        DailyWeather[] dailyWeatherArray = new DailyWeather[cursor.getCount()];
        int cursorIndex = 0;
        
        if (cursor.moveToFirst()){
        	do {
	        	DailyWeather dailyWeather = new DailyWeather();
	        	dailyWeather.regattaId = regattaid;
	        	dailyWeather.time = cursor.getInt(0);
	        	dailyWeather.icon = cursor.getString(1);
	        	dailyWeather.temperatureMin = cursor.getFloat(2);
	        	dailyWeather.temperatureMax = cursor.getFloat(3);
	        	dailyWeather.windSpeed = cursor.getFloat(4);
	        	dailyWeather.windDir = cursor.getInt(5);
	        	dailyWeather.mm = cursor.getFloat(6);
	        	
	        	dailyWeatherArray[cursorIndex] = dailyWeather;
	        	cursorIndex++;
        	} while (cursor.moveToNext());

            db.close();
        	return dailyWeatherArray;
        }
        else{

            db.close();
        	return null;
        }
        
        
    }
	
	
}
