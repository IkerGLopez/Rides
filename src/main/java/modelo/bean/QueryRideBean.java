package modelo.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import modelo.businessLogic.BLFacade;
import modelo.domain.Driver;
import modelo.domain.Passenger;
import modelo.domain.Ride;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("QueryRide")
@SessionScoped
public class QueryRideBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String selectedDepartCity;
	private String selectedDestinationCity;
	private float selectedPrice;
	private int selectednPlaces;
	private String selectedDriverName;
	private Ride selectedRide;
	private List<String> departCities;
	private List<String> destinationCities;
	private List<Date> availableDates;
	private Date selectedDate;
	private List<Ride> rides = new ArrayList<>();
	private BLFacade bl;

	@Inject
	private SessionManagementBean sessionManagementBean;

	public QueryRideBean() {
		try {
			bl = BLFacadeImplementationBean.getInstance();
			rides = new ArrayList<>();
			System.out.println("BLFacadeImplementation instance obtained successfully.");
		} catch (Exception e) {
			System.out.println("Failed to obtain BLFacadeImplementation instance.");
			e.printStackTrace();
		}
	}

	public String getSelectedDriverName() {
		return selectedDriverName;
	}

	public void setSelectedDriverName(String selectedDriverName) {
		this.selectedDriverName = selectedDriverName;
	}

	public Ride getSelectedRide() {
		return selectedRide;
	}

	public void setSelectedRide(Ride selectedRide) {
		this.selectedRide = selectedRide;
	}

	public float getSelectedPrice() {
		return selectedPrice;
	}

	public void setSelectedPrice(float selectedPrice) {
		this.selectedPrice = selectedPrice;
	}

	public int getSelectednPlaces() {
		return selectednPlaces;
	}

	public void setSelectednPlaces(int selectednPlaces) {
		this.selectednPlaces = selectednPlaces;
	}

	@PostConstruct
	public void initializeDepartCities() {
		if (bl != null) {
			departCities = bl.getDepartCities();
			System.out.println("Depart cities initialized");
		} else {
			departCities = new ArrayList<>();
			System.out.println("BLFacade is null, unable to initialize depart cities.");
		}
	}

	public String getSelectedDepartCity() {
		return selectedDepartCity;
	}

	public void setSelectedDepartCity(String departCity) {
		this.selectedDepartCity = departCity;
	}

	public String getSelectedDestinationCity() {
		return selectedDestinationCity;
	}

	public void setSelectedDestinationCity(String destinationCity) {
		this.selectedDestinationCity = destinationCity;
	}

	public List<String> getDepartCities() {
		return departCities;
	}

	public void setDepartCities(List<String> departCities) {
		this.departCities = departCities;
	}

	public List<String> getDestinationCities() {
		return destinationCities;
	}

	public void setDestinationCities(List<String> destinationCities) {
		this.destinationCities = destinationCities;
	}

	public List<Date> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<Date> availableDates) {
		this.availableDates = availableDates;
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public void setSelectedDate(Date fecha) {
		this.selectedDate = fecha;
	}

	public BLFacade getBl() {
		return bl;
	}

	public void setBl(BLFacade bl) {
		this.bl = bl;
	}

	public void updateDestinationCities(AjaxBehaviorEvent event) {
		if (selectedDepartCity != null && !selectedDepartCity.isEmpty()) {
			this.setDestinationCities(bl.getDestinationCities(selectedDepartCity));

			this.selectedDate = new Date();

			updateAvailableDates(null);
		} else {
			this.setDestinationCities(Collections.emptyList());
		}
	}

	public void updateAvailableDates(AjaxBehaviorEvent event) {
		if (selectedDepartCity != null && selectedDestinationCity != null && selectedDate != null) {
			List<Date> rawDates = bl.getThisMonthDatesWithRides(selectedDepartCity, selectedDestinationCity,
					selectedDate);
			availableDates = new ArrayList<Date>();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for (Date date : rawDates) {
				try {
					availableDates.add(sdf.parse(sdf.format(date)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void obtainRides(AjaxBehaviorEvent event) {
		List<Ride> ridesDisp = new ArrayList<Ride>();
		System.out.println(selectedDate);
		if (selectedDepartCity != null && selectedDestinationCity != null && selectedDate != null) {
			ridesDisp = bl.getRides(selectedDepartCity, selectedDestinationCity, selectedDate);
			if (ridesDisp != null && !ridesDisp.isEmpty()) {
				for (Ride ride : ridesDisp) {
					ride.toString();
				}
				this.rides = ridesDisp;
			} else {
				ridesDisp = new ArrayList<Ride>();
				this.rides = ridesDisp;
				System.out.println("No rides found.");
			}
		}
	}

	public String close() {
		String role = sessionManagementBean.getUserRole();

		if ("driver".equals(role)) {
			return "MainDriver.xhtml?faces-redirect=true";
		} else {
			return "MainPassenger.xhtml?faces-redirect=true";
		}
	}

	public boolean isPassenger() {
		String role = sessionManagementBean.getUserRole();
		return "passenger".equals(role);
	}

	public String book() {
		Passenger passenger = loggedPassenger();

		Ride rideToBook = selectedRide;

		for (Ride r : passenger.getRides()) {
			if (r.getDate().equals(rideToBook.getDate())) {
				return null;
			}
		}

		bl.addPassengerToRide(passenger, rideToBook);
		return null;
	}

	public boolean isRideBookedForSelectedDate() {
		if (sessionManagementBean.getUserRole().equals("passenger")) {
			Passenger p = loggedPassenger();

			if (selectedDate != null) {
				for (Ride r : p.getRides()) {
					if (r.getDate().equals(selectedDate)) {
						return true;
					}
				}
			}
			return false;
		} else
			return false;
	}
	
	public Passenger loggedPassenger() {
		Passenger user = (Passenger) sessionManagementBean.getLoggedInUser();
		String username = user.getUsername();
		return bl.getPassenger(username);
	}

}
