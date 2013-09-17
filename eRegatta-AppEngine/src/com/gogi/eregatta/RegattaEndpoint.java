package com.gogi.eregatta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServlet;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

@Api(name = "regattaendpoint")
public class RegattaEndpoint {
private static final Logger log = Logger.getLogger(HttpServlet.class.getName());
	
	@ApiMethod (httpMethod="GET")
	public List<Regatta> downloadRegattas(/*@Named("searchdata") String jsonSearchData*/) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
		List<Regatta> results = new ArrayList<Regatta>();
		Query regattaQuery = new Query("regatta");
		String tester = "";
		
		for (final Entity entity: datastore.prepare(regattaQuery).asIterable()) {
			Regatta regatta = new Regatta();
			
			int id = Integer.parseInt(entity.getKey().getName());
			regatta.setId(id);
			regatta.setCountry(entity.getParent().getName());
			regatta.setName(entity.getProperty("Name").toString());
			regatta.setStartDate(entity.getProperty("StartDate").toString());
			regatta.setEndDate(entity.getProperty("EndDate").toString());
			regatta.setEntryAddress(entity.getProperty("Entry Address").toString());
			regatta.setVenue(entity.getProperty("Venue").toString());
			regatta.setEmail(entity.getProperty("Email").toString());
			regatta.setEntryName(entity.getProperty("Entry Name").toString());
			regatta.setFax(entity.getProperty("Fax").toString());
			regatta.setTelephone(entity.getProperty("Tel").toString());
			regatta.setWebsite(entity.getProperty("Website").toString());
			
			if (entity.getProperty("Lat") != null){
				regatta.setLatitude(Float.parseFloat(entity.getProperty("Lat").toString()));
			}
			else{
				regatta.setLatitude(null);
				
			}
			if (entity.getProperty("Lon") != null){
				regatta.setLongitude(Float.parseFloat(entity.getProperty("Lon").toString()));
			}
			else{
				regatta.setLongitude(null);
			}
			results.add(regatta);
			
		}		
		log.warning("DEBUG: "+"size of regattas array: "+results.size());
		return results;
	}
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	/*@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listRegatta")
	public CollectionResponse<Regatta> listRegatta(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Regatta> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Regatta as Regatta");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Regatta>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Regatta obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Regatta> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}*/

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getRegatta")
	public Regatta getRegatta(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Regatta regatta = null;
		try {
			regatta = mgr.find(Regatta.class, id);
		} finally {
			mgr.close();
		}
		return regatta;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param regatta the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertRegatta")
	public Regatta insertRegatta(Regatta regatta) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsRegatta(regatta)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(regatta);
		} finally {
			mgr.close();
		}
		return regatta;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param regatta the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateRegatta")
	public Regatta updateRegatta(Regatta regatta) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsRegatta(regatta)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(regatta);
		} finally {
			mgr.close();
		}
		return regatta;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeRegatta")
	public Regatta removeRegatta(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Regatta regatta = null;
		try {
			regatta = mgr.find(Regatta.class, id);
			mgr.remove(regatta);
		} finally {
			mgr.close();
		}
		return regatta;
	}

	private boolean containsRegatta(Regatta regatta) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Regatta item = mgr.find(Regatta.class, regatta.getId());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
