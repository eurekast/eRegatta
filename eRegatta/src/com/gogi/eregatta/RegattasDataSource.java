package com.gogi.eregatta;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.api.services.regattaendpoint.model.Regatta;

public class RegattasDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { 
			  MySQLiteHelper.COLUMN_ID,
			  MySQLiteHelper.COLUMN_NAME,
			  MySQLiteHelper.COLUMN_COUNTRY,
			  MySQLiteHelper.COLUMN_STARTDATE,
			  MySQLiteHelper.COLUMN_ENDDATE,
			  MySQLiteHelper.COLUMN_ENTRYADDRESS,
			  MySQLiteHelper.COLUMN_VENUE,
			  MySQLiteHelper.COLUMN_EMAIL,
			  MySQLiteHelper.COLUMN_ENTRYNAME,
			  MySQLiteHelper.COLUMN_FAX,
			  MySQLiteHelper.COLUMN_TELEPHONE,
			  MySQLiteHelper.COLUMN_WEBSITE,
			  MySQLiteHelper.COLUMN_LATITUDE,
			  MySQLiteHelper.COLUMN_LONGITUDE,
			  };

	  public RegattasDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Regatta downloadRegattas() {
	    ContentValues values = new ContentValues();
	    
	    values.put(MySQLiteHelper.COLUMN_ID, /*regattaArgs[0]*/ "");
	    long insertId = database.insert(MySQLiteHelper.TABLE_REGATTAS, null,
	        values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_REGATTAS,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Regatta newRegatta = cursorToRegatta(cursor);
	    cursor.close();
	    return newRegatta;
	  }

	  public void deleteRegatta(Regatta regatta) {
	    int id = regatta.getId();
	    System.out.println("Regatta deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_REGATTAS, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }
	  
	  public List<Regatta> searchRegattas(String[] filters) {
		  List<Regatta> regattas = new ArrayList<Regatta>();
		  return regattas;
	  }
	  
	  public List<Regatta> getAllRegattas() {
	    List<Regatta> regattas = new ArrayList<Regatta>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_REGATTAS,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Regatta regatta = cursorToRegatta(cursor);
	      regattas.add(regatta);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return regattas;
	  }

	  private Regatta cursorToRegatta(Cursor cursor) {
	    Regatta regatta = new Regatta();
	    regatta.setId(cursor.getInt(0));
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
	    regatta.setLatitude(cursor.getFloat(12));
	    regatta.setLongitude(cursor.getFloat(13));
	    return regatta;
	  }
}
