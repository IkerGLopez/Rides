package modelo.businessLogic;

import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

import modelo.domain.Driver;
import modelo.domain.Passenger;
import modelo.domain.Ride;
import modelo.domain.User;
import modelo.exceptions.RideMustBeLaterThanTodayException;
import modelo.exceptions.RideAlreadyExistException;
import modelo.dataAccess.DataAccessHibernate;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation implements BLFacade {
	private static BLFacadeImplementation instance;
	DataAccessHibernate dbManager;

	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementationMock instance");
		dbManager = new DataAccessHibernate();
	}

	public static BLFacadeImplementation getInstance() {
		if (instance == null) {
			instance = new BLFacadeImplementation();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<String> getDepartCities() {
		return dbManager.getDepartCities();
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<String> getDestinationCities(String from) {
		return dbManager.getDestinationCities(from);
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException, exceptions.RideAlreadyExistException, exceptions.RideMustBeLaterThanTodayException {
		return dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<Ride> getRides(String from, String to, Date date) {
		return dbManager.getRides(from, to, date);
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		return dbManager.getThisMonthDatesWithRides(from, to, date);
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public void initializeBD() {
		dbManager.initializeDB();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public boolean createDriver(String name, String email, String password) {
		return dbManager.createDriver(name, email, password);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public boolean createPassenger(String name, String email, String password) {
		return dbManager.createPassenger(name, email, password);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public Driver getDriver(String username) {
		return dbManager.getDriver(username);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public Passenger getPassenger(String username) {
		return dbManager.getPassenger(username);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<Ride> getRidesByDriver(Driver d) {
		return dbManager.getRidesByDriver(d);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public User getUser(String username) {
		return dbManager.getUser(username);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public void cancelRide(Ride ride) {
		dbManager.deleteRide(ride);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public void removePassengerFromRide(Passenger loggedInUser, Ride ride) {
		dbManager.removePassengerFromRide(loggedInUser, ride);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public void addPassengerToRide(Passenger p, Ride r) {
        dbManager.addPassengerToRide(p, r);
    }
	
}
