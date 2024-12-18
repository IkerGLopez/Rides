package modelo.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Ride implements Serializable {

	@XmlID
	@Id
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rideNumber;

	@Column(nullable = false)
	private String departCity;

	@Column(nullable = false)
	private String destinationCity;

	@Column(nullable = false)
	private int numPlaces;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(nullable = false)
	private float price;

	@ManyToOne
	private Driver driver;

	@ManyToMany
	private List<Passenger> passengers = new ArrayList<>();

	public Ride() {
		super();
	}

	public Ride(Integer rideNumber, String from, String to, Date date, int nPlaces, float price, Driver driver) {
		super();
		this.rideNumber = rideNumber;
		this.departCity = from;
		this.destinationCity = to;
		this.numPlaces = nPlaces;
		this.date = date;
		this.price = price;
		this.driver = driver;
	}

	public Ride(String from, String to, Date date, int nPlaces, float price, Driver driver) {
		super();
		this.departCity = from;
		this.destinationCity = to;
		this.numPlaces = nPlaces;
		this.date = date;
		this.price = price;
		this.driver = driver;
	}

	public Integer getRideNumber() {
		return rideNumber;
	}

	public void setRideNumber(Integer rideNumber) {
		this.rideNumber = rideNumber;
	}

	public String getDepartCity() {
		return departCity;
	}

	public void setDepartCity(String origin) {
		this.departCity = origin;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destination) {
		this.destinationCity = destination;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getNumPlaces() {
		return numPlaces;
	}

	public void setNumPlaces(int numPlaces) {
		this.numPlaces = numPlaces;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public void addPassenger(Passenger passenger) {
		passengers.add(passenger);
		this.setNumPlaces(this.getNumPlaces() - 1);
	}

	public void removePassenger(Passenger passenger) {
		passengers.remove(passenger);
		this.setNumPlaces(this.getNumPlaces() + 1);
	}

	public String toString() {
		return rideNumber + ";" + ";" + departCity + ";" + destinationCity + ";" + date;
	}

}
