package modelo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

//@Named("login")
@SessionScoped
public class MainBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public MainBean() {
		
	}
}
