package com.gogi.eregatta;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

@Api(name = "regattaclassendpoint")
public class RegattaClassEndpoint {
	
	@ApiMethod (httpMethod="GET")
	public List<RegattaClass> downloadRegattaClasses(/*@Named("searchdata") String jsonSearchData*/) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
		List<RegattaClass> results = new ArrayList<RegattaClass>();
		Query classQuery = new Query("class");
		
		for (final Entity entity: datastore.prepare(classQuery).asIterable()) {	
			RegattaClass regattaClass = new RegattaClass();
			
			regattaClass.setRegattaId(entity.getParent().getName());
			regattaClass.setRegattaClass(entity.getProperty("Class").toString());
			regattaClass.setDiscipline(entity.getProperty("Discipline").toString());
			regattaClass.setGrade(entity.getProperty("Grade").toString());
			regattaClass.setType(entity.getProperty("Type").toString());			
				
			results.add(regattaClass);
		}
		
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
	@ApiMethod(name = "listRegattaClass")
	public CollectionResponse<RegattaClass> listRegattaClass(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<RegattaClass> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from RegattaClass as RegattaClass");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<RegattaClass>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (RegattaClass obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<RegattaClass> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}*/

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getRegattaClass")
	public RegattaClass getRegattaClass(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		RegattaClass regattaclass = null;
		try {
			regattaclass = mgr.find(RegattaClass.class, id);
		} finally {
			mgr.close();
		}
		return regattaclass;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param regattaclass the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertRegattaClass")
	public RegattaClass insertRegattaClass(RegattaClass regattaclass) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsRegattaClass(regattaclass)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(regattaclass);
		} finally {
			mgr.close();
		}
		return regattaclass;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param regattaclass the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateRegattaClass")
	public RegattaClass updateRegattaClass(RegattaClass regattaclass) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsRegattaClass(regattaclass)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(regattaclass);
		} finally {
			mgr.close();
		}
		return regattaclass;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeRegattaClass")
	public RegattaClass removeRegattaClass(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		RegattaClass regattaclass = null;
		try {
			regattaclass = mgr.find(RegattaClass.class, id);
			mgr.remove(regattaclass);
		} finally {
			mgr.close();
		}
		return regattaclass;
	}

	private boolean containsRegattaClass(RegattaClass regattaclass) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			RegattaClass item = mgr.find(RegattaClass.class,
					regattaclass.getId());
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
