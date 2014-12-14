package test;

import java.io.File;
import postalcode.PostalCodeIndex;

public class testLoading {

	public static void main(String[] args) {
		PostalCodeIndex postalCodeIndex = new PostalCodeIndex();
		postalCodeIndex.load(new File("postal_codes.csv"));
		//System.out.println("Done");
		
		/*
		double randomLat = (Math.random() * 38.225) + 41.765;
		double randomLong = (Math.random()*-88.152) + -40.730;
		
		System.out.printf("Random Lat: %f\nRandom Long: %f\n", randomLat,randomLong);
		*/
		postalCodeIndex.calculateBoundingBox(47.537,-53.91);
		postalCodeIndex.getSubListWithinRange(postalCodeIndex.getmaxLat(), postalCodeIndex.getminLat(), postalCodeIndex.getmaxLong(), postalCodeIndex.getminLong());
		System.out.println(postalCodeIndex.findClosestCoordinate(47.537,-43.91));
		
	}

}
