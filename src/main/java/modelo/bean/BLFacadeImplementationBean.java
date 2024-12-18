package modelo.bean;

import modelo.businessLogic.BLFacade;
import jakarta.enterprise.context.SessionScoped;
import modelo.businessLogic.BLFacadeImplementation;

import java.io.Serializable;

@SessionScoped
public class BLFacadeImplementationBean implements Serializable {
    private static final long serialVersionUID = 1L;
	
	private static final BLFacade instance = new BLFacadeImplementation();	


	public static BLFacade getInstance() {
		return instance;
	}
	
} 