package com.gogi.eregatta;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class putExtras extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(HttpServlet.class.getName()); 
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query weatherQuery = new Query ("weather");									// brisanje prejšnjih podatkov o vremenu
		weatherQuery.setKeysOnly();
		final ArrayList<Key> exWeatherKeys = new ArrayList<Key>();							
		for (final Entity entity: datastore.prepare(weatherQuery).asIterable()) {			
			exWeatherKeys.add(entity.getKey());
		}
		exWeatherKeys.trimToSize();
		try {
			datastore.delete(exWeatherKeys);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		Query bookingQuery = new Query ("booking");									// brisanje prejšnjih podatkov o bookingu
		bookingQuery.setKeysOnly();															
		final ArrayList<Key> exBookingKeys = new ArrayList<Key>();							
		for (final Entity entity: datastore.prepare(bookingQuery).asIterable()) {			
			exBookingKeys.add(entity.getKey());
		}
		exBookingKeys.trimToSize();
		try {
			datastore.delete(exBookingKeys);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		Query regattaQuery = new Query ("regatta");												// gre èez vse regate in dodaja taske za pridobivanje vremenske napovedi
		for (Entity regatta : datastore.prepare(regattaQuery).asIterable()) {
			
			String country = regatta.getProperty("Country").toString();							// dodajanje country, venue, startDate, endDate in regattaKey v POST request za /updateweather
			String venue = regatta.getProperty("Venue").toString();
			String startDate = regatta.getProperty("StartDate").toString();
			String endDate = regatta.getProperty("EndDate").toString();
			String regattaKey = KeyFactory.keyToString(regatta.getKey());
			
			Date today = new Date();															// èe je kakšen izmed datumov že v preteklosti se popravi naprej;						
			Date start = returnDate(startDate);
			Date end = returnDate(endDate);
			
			String[] startDateForInput = toBookingFormat(start);
			String[] endDateForInput = toBookingFormat(end);
			
			Queue queue = QueueFactory.getDefaultQueue();																// dodajanje taskov za pridobivanje podatkov s spletnih strani
			queue.add(withUrl("/updatebooking")
					.param("venue", venue)
					.param("country", country)
					.param("regattaKey", regattaKey)
					.param("startDay", startDateForInput[0])
					.param("startMonthYear", startDateForInput[1])
					.param("endDay", endDateForInput[0])
					.param("endMonthYear", endDateForInput[1]));
		    
		    log.warning("DEBUG: country="+country+", venue="+venue+", startDate="+startDate+", startDay="+startDateForInput[0]+", startMonthYear="+startDateForInput[1]);
		}
		

	}
	
	private static String[] toBookingFormat (Date date){		// iz formata "1 January 2012" pretvori v {"1", "January 2012"}
		//String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
		
		String[] bookingFormatDate = new String[2];
		
		bookingFormatDate[0] = Integer.toString(date.getDate());
		int monthValue = date.getMonth()+1;
		int yearValue = date.getYear()+1900;
		bookingFormatDate[1] = yearValue+"-"+monthValue;
		
		return bookingFormatDate;
	}
	
	
	private static String toSQLFormat (String datastoreFormat){		// iz formata "1 January 2012" pretvori v "2012-01-01"
		ArrayList<String> months = new ArrayList<String>();
		months.add("January");
		months.add("February");
		months.add("March");
		months.add("April");
		months.add("May");
		months.add("June");
		months.add("July");
		months.add("August");
		months.add("September");
		months.add("October");
		months.add("November");
		months.add("December");
		
		String bookingFormatDate = "";
		try {
			StringTokenizer st = new StringTokenizer(datastoreFormat, " ");
			ArrayList<String> dateSplit = new ArrayList<String>();
			
			while (st.hasMoreElements()) {
				dateSplit.add(st.nextToken());
			}
			
			String day = dateSplit.get(0);
			String month = String.valueOf(months.indexOf(dateSplit.get(1))+1);
			String year = dateSplit.get(2);
			
			if (day.length()<2){
				day = "0"+day;
			}
			if (month.length()<2){
				month = "0"+month;
			}
			bookingFormatDate = year+"-"+month+"-"+day;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookingFormatDate;
	}
	
	private static Date returnDate (String date){				// iz formata "2012-01-01" vrne objekt tipa Date
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Date parsedDate = new Date();
		
		try {
			parsedDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parsedDate;
	}
}
