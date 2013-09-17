package com.gogi.eregatta;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class refreshCountries extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		final HtmlPage page;
		final WebClient webClient = new WebClient();
		
		webClient.setCookieManager(new CookieManager() {			// POPRAVEK BUGA KI NASTAVI PORT 80 NAMESTO -1
			protected int getPort(final URL url) {
				final int r = super.getPort(url);
				return r != -1 ? r : 80;
			}
		});
		    
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			page = webClient.getPage("http://www.sailing.org/regattasearch.php");
			
			if (page != null){						// ce je stran prenesena, zacne iskat dropdown z drzavami
				
				List<?> countriesOptions = page.getByXPath("//*[@id='skiptomain']/div[2]/div/div[1]/div[1]/div[1]/div/form/fieldset/table/tbody/tr[3]/td[2]/select/option");
				if (countriesOptions.size() > 0){											// ce je List z drzavami poln gre parsat
					Iterator<?> optionsIterator = countriesOptions.iterator(); 
					
					while (optionsIterator.hasNext()) {							
						HtmlOption option = (HtmlOption) optionsIterator.next();   			// razdeli na kratice in imena ter zapiše v datastore
						String optionValue = option.getText();
						String[] splitCountry = optionValue.split(" - ");
						if (splitCountry.length>1){										
							Entity country = new Entity("country", splitCountry[1]);
							country.setProperty("name", splitCountry[0]);
							country.setProperty("tag", splitCountry[1]);
					        datastore.put(country);
						}
					}

				}
				else {
					
					// ce ne najde dropdowna z drzavami
					
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		webClient.closeAllWindows();
	}
}

