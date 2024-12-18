package modelo.bean;

import java.io.Serializable;
import java.util.Date;

import modelo.businessLogic.BLFacade;
import modelo.domain.Driver;
import modelo.domain.Ride;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
//import modelo.mocks.BLFacadeImplementationMock;

@Named("CreateRide")
@SessionScoped
public class CreateRideBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String departCity;
	private String arrivalCity;
	private int numSeats;
	private float price;
	private Date fecha;
	private BLFacade bl;

	@Inject
	private SessionManagementBean sessionManagementBean;

	public CreateRideBean() {
		try {
			bl = BLFacadeImplementationBean.getInstance();
			System.out.println("BLFacadeImplementationMock instance created successfully.");
			System.out.println(bl.getDepartCities().toString());
		} catch (Exception e) {
			System.out.println("Failed to create BLFacadeImplementationMock instance.");
			e.printStackTrace();
		}
	}

	public String getDepartCity() {
		return departCity;
	}

	public void setDepartCity(String departCity) {
		this.departCity = departCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public int getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void validateAndCreateRide() {
		FacesContext context = FacesContext.getCurrentInstance();
		if ((context.getMaximumSeverity() == null) && comprobar().equals("ok")) {
			createRide();
		}
	}

	public Ride createRide() {
		try {
			Driver loggedInDriver = (Driver) sessionManagementBean.getLoggedInUser();
			String driverEmail = loggedInDriver.getEmail();
			Ride ride = bl.createRide(departCity, arrivalCity, fecha, numSeats, price, driverEmail);

			String info = String.format("Tu oferta se ha publicado correctamente", departCity, arrivalCity, numSeats,
					price, fecha.toString());

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, info, info));

			return ride;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear la oferta.", e.getMessage()));
			return null;
		}
	}

	public String comprobar() {
		if (departCity.equals(arrivalCity)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Las ciudades de salida y destino no pueden ser iguales."));
			return "error";
		}
		return "ok";
	}

}
