package com.gogi.eregatta;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class updateRegattaDetails extends HttpServlet {
	private static final Logger log = Logger.getLogger(HttpServlet.class.getName());
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		HtmlPage regattaDetailPage;
		final WebClient webClient = new WebClient();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		webClient.setCookieManager(new CookieManager() {			// POPRAVEK BUGA KI NASTAVI PORT 80 NAMESTO -1
			protected int getPort(final URL url) {
				final int r = super.getPort(url);
				return r != -1 ? r : 80;
			}
		});
		
		
	// UTIŠANJE HTMLUNIT ERRORJEV
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	    webClient.setCssEnabled(false);
	    webClient.setIncorrectnessListener(new IncorrectnessListener() {
	        @Override
	        public void notify(String arg0, Object arg1) {}
	    });
	    webClient.setCssErrorHandler(new ErrorHandler() {
	    	@Override
	        public void warning(CSSParseException exception) throws CSSException {}
	        @Override
	        public void fatalError(CSSParseException exception) throws CSSException {}
	        @Override
	        public void error(CSSParseException exception) throws CSSException {}
	    });
	    webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
	        @Override
	        public void timeoutError(HtmlPage arg0, long arg1, long arg2) {}
	        @Override
	        public void scriptException(HtmlPage arg0, ScriptException arg1) {}
	        @Override
	        public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {}
	        @Override
	        public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {}
	    });
	    webClient.setHTMLParserListener(new HTMLParserListener() {
	        @Override
	        public void warning(String arg0, URL arg1, String arg5, int arg2, int arg3, String arg4) {}
	        @Override
	        public void error(String arg0, URL arg1, String arg5, int arg2, int arg3, String arg4) {}
	    });
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.setThrowExceptionOnScriptError(false);
	    
		
		try{
			String regattaId = req.getParameter("regattaId");							// iz POST requesta dobi ven parametre
			String country = req.getParameter("country");
			String name = req.getParameter("name");
			log.warning("Regatta id: "+regattaId);
			Key countryKey = KeyFactory.createKey("country", country);
			Entity regatta = new Entity("regatta", regattaId, countryKey);				// doloèi propertye za regato na podlagi tega kar smo dobili iz htmlja
			regatta.setProperty("Name", name.replaceAll("\u00a0", " "));
			
			String lat;																	// doloèi propertye koordinate èe so bili podani v putRegattas
			String lon;
			if ((lat = req.getParameter("lat")) != null && (lon = req.getParameter("lon")) != null){
				regatta.setProperty("Lat", lat);
				regatta.setProperty("Lon", lon);
				log.warning("Regatta coord.: "+lat+", "+lon);
			}
			else{
				log.warning("Coordinates not found");
			}
			
	    	
    		regattaDetailPage = webClient.getPage("http://www.sailing.org/regattainfo.php?rgtaid="+regattaId);			
	    	
			List<?> regattaDetailTrList = regattaDetailPage.getByXPath("//*[@id='skiptomain']/div[2]/div/div[1]/div[1]/div/div/div[3]/table/tbody/tr");	
			
			if (regattaDetailTrList.size() > 0){											// ce je List z tableRow-i detajlov regate poln gre parsat
				Iterator<?> detailTrIterator = regattaDetailTrList.iterator(); 
				boolean emptyTd = false;
				String propertyName="";
				String propertyValue="";
				
				while (detailTrIterator.hasNext()){												//gre èez vse vrstice (tr) z detajli regate
					HtmlTableRow trDetail = (HtmlTableRow) detailTrIterator.next();   			
					List<HtmlTableCell> tdDetailList = trDetail.getCells();
					
					if (tdDetailList.get(0).getTextContent().replaceAll("\u00a0", "").length()>1){		//èe prva celica ni vemo da gre za nov property
						if (emptyTd==true){														// èe je bila prejšna vrstica še del nekega prejšnega propertya (prazen td z imenom propertya), zdej je pa nov je treba zapisat prejšnega
							emptyTd=false;
							
							regatta.setProperty(propertyName.replaceAll("\u00a0", " "), propertyValue.replaceAll("\u00a0", " "));	
							
						}
						
						propertyName = tdDetailList.get(0).getTextContent();										// property za novo vrstico
						if (tdDetailList.get(1).hasChildNodes()){														// èe je link je treba pogledat še v child element <a>
							propertyValue = tdDetailList.get(1).getFirstChild().getTextContent();
						}
						else {
							propertyValue = tdDetailList.get(1).getTextContent();
						}
						
						
						propertyValue = propertyValue.replaceAll("\u00a0", " ");
						if (propertyName.equals("Start Date") || propertyName.equals("End Date")){
							propertyName = propertyName.replaceAll(" ", "");
							propertyValue = toSQLdate(propertyValue);
						}
						propertyName = propertyName.replaceAll("\u00a0", "");
						
						
						regatta.setProperty(propertyName, propertyValue);
					}
					else {
						emptyTd = true;
						if (tdDetailList.get(1).hasChildNodes()){														// èe je link je treba pogledat še v child element <a>
							propertyValue += ", "+ tdDetailList.get(1).getFirstChild().getTextContent();
						}
						else {
							propertyValue += ", "+ tdDetailList.get(1).getTextContent();
						}
					}
				}
			}
			else{
				log.severe(regattaId+": regatta info unavailable");
			}
			
			datastore.put(regatta);
			
			List<?> regattaClassTrList = regattaDetailPage.getByXPath("//*[@id='skiptomain']/div[2]/div/div[2]/div[2]/div/div/table/tbody/tr");	
			if (regattaClassTrList.size() > 0){															// ce je List z tableRow-i typov in classov regate poln gre parsat
				Iterator<?> classTrIterator = regattaClassTrList.iterator(); 
				classTrIterator.next();																	// v prvi vrstici so naslovi stolpcev in to ne rabmo
				while (classTrIterator.hasNext()){														// gre èez vse vrstice
					HtmlTableRow trClass = (HtmlTableRow) classTrIterator.next(); 
					List<HtmlTableCell> tdClassList = trClass.getCells();
					
					String discipline = tdClassList.get(0).getTextContent();
					String type = tdClassList.get(1).getTextContent();
					String Class = tdClassList.get(2).getTextContent();
					String grade = tdClassList.get(3).getTextContent();
					
					Entity regattaClass = new Entity ("class", regatta.getKey());						// klase in discipline so potomec regate
					regattaClass.setProperty("Discipline", discipline.replaceAll("\u00a0", " "));
					regattaClass.setProperty("Type", type.replaceAll("\u00a0", " "));
					regattaClass.setProperty("Class", Class.replaceAll("\u00a0", " "));
					regattaClass.setProperty("Grade", grade.replaceAll("\u00a0", " "));
						
					datastore.put(regattaClass);
				}
			}
			else{
				log.warning(regattaId+": regatta classes unavailable");
			}
		}
		catch(ScriptException e){
			e.printStackTrace();
			log.severe("DEBUG: ScriptException");
		}
		catch(Exception e){
			e.printStackTrace();
			log.severe("DEBUG: unknown Exception: "+e.getMessage());
		}
	}
	
	private String toSQLdate(String strDate){
		SimpleDateFormat longFormat = new SimpleDateFormat("d MMMMM yyyy");
		String formattedDate = null;
        try {
        	Date date = longFormat.parse(strDate);
        	SimpleDateFormat SQLformat = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = SQLformat.format(date);
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.severe("DEBUG: error during converting to SQL date format");
        }
        if (formattedDate != null){
        	return formattedDate;
        }
        else {
        	return "";
        }
	}
}
