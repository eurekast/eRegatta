package com.gogi.eregatta;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


public class getServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query regattasQuery = new Query("regatta");
		
		resp.setContentType("text/xml");
		PrintWriter output = resp.getWriter();
		output.print("<regattas>");
		
		for (Entity regatta : datastore.prepare(regattasQuery).asIterable()) {
			output.print("<regatta>");
			
			String regattaID = Long.toString(regatta.getKey().getId());
			if (regattaID != null)
				output.print("<ID>" + regattaID + "</ID>");
			
			Query windguruQuery = new Query ("windguru", regatta.getKey());
			for (Entity windguru : datastore.prepare(windguruQuery).asIterable()) {
				String temperatureMax = (String) windguru.getProperty("temperatureMax");
				if (temperatureMax != null)
					output.print("<tempMax>" + temperatureMax + "</tempMax>");
				
				String temperatureMin = (String) windguru.getProperty("temperatureMin");
				if (temperatureMin != null)
					output.print("<tempMin>" + temperatureMin + "</tempMin>");
				
			}	 
			output.print("</regatta>");
		}
		
		output.print("</regattas>");
		
	}
}
