package modelo.bean;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import modelo.domain.Ride;
import modelo.businessLogic.BLFacade;

@Named("Register")
@SessionScoped
public class RegisterBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    private String passwordConfirmation;
    private String email;
    private BLFacade bl;
    private String role;
    
    public RegisterBean() {
		try {
			bl = BLFacadeImplementationBean.getInstance();
		} catch (Exception e) {
			System.out.println("Failed to obtain BLFacadeImplementation instance.");
			e.printStackTrace();
		}
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public BLFacade getBl() {
		return bl;
	}

	public void setBl(BLFacade bl) {
		this.bl = bl;
	}
	public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String makeRegister() {
        if (password != null && !password.equals(passwordConfirmation)) {
            FacesContext.getCurrentInstance().addMessage(null,
            		new FacesMessage("Las contraseñas no coinciden."));
            return null;
        }
        if(role.equals("driver")) {
        	bl.createDriver(username, password, email);
        }else {
        	bl.createPassenger(username, password, email);
        }
        
        return "RegisterConfirmation";
    }
}