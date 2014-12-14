package postalcode;
import java.util.Comparator;
import java.util.Scanner;

import javafx.collections.ObservableList;
/**
 * PostalCode class used to store, parse, and compare PostalCode objects from the input file. The PostalCode constructor parses the 
 * file data and initializes the instance variables.This class also includes the Comparator methods used to teach the program how to compare
 * instance variables.
 * @author Brodie
 *
 */

public class PostalCode {
	private String postalCode;
	private String city;
	private String province;
	private double latitude;
	private double longitude;

	
	//PostalCode constructor used to to initialize and read in the instance variables using the file selected
	public PostalCode(Scanner fileInput) {
		String firstField = fileInput.next();// abandon the first field which is merely a line count
		postalCode = fileInput.next();
		city = fileInput.next().intern(); //intern() takes the String instance and checkes to see if a copy of the String value already exists in the StringTable
		province = fileInput.next().intern();//intern() takes the String instance and checkes to see if a copy of the String value already exists in the StringTable
		fileInput.next(); //abandon the abbreviated province
		fileInput.next(); //abandon redundant field
		latitude = fileInput.nextDouble();
		longitude = fileInput.nextDouble();
	}
	
	public PostalCode(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// toString representation for PostalCode objects
	public String toString() {
		return String.format("%s %s %s %f %f",postalCode,city,province,latitude,longitude);
	}
	// Return city
	public String getCity() {
		return city;
	}
	//Return province
	public String getProvince() {
		return province;
	}
	//Return latitude
	public double getLatitude() {
		return latitude;
	}
	//Return Longitude
	public double getLongitude () {
		return longitude;
	}
	//Return PostalCOde
	public String getPostalCode() {
		return postalCode;
	}
		
	//Comparator method to compare latitudes
	public static class CompareLatitude implements Comparator<PostalCode>  {
		public static final CompareLatitude instance = new CompareLatitude();
		CompareLatitude() {
		}
		public int compare(PostalCode code1, PostalCode code2) {
			return Double.compare(code1.latitude, code2.latitude);
		}
	}
	
	//Comparator method to compare Longitude
	public static class CompareLongitude implements Comparator<PostalCode> {
		public static final CompareLongitude instance = new CompareLongitude();
		private CompareLongitude() {
		}
		public int compare(PostalCode code1, PostalCode code2) {
			return Double.compare(code2.longitude, code1.longitude);
		}
	}
	

}



