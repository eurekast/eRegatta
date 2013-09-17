package com.gogi.eregatta;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class getData extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		PrintWriter output = resp.getWriter();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query regattaQuery = new Query ("regatta");												
		output.print("\"'regatta':");
		output.print("[");
		boolean isFirstRegatta = true;
		
		for (Entity regatta : datastore.prepare(regattaQuery).asIterable()) {
			if (!isFirstRegatta){
				output.print(",");
			}
			else {
				
				isFirstRegatta=false;
			}
			
			String regattaId = regatta.getKey().getName();
			String regattaName = regatta.getProperty("Name").toString();
			String regattaCountry = regatta.getProperty("Country").toString();
			String regattaVenue = regatta.getProperty("Venue").toString();
			String regattaStart = regatta.getProperty("Start Date").toString();
			String regattaEnd = regatta.getProperty("End Date").toString();
			String regattaWebsite = regatta.getProperty("Website").toString();
			String regattaEmail = regatta.getProperty("Email").toString();
			String regattaAddress = regatta.getProperty("Entry Address").toString();
			String regattaTel = regatta.getProperty("Tel").toString();
			String regattaFax = regatta.getProperty("Fax").toString();
			
			output.print("{");
			output.print("'Id': '"+regattaId+"',");
			output.print("'Name': '"+regattaName+"',");
			output.print("'Country': '"+regattaCountry+"',");
			output.print("'Venue': '"+regattaVenue+"',");
			output.print("'Start Date': '"+regattaStart+"',");
			output.print("'End Date': '"+regattaEnd+"',");
			output.print("'Website': '"+regattaWebsite+"',");
			output.print("'Email': '"+regattaEmail+"',");
			output.print("'Entry Address': '"+regattaAddress+"',");
			output.print("'Tel': '"+regattaTel+"',");
			output.print("'Fax': '"+regattaFax+"',");
				
			output.print("'class' : [");
			Query classQuery = new Query ("class", regatta.getKey());
			boolean isFirstClass = true;
			for (Entity classEntity : datastore.prepare(classQuery).asIterable()) {
				if (!isFirstClass){
					output.print(",");
				}
				else {
					isFirstClass=false;
				}
				
				String className = classEntity.getProperty("Class").toString();
				String classDiscipline = classEntity.getProperty("Discipline").toString();
				String classGrade = classEntity.getProperty("Grade").toString();
				String classType = classEntity.getProperty("Type").toString();
				
				output.print("{");
				output.print("'ClassName': '"+className+"',");
				output.print("'Discipline': '"+classDiscipline+"',");
				output.print("'Grade': '"+classDiscipline+"',");
				output.print("'Type': '"+classType+"'");
				output.print("}");
				
			}
			output.print("],");
			
			output.print("'weather' : [");
			Query weatherQuery = new Query ("weather", regatta.getKey()).addSort("order");
			boolean isFirstWeather = true;
			for (Entity weather : datastore.prepare(weatherQuery).asIterable()) {
				if (!isFirstWeather){
					output.print(",");
				}
				else {
					isFirstWeather=false;
				}
				
				String weatherDate = weather.getProperty("date").toString();
				String weatherConditions = weather.getProperty("conditions").toString();
				String weatherTempHigh = weather.getProperty("tempHigh").toString();
				String weatherTempLow = weather.getProperty("tempLow").toString();
				String weatherDateUpdated = weather.getProperty("dateUpdated").toString();
				String weatherImageName = weather.getProperty("imageName").toString();
				
				output.print("{");
				output.print("'date': '"+weatherDate+"',");
				output.print("'conditions': '"+weatherConditions+"',");
				output.print("'tempHigh': '"+weatherTempHigh+"',");
				output.print("'tempLow': '"+weatherTempLow+"',");
				output.print("'dateUpdated': '"+weatherDateUpdated+"',");
				output.print("'imageName': '"+weatherImageName+"'");
				output.print("}");
				
			}
			output.print("],");
			
			output.print("'booking' : [");
			Query bookingQuery = new Query ("booking", regatta.getKey());
			boolean isFirstBooking = true;
			for (Entity booking : datastore.prepare(bookingQuery).asIterable()) {
				if (!isFirstBooking){
					output.print(",");
				}
				else {
					isFirstBooking=false;
				}
				
				String bookingPrice = booking.getProperty("price").toString();
				String bookingUrl = booking.getProperty("Url").toString();
				
				output.println("{");
				output.println("'price': '"+bookingPrice+"',");
				output.println("'Url': '"+bookingUrl+"'");
				output.println("}");
				
			}
			output.print("]");
					
			
			output.print("}");
		}
		output.print("]");
		output.close();
	}
}
