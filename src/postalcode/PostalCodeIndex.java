package postalcode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import postalcode.PostalCode.CompareLatitude;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Class PostalCodeIndex used to initialize the ObservableList objects and sort the lists. Five observable lists are created for each instance variable that needs to be
 * sorted. Getter methods for each postalCode are created to return the ObservableList. Method load, 
 * @author Brodie
 *
 */
public class PostalCodeIndex {
	private double minLat,maxLat,minLong,maxLong;
	private Set<PostalCode> uniqueSearchSet;
	//Create ObservableList's for each instance variable that must be sorted by
	private ObservableList<PostalCode> codeOrder = FXCollections.observableList(new ArrayList<PostalCode>(760000));
	private ObservableList<PostalCode> latitudeOrderList = FXCollections.observableArrayList(new ArrayList<PostalCode>(760000));
	private ObservableList<PostalCode> longitudeOrderList = FXCollections.observableArrayList(new ArrayList<PostalCode>(760000));

	// Getter methods to return reference to each ObservableList object.
	public ObservableList<PostalCode> getCodeOrder() { return FXCollections.unmodifiableObservableList(codeOrder); }
	public ObservableList<PostalCode> getLatitudeOrder() { return FXCollections.unmodifiableObservableList(latitudeOrderList); }
	public ObservableList<PostalCode> getLongitudeOrder() { return FXCollections.unmodifiableObservableList(longitudeOrderList); }
	public double getminLat() { return minLat; }
	public double getmaxLat() { return maxLat; }
	public double getminLong() { return minLong; }
	public double getmaxLong() { return maxLong; }
	
	// Scan in input file and parse data into codeOrder
	public void load(File file) {
		try (Scanner fileInput = new Scanner(file)) {
			fileInput.useDelimiter("\\||\\r\\n");
			fileInput.nextLine();
			while (fileInput.hasNext()) 
				codeOrder.add(new PostalCode(fileInput));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//copy codeOrder into latitudeOrderList, making an exact copy of the original array
		latitudeOrderList = FXCollections.observableArrayList(codeOrder);
		//sort latitudeOrderList with our comparator method
		Collections.sort(latitudeOrderList,PostalCode.CompareLatitude.instance);
		//copy codeOrder into longitudeOrderList, making an exact copy of the original array
		longitudeOrderList = FXCollections.observableArrayList(codeOrder);
		//sort longitudeOrderList with our comparator method
		Collections.sort(longitudeOrderList,PostalCode.CompareLongitude.instance);

	}
	/**Method to find closest PostalCOde object with our unique search set, checks to see if the PostalCode object is within the correct boundries,
	 * @param latitude
	 * @param longitude
	 * @return closestPostalCode
	 */
	public PostalCode findClosestCoordinate(double latitude, double longitude) {
		double closestDistance = Double.MAX_VALUE;
		PostalCode closestPostalCode = null;
		double distance;
		for(PostalCode postalCode : uniqueSearchSet) { //Search uniqueSearchSet
			if(postalCode.getLongitude() < minLong && postalCode.getLongitude() > maxLong && postalCode.getLatitude() > minLat && postalCode.getLatitude() <maxLat){ //Check to see if the PostalCode lat/long is between bonds
				distance = distance(latitude,longitude,postalCode.getLatitude(),postalCode.getLongitude(),'K'); //Search for the distance between the two points
				if(distance < closestDistance) { 
					closestDistance = distance;
					closestPostalCode = postalCode;
				}	
			}
		}
		if(closestPostalCode == null ) {
			closestPostalCode = findClosestCoordinateBruteForce(latitude,longitude);
		}
		return closestPostalCode;	
	}
	
	/** Find the closest coordinate from the given set by brute force, go through all values in the latitudeOrderList
	 * and check each PostalCode's distance from the set Lat/Long, not very effective BigO(n^2) for n items.
	 * @param latitude
	 * @param longitude
	 * @return closestPostalCode
	 */
	public PostalCode findClosestCoordinateBruteForce(double latitude, double longitude) {
		double closestDistance = Double.MAX_VALUE; 
		PostalCode closestPostalCode = null;
		double distance;
		for(PostalCode postalCode : getLatitudeOrder()) {
			distance = distance(latitude,longitude,postalCode.getLatitude(),postalCode.getLongitude(),'K');
			if(distance < closestDistance) {
				closestDistance = distance;
				closestPostalCode = postalCode;
			}	
		}
		//System.out.printf("Closest Distance: %f\n", closestDistance);
		return closestPostalCode;	
	}
	
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /*::  Calculate the Difference Between two lats and longs   				 */
  /*     Code Taken from stack overflow, user dommer								   */
  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
    double theta = lon1 - lon2;
    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;
    if (unit == 'K') {
      dist = dist * 1.609344;
    } else if (unit == 'N') {
      dist = dist * 0.8684;
      }
    return (dist);
  }
	
  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /*::  This function converts decimal degrees to radians 						 */
  /* 		Code Taken from stack overflow, user dommer								    :*/
  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  private double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /*::  This function converts radians to decimal degrees             :*/
  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  private double rad2deg(double rad) {
    return (rad * 180.0 / Math.PI);
  }
 
  /**Calculate the shape around the given lat/long set and determine the min/max Long/Lat coordinates to search in, defining our search from the
   * 3/4th million records to a more defined list. Formula's taken from stack overflow, for latitude/Longitude per km.
   * 
   * @param latitude
   * @param longitude
   */
	public void calculateBoundingBox(double latitude, double longitude) {
		double r = 300;
		minLat = latitude - r / 110.54;  //Calculate the min latitude of the box
		minLong = longitude + (r/111.320 *Math.cos(Math.toRadians(longitude))); //Calculate the min latitude of the box
		maxLat = latitude + (r/ 110.54); //Calculate the max latitude of the box
		maxLong = longitude - (r/111.320 *Math.cos(Math.toRadians(longitude))); //Calculate the max Longitude of the box
		if(maxLong > minLong) { //On higher longitudes the values can get switched, so switch them back
			double tempY = minLong;
			minLong = maxLong;
			maxLong = tempY;
		}
	}

	/** Take the box dimensions we created in calculateBoundingBox() and get the index where they would be location in the ArrayLists,
	 * take the locations and get a sublist, and add them into a hashset. This uniqueSearchSet will than be searched over for the closest
	 * location
	 * 
	 * @param maxLatitude
	 * @param minLatitude
	 * @param maxLongitude
	 * @param minLongitude
	 */
	public void getSubListWithinRange(double maxLatitude, double minLatitude, double maxLongitude, double minLongitude) {
		PostalCode postalCode = new PostalCode(minLatitude, 0); //Create a PostalCode object with the minLatitude Value
		int startPointLatitude  = Math.abs(Collections.binarySearch(getLatitudeOrder(), postalCode, PostalCode.CompareLatitude.instance)); //Find where that object would be located in the latitudeorder
		postalCode = new PostalCode(maxLatitude,0); //Create a PostalCode object with the maxLatitude value
		int endPointLatitude = Math.abs(Collections.binarySearch(getLatitudeOrder(),postalCode, PostalCode.CompareLatitude.instance));//Find index where it would exist
		postalCode = new PostalCode(0,minLongitude); //Create a PostalCode object with minLongitude 
		int startPointLongitude = Math.abs(Collections.binarySearch(getLongitudeOrder(), postalCode, PostalCode.CompareLongitude.instance)); //Find index where it would exist
		postalCode = new PostalCode(0,maxLongitude); //Create a PostalCode object with maxLongitude
		int endPointLongitude = Math.abs(Collections.binarySearch(getLongitudeOrder(), postalCode, PostalCode.CompareLongitude.instance)); //Find index where it would exist
		if(endPointLatitude  >= 765345) { endPointLatitude = getLatitudeOrder().size()-1; } //If the end is larger than the index, reset to max index
		if(endPointLongitude  >= 763545) { endPointLongitude = getLongitudeOrder().size()-1; } //If the end is larger than the index, reset to max index
		if(startPointLatitude >= 763545) { startPointLatitude = getLatitudeOrder().size()-300; } //if the start is larger than the index, reset to 300 away from max index
		if(startPointLongitude >= 763545) { startPointLongitude = getLongitudeOrder().size()-300; } //if the start is larger than the index, reset to 300 away from max index
		uniqueSearchSet = new HashSet<PostalCode>(getLatitudeOrder().subList(startPointLatitude, endPointLatitude)); //Initialize the HashSet with the sublist of startPointLatitude
		uniqueSearchSet.addAll(getLongitudeOrder().subList(startPointLongitude, endPointLongitude));//Add sublist of getLongitudeOrder to the HashSet
	}
}


	
	


