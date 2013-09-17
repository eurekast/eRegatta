package com.gogi.eregatta;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class updateBooking extends HttpServlet {
	private static final Logger log = Logger.getLogger(HttpServlet.class.getName()); 
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//PrintWriter out = resp.getWriter();
		log.warning("DEBUG: doPost started.");
		
		final HtmlPage page;
		final HtmlPage page2;
		final HtmlPage page3;
		final HtmlPage page3stars;
		HtmlPage page4;
		final WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(false);
        final SilentCssErrorHandler eh = new SilentCssErrorHandler();
        webClient.setCssErrorHandler(eh);											// zarad tega vsega naj bi hitrej delal
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setCssEnabled(false);
        webClient.setPopupBlockerEnabled(true);
        webClient.setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(3600); 
        webClient.setTimeout(9000); 
        webClient.getCookieManager().setCookiesEnabled(true);
        
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		webClient.setCookieManager(new CookieManager() {			// POPRAVEK BUGA KI NASTAVI PORT 80 NAMESTO -1
			protected int getPort(final URL url) {
				final int r = super.getPort(url);
				return r != -1 ? r : 80;
			}
		});
		
		List<?> searchInputList;
		try{
			page = webClient.getPage("http://www.booking.com/index.en-gb.html");			// odpre spletno stran www.booking.com
			searchInputList= page.getByXPath("//*[@id='destination']");
			
			
			if (!searchInputList.isEmpty()){													// èe najde search polje
				HtmlTextInput searchInput = (HtmlTextInput) searchInputList.get(0);
				HtmlSelect inDayInput = (HtmlSelect) page.getElementById("checkin_day");
				HtmlSelect inMonthYearInput = (HtmlSelect) page.getElementById("checkin_year_month");
				HtmlSelect outDayInput = (HtmlSelect) page.getElementById("checkout_monthday");
				HtmlSelect outMonthYearInput = (HtmlSelect) page.getElementById("checkout_year_month");
				
				
				
				log.warning("DEBUG: " +
						"venue "+req.getParameter("venue")+
						", country "+req.getParameter("country")+
						", startDay "+req.getParameter("startDay")+
						", startMonthYear "+req.getParameter("startMonthYear")+
						", endDay "+req.getParameter("endDay")+
						", endMonthYear "+req.getParameter("endMonthYear"));
				
			
				// v search napiše kraj in državo
				searchInput.setValueAttribute(req.getParameter("venue")+", "+req.getParameter("country"));		
				
				//izpolni dropdowne za datum
				HtmlOption inDayOption = inDayInput.getOptionByValue(req.getParameter("startDay"));
				HtmlOption inMonthYearOption = inMonthYearInput.getOptionByValue(req.getParameter("startMonthYear"));
				HtmlOption outDayOption = outDayInput.getOptionByValue(req.getParameter("endDay"));
				HtmlOption outMonthYearOption = outMonthYearInput.getOptionByValue(req.getParameter("endMonthYear"));
				
				inDayInput.setSelectedAttribute(inDayOption, true);
				outDayInput.setSelectedAttribute(outDayOption, true);
				inMonthYearInput.setSelectedAttribute(inMonthYearOption, true);
				outMonthYearInput.setSelectedAttribute(outMonthYearOption, true);
				
				// najde gumb za izvršitev searcha
				List<?> searchButtonList = page.getByXPath("//*[@id='searchbox_btn']");					
				HtmlButton searchButton = (HtmlButton) searchButtonList.get(0);
				
				// naloži drugo stran (cities)
			    page2 = searchButton.click();	
			    
			    // najde gumb za izvršitev searcha
			    List<?> cityDivList = page2.getByXPath("//*[@id='cityWrapper']/div/div[2]/a");
			    if (!cityDivList.isEmpty()){
			    	log.warning("DEBUG: Page 2 opened (cities)");
			    	
			    	// najde link na prvo mesto
			    	HtmlAnchor cityLinkElement = (HtmlAnchor) cityDivList.get(0);								
			    	String cityLink = "http://www.booking.com" +cityLinkElement.getAttribute("href");
			    	
			    	// in ga klikne
			    	page3 = webClient.getPage(cityLink);			
			    	
			    	// naloži stran kjer so samo hoteli s 3 zvezdicami
			    	HtmlAnchor page3starsLink = (HtmlAnchor) page3.getByXPath("//*[@id='filter_class']/div[2]/a[3]").get(0);		
			    	String finalUrl = "http://www.booking.com"+page3starsLink.getAttribute("href");
			    	log.warning("DEBUG: openingPage: "+finalUrl);
			    	//out.write("DEBUG: openingPage: "+page3starsLink.getAttribute("href"));
			    	page3stars = page3starsLink.click();
			    	if(page3stars!=null){
			    		log.warning("DEBUG: Page 3 opened (hotel list ordered by stars)");
			    		//out.write(page3stars.asXml());
			    	}
			    	else {
			    		log.warning("DEBUG: Page 3 FAILED to open (hotel list ordered by stars)");
			    	}
			    	
			    	////*[@id="hotellist_inner"]/div/div[2]/div[3]/form/table/tbody/tr
			    	List<?> priceTrList = page3stars.getByXPath("//*[@id='hotellist_inner']/div/div[2]/div[3]/form/table/tbody/tr");
			    	if (priceTrList.size()>0){
			    		Iterator<?> priceTrIterator = priceTrList.iterator();
			    		double priceSum = 0;
			    		int priceRows=0;
			    		
			    		//out.write("<p>"+priceTrList.size()+"</p>");
			    		
			    		// gre èez vse vrstice s cenami vseh hotelov
			    		while(priceTrIterator.hasNext()){		
		    				try {
		    					HtmlTableRow priceTr = (HtmlTableRow) priceTrIterator.next(); 		
		    					
		    					//najde število oseb v tej sobi
	    						List<?> priceCells = priceTr.getCells();
	    						HtmlTableCell maxPersonsCell = (HtmlTableCell) priceCells.get(1);	
	    	    				String maxPersons = maxPersonsCell.getTextContent().replaceAll( "[^\\d]", "" );
	    	    				//out.write("<p>"+maxPersons+"</p>");

	    	    				// èe je število oseb 2 pogleda kakšna je cena
	    	    				if (maxPersons.equals("2")){
	    	    					HtmlTableCell priceCell = (HtmlTableCell) priceCells.get(3);	
	    	    					DomElement priceDivDom = priceCell.getFirstElementChild();			
		    						if (priceDivDom!=null){
			    						HtmlDivision priceDiv = (HtmlDivision) priceDivDom;
			    						HtmlStrong priceStrong = (HtmlStrong) priceDiv.getHtmlElementsByTagName("strong").get(0);
			    						priceSum += Double.parseDouble(priceStrong.getTextContent().replaceAll("[$ ,]", ""));
			    						//log.warning(priceStrong.getTextContent().replaceAll("[$ ,]", ""));
			    						//out.println("Price: "+priceStrong.getTextContent()+", Sum: "+priceSum+"<br>");
			    						priceRows++;
			    						
			    						// brisanje prejšnjih podatkov o bookingu
			    						
			    						if (!priceTrIterator.hasNext() || priceRows==20){
				    						// izraèuna in zaokroži se povpreèna cena za vse hotele v tem mestu
				    						double averagePrice = priceSum/priceRows;							
				    			    		averagePrice = (double)Math.round(averagePrice * 100) / 100;
				    			    		String averagePriceString = Double.toString(averagePrice);
				    			    		
				    			    		log.warning ("DEBUG: Writing new booking. iterator: "+priceTrIterator.hasNext()+", rows = "+priceRows);
				    						//log.warning("DEBUG: Avarage Price: "+averagePriceString+", priceSum = "+priceSum+", priceCount ="+priceRows);
				    						
				    						// key regate kateri pripada booking
				    			    		Key regattaKey = KeyFactory.stringToKey(req.getParameter("regattaKey"));									
				    			    		
				    			    		// iz datastora se izbriše booking od prej
				    			    		Query bookingQuery = new Query ("booking", regattaKey);									
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
				    						// vpiše se nov booking
				    						Entity booking = new Entity("booking", regattaKey);
				    			    		booking.setProperty("price", averagePriceString);
				    			    		booking.setProperty("url", finalUrl);
				    			    		try {
				    			    			datastore.put(booking);
				    			    		}
				    			    		catch (Exception e){
				    			    			log.warning("DEBUG: Error writing booking into database: "+e.getMessage());
				    			    		}
				    			    		log.warning("Booking (un)succesfully added");
				    			    		break;
		    							}
		    						}
	    	    				}
		    				}
		    				catch(Exception e){
		    					e.printStackTrace();
		    					log.warning("DEBUG: Error while parsing price table rows: "+e.getMessage());
		    				}
		    			}
			    	}
			    	else{
			    		log.warning("DEBUG: Did not find elements with prices");
			    	}
			 	}
			} 
		}catch (Exception e){
			e.printStackTrace();
			log.warning(e.getMessage());
		}
		
	//out.close();
	}
	
}
