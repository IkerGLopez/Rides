package modelo.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import modelo.domain.Driver;
import modelo.domain.Passenger;
import modelo.domain.User;

import java.io.Serializable;

@Named("SessionManagementBean")
@SessionScoped
public class SessionManagementBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public boolean login(User u) {
    	try {
    		FacesContext context = FacesContext.getCurrentInstance();
    		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
    		session.setAttribute("loggedInUser", u);
    		return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    public boolean logout() {
    	try {
        	FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        	return true;
		} catch (Exception e) {
			return false;
		}
    }

    public Object getLoggedInUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get("loggedInUser");
    }

    public String getUserRole() {
        Object user = getLoggedInUser();
        if (user instanceof Driver) {
            return "driver";
        } else if (user instanceof Passenger) {
            return "passenger";
        } else {
            return "unknown";
        }
    }

    public boolean isPassenger() {
        return "passenger".equals(getUserRole());
    }

    public boolean isDriver() {
        return "driver".equals(getUserRole());
    }
}
