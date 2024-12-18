package modelo.businessLogic;

import java.util.Date;
import java.util.List;

//import domain.Booking;
import modelo.domain.Ride;
import modelo.domain.User;
import modelo.domain.Driver;
import modelo.domain.Passenger;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade {

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	@WebMethod
	public List<String> getDepartCities();

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	@WebMethod
	public List<String> getDestinationCities(String from);

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from    the origin location of a ride
	 * @param to      the destination location of a ride
	 * @param date    the date of the ride
	 * @param nPlaces available seats
	 * @param driver  to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 * @throws modelo.exceptions.RideMustBeLaterThanTodayException 
	 * @throws modelo.exceptions.RideAlreadyExistException 
	 */
	@WebMethod
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException, modelo.exceptions.RideMustBeLaterThanTodayException, modelo.exceptions.RideAlreadyExistException;

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	@WebMethod
	public List<Ride> getRides(String from, String to, Date date);

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	@WebMethod
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);

	/**
	 * This method calls the data access to initialize the database with some events
	 * and questions. It is invoked only when the option "initialize" is declared in
	 * the tag dataBaseOpenMode of resources/config.xml file
	 */
	@WebMethod
	public void initializeBD();

	/**
	 * This method creates a driver.
	 *
	 * @param username the name of the driver
	 * @param password the password of the driver
	 * @param email    the email of the driver
	 * @return true if the driver was created, false otherwise
	 */
	@WebMethod
	public boolean createDriver(String name, String email, String password);

	/**
	 * This method creates a passenger.
	 *
	 * @param name     the name of the passenger
	 * @param email    the email of the passenger
	 * @param password the password of the passenger
	 * @return true if the passenger was created, false otherwise
	 */
	@WebMethod
	public boolean createPassenger(String name, String email, String password);

	/**
	 * This method retrieves a driver from the database.
	 *
	 * @param username the name of the driver
	 * @return the driver
	 */
	@WebMethod
	public Driver getDriver(String username);

	/**
	 * This method retrieves a passenger from the database.
	 *
	 * @param username the name of the passenger
	 * @return the passenger
	 */
	@WebMethod
	public Passenger getPassenger(String username);

	/**
	 * This method retrieves all rides associated with a given driver.
	 *
	 * @param d the driver whose rides are to be retrieved
	 * @return a list of rides associated with the driver
	 */
	@WebMethod
	public List<Ride> getRidesByDriver(Driver d);
	
	@WebMethod
	public User getUser(String username);

	@WebMethod
	public void cancelRide(Ride ride);

	@WebMethod
	public void removePassengerFromRide(Passenger loggedInUser, Ride ride);
	
	@WebMethod	
	public void addPassengerToRide(Passenger p, Ride r);
}
