package modelo.dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import modelo.JPAUtil;

import modelo.configuration.UtilDate;
import modelo.domain.Driver;
import modelo.domain.Passenger;
import modelo.domain.Ride;
import modelo.domain.User;
import modelo.exceptions.RideAlreadyExistException;
import modelo.exceptions.RideMustBeLaterThanTodayException;

public class DataAccessHibernate {

	public DataAccessHibernate() {
		initializeDB();
	}

	public void initializeDB() {
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Calendar today = Calendar.getInstance();
			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			// Create drivers and passengers
			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez", "1234");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga", "1234");
			Driver driver3 = new Driver("driver3@gmail.com", "Ana Menendez", "1234");
			Driver driver4 = new Driver("d@gmail.com", "d", "d");

			Passenger passenger1 = new Passenger("passenger1@gmail.com", "Mario Explorador", "1234");
			Passenger passenger2 = new Passenger("passenger2@gmail.com", "Sofia Martinez", "1234");
			Passenger passenger3 = new Passenger("passenger3@gmail.com", "Leo Perez", "1234");
			Passenger passenger4 = new Passenger("p@gmail.com", "p", "p");

			// Persist drivers and passengers
			em.getTransaction().begin();
			em.persist(driver1);
			em.persist(driver2);
			em.persist(driver3);
			em.persist(driver4);
			em.persist(passenger1);
			em.persist(passenger2);
			em.persist(passenger3);
			em.persist(passenger4);
			em.getTransaction().commit();
			em.close();

			// Create rides
			createRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7, driver1.getEmail());
			createRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8, driver1.getEmail());
			createRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4, driver1.getEmail());
			createRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8, driver1.getEmail());

			createRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3, driver2.getEmail());
			createRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5, driver2.getEmail());
			createRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5, driver2.getEmail());

			createRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3, driver3.getEmail());

			System.out.println("\u001B[37mDatabase initialized\u001B[0m");
		} catch (Exception e) {
			System.out.println("\u001B[37mError initializing Database: " + e.getMessage() + "\u001B[0m");
			e.printStackTrace();
		}
	}

	public List<String> getDepartCities() {
		EntityManager em = JPAUtil.getEntityManager();
		TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.departCity FROM Ride r ORDER BY r.departCity",
				String.class);
		List<String> cities = query.getResultList();
		System.out.println("\u001B[37mRetrieved depart cities: " + cities + "\u001B[0m");
		return cities;
	}

	public List<String> getDestinationCities(String from) {
		EntityManager em = JPAUtil.getEntityManager();
		TypedQuery<String> query = em.createQuery(
				"SELECT DISTINCT r.destinationCity FROM Ride r WHERE r.departCity = :from ORDER BY r.destinationCity",
				String.class);
		query.setParameter("from", from);
		List<String> cities = query.getResultList();
		System.out.println("\u001B[37mRetrieved arrival cities from " + from + ": " + cities + "\u001B[0m");
		return cities;
	}

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		EntityManager em = JPAUtil.getEntityManager();
		System.out.println("\u001B[37mCreating ride: from= " + from + ", to= " + to + ", driver= " + driverEmail
				+ ", date= " + date + "\u001B[0m");

		/*
		 * if (new Date().compareTo(date) > 0) { throw new
		 * RideMustBeLaterThanTodayException(
		 * ResourceBundle.getBundle("Etiquetas").getString(
		 * "CreateRideGUI.ErrorRideMustBeLaterThanToday")); }
		 */

		TypedQuery<Driver> query = em.createQuery("SELECT d FROM Driver d WHERE d.email = :email", Driver.class);
		query.setParameter("email", driverEmail);
		Driver driver = query.getSingleResult();

		if (driver == null) {
			throw new NullPointerException("Driver not found");
		}

		if (driver.doesRideExists(from, to, date)) {
			throw new RideAlreadyExistException(
					ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
		}

		Ride ride = new Ride(from, to, date, nPlaces, price, driver);
		driver.addRide(ride);
		em.getTransaction().begin();
		em.persist(ride);
		em.merge(driver);
		em.getTransaction().commit();
		em.close();
		System.out.println("\u001B[37mCreated ride: " + ride + "\u001B[0m");

		return ride;
	}

	public List<Ride> getRides(String from, String to, Date date) {
		EntityManager em = JPAUtil.getEntityManager();
		System.out
				.println("\u001B[37mRetrieving rides: from= " + from + ", to= " + to + ", date= " + date + "\u001B[0m");

		TypedQuery<Ride> query = em.createQuery(
				"SELECT r FROM Ride r WHERE r.departCity = :from AND r.destinationCity = :to AND r.date = :date",
				Ride.class);
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("date", date);
		List<Ride> res = query.getResultList();
		System.out.println("\u001B[37mRetrieved rides: " + res + "\u001B[0m");
		return res;
	}

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		EntityManager em = JPAUtil.getEntityManager();
		System.out.println("\u001B[37mRetrieving this month's dates with rides: from= " + from + ", to= " + to
				+ ", date= " + date + "\u001B[0m");

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = em.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.departCity = :from AND r.destinationCity = :to AND r.date BETWEEN :start AND :end ORDER BY r.date",
				Date.class);
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("start", firstDayMonthDate);
		query.setParameter("end", lastDayMonthDate);
		List<Date> res = query.getResultList();
		System.out.println("\u001B[37mRetrieved dates with rides: " + res + "\u001B[0m");
		return res;
	}

	public boolean createDriver(String username, String password, String email) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			System.out.println("\u001B[37mCreating driver: username= " + username + ", email= " + email + "\u001B[0m");
			Driver driver = new Driver(email, username, password);
			em.getTransaction().begin();
			em.persist(driver);
			em.getTransaction().commit();
			em.close();
			System.out.println("\u001B[37mCreated driver: " + driver + "\u001B[0m");
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				em.close();
			}
			return false;
		}
	}

	public boolean createPassenger(String username, String password, String email) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			System.out
					.println("\u001B[37mCreating Passenger: username= " + username + ", email= " + email + "\u001B[0m");
			Passenger passenger = new Passenger(email, username, password);
			em.getTransaction().begin();
			em.persist(passenger);
			em.getTransaction().commit();
			em.close();
			System.out.println("\u001B[37mCreated Passenger: " + passenger + "\u001B[0m");
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				em.close();
			}
			return false;
		}
	}

	public Driver getDriver(String username) {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        // Query for a Driver with the given username
	        TypedQuery<Driver> query = em.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class);
	        query.setParameter("username", username);

	        Driver driver = query.getSingleResult();
	        System.out.println("\u001B[37mDriver found: " + driver + "\u001B[0m");
	        return driver; // Return the found Driver
	    } catch (NoResultException e) {
	        System.out.println("\u001B[37mNo driver found for username: " + username + "\u001B[0m");
	        return null; // Return null if not found
	    }
	}

	public Passenger getPassenger(String username) {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        // Query for a Passenger with the given username
	        TypedQuery<Passenger> query = em.createQuery("SELECT p FROM Passenger p WHERE p.username = :username", Passenger.class);
	        query.setParameter("username", username);

	        Passenger passenger = query.getSingleResult();
	        System.out.println("\u001B[37mPassenger found: " + passenger + "\u001B[0m");
	        return passenger; // Return the found Passenger
	    } catch (NoResultException e) {
	        System.out.println("\u001B[37mNo passenger found for username: " + username + "\u001B[0m");
	        return null; // Return null if not found
	    }
	}
	
	public User getUser(String username) {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
	        query.setParameter("username", username);
	        
	        User user = query.getSingleResult();
	        if (user != null) {
	            System.out.println("\u001B[37mUser found: " + user + "\u001B[0m");
	        }
	        return user;
	    } catch (NoResultException e) {
	        System.out.println("\u001B[37mNo user found for username: " + username + "\u001B[0m");
	        return null;
	    }
	}

	public List<Ride> getRidesByDriver(Driver d) {
		EntityManager em = JPAUtil.getEntityManager();
		TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.driver = :driver", Ride.class);
		query.setParameter("driver", d);
		return query.getResultList();
	}

	public void deleteRide(Ride ride) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		
	    for (Passenger p : ride.getPassengers()) {
	    	p.removeRide(ride);
	        em.merge(p);
	    }
	    
	    Driver d = ride.getDriver();
	    d.removeRide(ride);
	    em.merge(d);
	    
		em.remove(em.contains(ride) ? ride : em.merge(ride));
		em.getTransaction().commit();
		em.close();
	}

	public void removePassengerFromRide(Passenger loggedInUser, Ride ride) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		loggedInUser.removeRide(ride);
		ride.removePassenger(loggedInUser);
		em.merge(loggedInUser);
		em.merge(ride);
		em.getTransaction().commit();
		em.close();
	}
	
	public void addPassengerToRide(Passenger loggedInUser, Ride ride) {
	    EntityManager em = JPAUtil.getEntityManager();
	    em.getTransaction().begin();

	    loggedInUser.addRide(ride);
		ride.addPassenger(loggedInUser);

	    em.merge(loggedInUser);
	    em.merge(ride);

	    em.getTransaction().commit();
	    em.close();
	}

	
}
