package modelo.bean;

import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelo.domain.Driver;
import modelo.domain.User;
import modelo.businessLogic.BLFacade;

@Named("Login")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private BLFacade bl;
	
	@Inject
    private SessionManagementBean sessionManagementBean;

	public LoginBean() {
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

	public BLFacade getBl() {
		return bl;
	}

	public void setBl(BLFacade bl) {
		this.bl = bl;
	}
	
	public String makeLogin() {
		FacesContext context = FacesContext.getCurrentInstance();

		User u = bl.getUser(username);
		
		if (u == null) {
			context.addMessage(null, new FacesMessage("El nombre de usuario introducido no existe."));
			return null;
		}
		
		if (!u.getPassword().equals(password)) {
			context.addMessage(null, new FacesMessage("La contraseña introducida no es correcta."));
			return null;
		}
		
		sessionManagementBean.login(u);
		
		if (u instanceof Driver) {
			return "MainDriver.xhtml?faces-redirect=true";
		} else {
			return "MainPassenger.xhtml?faces-redirect=true";
		}
		
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean success = sessionManagementBean.logout();
		if (success) {
			return "Login.xhtml?faces-redirect=true";
		} else {
			context.addMessage(null, new FacesMessage("Error al cerrar la sesión"));
			return null;
		}
	}

}