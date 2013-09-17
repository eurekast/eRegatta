package com.gogi.eregatta;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class putRegattas1 extends HttpServlet {
	static final long serialVersionUID = 1;

	private static final Logger log = Logger.getLogger(HttpServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter printer = resp.getWriter();
		int failedAttempts = 0;
		int totalAttempts = 0;
		
		final WebClient webClient = new WebClient();
		final HtmlPage page;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query regattaQuery = new Query ("regatta");									// brisanje prejšnjih podatkov o regatah
		regattaQuery.setKeysOnly();															
		final ArrayList<Key> exRegattaKeys = new ArrayList<Key>();							
		for (final Entity entity: datastore.prepare(regattaQuery).asIterable()) {			
			exRegattaKeys.add(entity.getKey());
		}
		exRegattaKeys.trimToSize();
		try {
			datastore.delete(exRegattaKeys);
		}
		catch (Exception e){
			e.printStackTrace();
		}																			// ---------------------------------------

		Query classQuery = new Query ("class");									// brisanje prejšnjih podatkov o classih
		classQuery.setKeysOnly();															
		final ArrayList<Key> exClassKeys = new ArrayList<Key>();							
		for (final Entity entity: datastore.prepare(classQuery).asIterable()) {			
			exClassKeys.add(entity.getKey());
		}
		exClassKeys.trimToSize();
		try {
			datastore.delete(exClassKeys);
		}
		catch (Exception e){
			e.printStackTrace();
		}																		// --------------------------------------
		
		webClient.setCookieManager(new CookieManager() {			// POPRAVEK BUGA KI NASTAVI PORT 80 NAMESTO -1
			protected int getPort(final URL url) {
				final int r = super.getPort(url);
				return r != -1 ? r : 80;			
			}
		});															// -------------------------------------------
		
		try {
			page = webClient.getPage("http://members.sailing.org/regattas.php?max=30&begin=1&includeref=092CC935AC8B48B299B32C7E69CC3F17");
			
			if (page != null){															// ce je stran prenesena, zacne iskat dropdown z drzavami
				
				List<?> regattasTrList = page.getByXPath("//*[@id='skiptomain']/div[2]/div/div[1]/div[1]/div/div[1]/table/tbody/tr");
				
				printer.write("<p>Regatta rows found: "+regattasTrList.size()+"</p>");
				if (regattasTrList.size() > 0){											// ce je List z tableRow-i poln gre parsat

					Iterator<?> trIterator = regattasTrList.iterator(); 
					if (trIterator.hasNext()){												// iterira èez vse celice v tabeli in si piše atribute regate
						trIterator.next();
						
						while (trIterator.hasNext()) {									
							HtmlTableRow tr = (HtmlTableRow) trIterator.next();   			
							List<HtmlTableCell> tdList = tr.getCells();
							
							totalAttempts++;
							try {														
								String name = tdList.get(1).getTextContent();
								String country = tdList.get(3).getTextContent();
								String venue = tdList.get(2).getTextContent();
										
								
								HtmlAnchor anchor = (HtmlAnchor) tdList.get(1).getFirstChild();			// regattaId dobi iz linka na regato (php atribut 'rgtaid')
								String[] splitUrl = anchor.getAttribute("href").split("=");
								String regattaId = "";
								if (splitUrl.length>1){
									regattaId = splitUrl[1];
								}
								
								printer.write(regattaId+" ");
								TaskOptions taskOptions = withUrl("/updateregattadetails")				// parametri za task
							    		.param("country", country)
							    		.param("regattaId", regattaId)
							    		.param("name", name);
								
								if (regattaId != null && regattaId != ""){								// iskanje koordinat
									
									URL url;
									String venueEncoded;
									InputStream is = null;
									DataInputStream dis;
									String line;

									String json = "";
									try {
										String countryStr = "";											// pretvori iz "SLO" v "Slovenia
										Key countryKey = KeyFactory.createKey("country", country);		
										Entity countryEnt = datastore.get(countryKey);
										countryStr = countryEnt.getProperty("name").toString();		
										
										venueEncoded = URLEncoder.encode(venue, "UTF-8")+"+"+URLEncoder.encode(countryStr, "UTF-8");	// downloada stran s koordinatami
										url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+venueEncoded+"&sensor=false");
										log.warning("GeoCode URL: "+url);
									    is = url.openStream();  // throws an IOException
									    dis = new DataInputStream(new BufferedInputStream(is));
									    while ((line = dis.readLine()) != null) {
									    	json+=line;
									    }
									} catch (MalformedURLException mue) {
									     mue.printStackTrace();
									} catch (IOException ioe) {
									     ioe.printStackTrace();
									} catch (Exception e){
										e.printStackTrace();
									} finally {
									    try {
									        is.close();
									    } catch (Exception ioe) {
									    	ioe.printStackTrace();
									    }
									}
									
									String[] coordinates = new String[2];		//zapiše koordinate v paramater taska in task da v queue
									if (json != null && json!=""){
										log.warning("Coordinates json received");
										try{
											coordinates = parseJson(json);
											taskOptions = taskOptions
													.param("lat", coordinates[0])
													.param("lon", coordinates[1]);
										}
										catch (Exception e){
											log.warning("JSON coordinates file failed to parse");
											log.warning(e.getMessage());
											e.printStackTrace();
										}
									}
									
									Queue queue = QueueFactory.getDefaultQueue();
								    queue.add(taskOptions);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.warning("Error: "+e.getMessage());
								failedAttempts++;
							}
						}
					}
				}
			}
		}
		catch (Exception e){
			
		}	    
		printer.write("<p>Total attempts: "+totalAttempts+"</p>");
		printer.write("<p>Failed attempts: "+failedAttempts+"</p>");
		printer.close();
	}
	public String[] parseJson(String jsonLine) {
		String[] result = {"0", "0"};
		try{
		    JsonElement jelement = new JsonParser().parse(jsonLine);
		    JsonObject  jobject = jelement.getAsJsonObject();
		    JsonArray jarray = jobject.getAsJsonArray("results");
		    jobject = jarray.get(0).getAsJsonObject();
		    jobject = jobject.getAsJsonObject("geometry");
		    jobject = jobject.getAsJsonObject("location");
		    
		    result[0]= jobject.get("lat").toString();
		    result[1]= jobject.get("lng").toString();
		}
		catch(Exception e){
			log.warning("Coordinates: "+result[0]+" "+result[1]);
		}
	    return result;
	}
	
}
