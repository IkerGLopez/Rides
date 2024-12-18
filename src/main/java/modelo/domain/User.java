package modelo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlID
	@Id
	private String email;
	private String username;
	private String password;
	@XmlIDREF
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Ride> rides = new ArrayList<Ride>();

	public User() {
		super();
	}

	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String toString() {
		return email + ";" + username + rides;
	}

	public Ride addRide(Ride ride) {
		rides.add(ride);
		return ride;
	}
	
	public void removeRide(Ride ride) {
		rides.remove(ride);
	}

	public boolean doesRideExists(String from, String to, Date date) {
		for (Ride r : rides)
			if ((java.util.Objects.equals(r.getDepartCity(), from)) && (java.util.Objects.equals(r.getDestinationCity(), to))
					&& (java.util.Objects.equals(r.getDate(), date)))
				return true;

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username != other.username)
			return false;
		return true;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public Ride removeRide(String from, String to, Date date) {
		boolean found = false;
		int index = 0;
		Ride r = null;
		while (!found && index <= rides.size()) {
			r = rides.get(++index);
			if ((java.util.Objects.equals(r.getDepartCity(), from)) && (java.util.Objects.equals(r.getDestinationCity(), to))
					&& (java.util.Objects.equals(r.getDate(), date)))
				found = true;
		}

		if (found) {
			rides.remove(index);
			return r;
		} else
			return null;
	}

}
