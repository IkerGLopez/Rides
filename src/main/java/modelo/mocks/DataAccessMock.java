
package modelo.mocks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import modelo.configuration.UtilDate;
import modelo.domain.Driver;
import modelo.domain.Passenger;
import modelo.domain.Ride;
import modelo.domain.User;
import modelo.exceptions.RideAlreadyExistException;
import modelo.exceptions.RideMustBeLaterThanTodayException;

/**
 * This class implements the data access using ArrayLists instead of a database.
 */
public class DataAccessMock {

	private ArrayList<Ride> rides;
	private ArrayList<Driver> drivers;
	private ArrayList<Passenger> passengers;

	public DataAccessMock() {
		rides = new ArrayList<Ride>();
		drivers = new ArrayList<Driver>();
		passengers = new ArrayList<Passenger>();
		initializeDB();
	}

	public void initializeDB() {
		try {
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

			// Add drivers to the list
			drivers.add(driver1);
			drivers.add(driver2);
			drivers.add(driver3);
			drivers.add(driver4);
			
			// Add passengers
			passengers.add(passenger1);
			passengers.add(passenger2);
			passengers.add(passenger3);
			passengers.add(passenger4);

			// Create rides
			createRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7, driver1.getEmail());
			createRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8, driver1.getEmail());
			createRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4, driver1.getEmail());
			createRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8, driver1.getEmail());

			createRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3, driver2.getEmail());
			createRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5, driver2.getEmail());
			createRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5, driver2.getEmail());

			createRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3, driver3.getEmail());

			System.out.println("\u001B[37mDatabase Mock initialized\u001B[0m");
		} catch (Exception e) {
			System.out.println("\u001B[37mError initializing Database Mock: " + e.getMessage() + "\u001B[0m");
		}
	}

	/**
	 * This method returns all the cities where rides depart.
	 *
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		List<String> cities = new ArrayList<>();
		for (Ride ride : rides) {
			if (!cities.contains(ride.getDepartCity())) {
				cities.add(ride.getDepartCity());
			}
		}
		cities.sort(String::compareTo);
		System.out.println("\u001B[37mRetrieved depart cities: " + cities + "\u001B[0m");
		return cities;
	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city.
	 *
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getDestinationCities(String from) {
		List<String> cities = new ArrayList<>();
		for (Ride ride : rides) {
			if (ride.getDepartCity().equals(from) && !cities.contains(ride.getDestinationCity())) {
				cities.add(ride.getDestinationCity());
			}
		}
		cities.sort(String::compareTo);
		System.out.println("\u001B[37mRetrieved arrival cities from " + from + ": " + cities + "\u001B[0m");
		return cities;
	}

	/**
	 * This method creates a ride for a driver.
	 *
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param price       the price of the ride
	 * @param driverEmail to which ride is added
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println("\u001B[37mCreating ride: from= " + from + ", to= " + to + ", driver= " + driverEmail
				+ ", date= " + date + "\u001B[0m");

		if (new Date().compareTo(date) > 0) {
			throw new RideMustBeLaterThanTodayException(
					ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
		}

		Driver driver = null;
		for (Driver d : drivers) {
			if (d.getEmail().equals(driverEmail)) {
				driver = d;
				break;
			}
		}

		if (driver == null) {
			throw new NullPointerException("Driver not found");
		}

		if (driver.doesRideExists(from, to, date)) {
			throw new RideAlreadyExistException(
					ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
		}

		Ride ride = new Ride(from, to, date, nPlaces, price, driver);
		driver.addRide(ride);
		rides.add(ride);
		System.out.println("\u001B[37mCreated ride: " + ride + "\u001B[0m");

		return ride;
	}

	/**
	 * This method retrieves the rides from two locations on a given date.
	 *
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out
				.println("\u001B[37mRetrieving rides: from= " + from + ", to= " + to + ", date= " + date + "\u001B[0m");

		List<Ride> res = new ArrayList<>();
		for (Ride ride : rides) {
			if (ride.getDepartCity().equals(from) && ride.getDestinationCity().equals(to) && ride.getDate().equals(date)) {
				res.add(ride);
			}
		}
		System.out.println("\u001B[37mRetrieved rides: " + res + "\u001B[0m");
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events.
	 *
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println("\u001B[37mRetrieving this month's dates with rides: from= " + from + ", to= " + to
				+ ", date= " + date + "\u001B[0m");

		List<Date> res = new ArrayList<>();
		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		for (Ride ride : rides) {
			if (ride.getDepartCity().equals(from) && ride.getDestinationCity().equals(to) && !ride.getDate().before(firstDayMonthDate)
					&& !ride.getDate().after(lastDayMonthDate)) {
				if (!res.contains(ride.getDate())) {
					res.add(ride.getDate());
				}
			}
		}
		System.out.println("\u001B[37mRetrieved dates with rides: " + res + "\u001B[0m");
		return res;
	}

	/**
	 * This method creates a driver.
	 *
	 * @param username the name of the driver
	 * @param password the password of the driver
	 * @param email    the email of the driver
	 * @return true if the driver was created, false otherwise
	 */
	public boolean createDriver(String username, String password, String email) {
		try {
			System.out.println("\u001B[37mCreating driver: username= " + username + ", email= " + email + "\u001B[0m");
			Driver driver = new Driver(email, username, password);
			drivers.add(driver);
			System.out.println("\u001B[37mCreated driver: " + driver + "\u001B[0m");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean createPassenger(String username, String password, String email) {
		try {
			System.out
					.println("\u001B[37mCreating Passenger: username= " + username + ", email= " + email + "\u001B[0m");
			Passenger passenger = new Passenger(email, username, password);
			passengers.add(passenger);
			System.out.println("\u001B[37mCreated Passenger: " + passenger + "\u001B[0m");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method retrieves a driver from the database.
	 *
	 * @param username the name of the driver
	 * @return the driver, or null if not found
	 */
	public Driver getDriver(String username) {
		for (Driver driver : drivers) {
			if (driver.getUsername().equals(username)) {
				System.out.println("\u001B[37mDriver found: " + driver + "\u001B[0m");
				return driver;
			}
		}
		System.out.println("\u001B[37mDriver not found for username: " + username + "\u001B[0m");
		return null;
	}

	public Passenger getPassenger(String username) {
		for (Passenger pass : passengers) {
			if (pass.getUsername().equals(username)) {
				System.out.println("\u001B[37mPassenger found: " + pass + "\u001B[0m");
				return pass;
			}
		}
		System.out.println("\u001B[37mPassenger not found for username: " + username + "\u001B[0m");
		return null;
	}

	public List<Ride> getRidesByDriver(Driver d) {
		List<Ride> lista = new ArrayList<Ride>();
		for (Ride r : rides) {
			if (r.getDriver().equals(d)) {
				lista.add(r);
			}
		}
		return lista;
	}
	
	public User getUser(String username) {
		User user = getDriver(username);
		if (user == null) {
			user = getPassenger(username);
		}
		return user;
	}
	
	public void deleteRide(Ride ride) {
		rides.remove(ride);
		ride.getDriver().removeRide(ride);
		for (Passenger p : ride.getPassengers()) {
			p.removeRide(ride);
        }
	}
	
	public void removePassengerFromRide(Passenger loggedInUser, Ride ride) {
		loggedInUser.removeRide(ride);
		ride.removePassenger(loggedInUser);
	}
	
}
