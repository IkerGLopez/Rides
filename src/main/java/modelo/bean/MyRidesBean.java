package modelo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelo.domain.Driver;
import modelo.domain.Passenger;
import modelo.domain.Ride;
import modelo.domain.User;
import modelo.businessLogic.BLFacade;

@Named("MyRides")
@SessionScoped
public class MyRidesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private User user;
    private List<Ride> rides;
    private BLFacade bl;
    
    
    @Inject
    private SessionManagementBean sessionManagementBean;

    public MyRidesBean() {
		try {
			bl = BLFacadeImplementationBean.getInstance();
			System.out.println("BLFacadeImplementation instance obtained successfully.");
		} catch (Exception e) {
			System.out.println("Failed to obtain BLFacadeImplementation instance.");
			e.printStackTrace();
		}
	}


    public boolean isDriver() {
        return user instanceof Driver;
    }

    public boolean isPassenger() {
        return user instanceof Passenger;
    }

    public void cancelRide(Ride ride) {
        if (isDriver()) {
            bl.cancelRide(ride);
        } else if (isPassenger()) {
            bl.removePassengerFromRide((Passenger) user, ride);
        }
        rides = user.getRides();
    }

    public List<Ride> getRides() {
    	user =  (User) sessionManagementBean.getLoggedInUser(); 
 		String username = user.getUsername();
 		user = bl.getUser(username);
    	rides = user.getRides();
        return rides;
    }
}