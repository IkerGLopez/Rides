package modelo.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Passenger extends User implements Serializable {

	static final long serialVersionUID = 1L;
	
	public Passenger() {
		super();
	}

	public Passenger(String email, String name, String password) {
		super(email, name, password);
	}
	
	

}
