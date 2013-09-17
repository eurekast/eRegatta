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

@Api(name = "bookingendpoint")
public class BookingEndpoint {

	@ApiMethod (httpMethod="GET")
	public List<Booking> downloadBookings(/*@Named("searchdata") String jsonSearchData*/) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
		List<Booking> results = new ArrayList<Booking>();
		Query classQuery = new Query("booking");
		String tester = "";
		
		for (final Entity entity: datastore.prepare(classQuery).asIterable()) {	
			try {
				Booking booking = new Booking();
				booking.setId(Integer.parseInt(entity.getParent().getName()));
				booking.setPrice(entity.getProperty("price").toString());
				booking.setUrl(entity.getProperty("url").toString());
				results.add(booking);
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
			
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
	@ApiMethod(name = "listBooking")
	public CollectionResponse<Booking> listBooking(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Booking> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Booking as Booking");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Booking>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Booking obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Booking> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}*/

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getBooking")
	public Booking getBooking(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Booking booking = null;
		try {
			booking = mgr.find(Booking.class, id);
		} finally {
			mgr.close();
		}
		return booking;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param booking the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertBooking")
	public Booking insertBooking(Booking booking) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsBooking(booking)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(booking);
		} finally {
			mgr.close();
		}
		return booking;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param booking the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateBooking")
	public Booking updateBooking(Booking booking) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsBooking(booking)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(booking);
		} finally {
			mgr.close();
		}
		return booking;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeBooking")
	public Booking removeBooking(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Booking booking = null;
		try {
			booking = mgr.find(Booking.class, id);
			mgr.remove(booking);
		} finally {
			mgr.close();
		}
		return booking;
	}

	private boolean containsBooking(Booking booking) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Booking item = mgr.find(Booking.class, booking.getId());
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
