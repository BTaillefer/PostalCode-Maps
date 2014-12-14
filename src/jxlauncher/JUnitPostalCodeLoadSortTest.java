package jxlauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.junit.*;
import static org.junit.Assert.*;
import postalcode.*;

/**
 * jUnit class to support testing of classes PostalCode and PostalCodeIndex
 * Partially complete . . . to be completed by students in CST8130
 * @author rex
 */
public class JUnitPostalCodeLoadSortTest {
  // The following static class-oriented fields are used in setUpClass().
  // The load time is quite lengthy, so the tests are designed to process the loading, once-and-only-once.
  // Other tests can follow afterward in any sequence.
  private static PostalCodeIndex postalCodeIndex;
  private static long originalChecksum; // Numeric representation of the cummulative data. Used to compare with parallel, sorted data sets.

  public JUnitPostalCodeLoadSortTest() {
  }

  @BeforeClass // performed once-and-only once before the test class is instantiated
  public static void setUpClass() {
    postalCodeIndex = new PostalCodeIndex();
    //postalCodeIndex.load(new File("postal_codes_short.csv")); // Tiny file for preliminary testing of processing sequence, not correctness of sort
      postalCodeIndex.load(new File("postal_codes.csv")); // Large 58 MB file
    originalChecksum = calculateChecksum(postalCodeIndex.getCodeOrder()); // Capture checksum of original UNsorted data.
  }

  @Ignore
  @AfterClass // performed once-and-only once after the test class is instantiated and after all test routines have terminated
  public static void tearDownClass() { }

  @Ignore
  @Before // performed before each test
  public void setUp() {
	  
  }

  @Ignore
  @After // performed after each test
  public void tearDown() { }

  @Test
  public void sortCityTest() {
	  Long citychecksum;
    // TODO Iterate through the entire, collection comparing adjacent items.
    // TODO An item at index [i] must always be less-than-or-equal to an item at [i+1].
    // TODO The BEST way to iterate is with an enhanced for loop, so that you could use the same code to test TreeSet organization (even though we are NOT using TreeSets for this).
    // TODO The enhan
    PostalCode previous = null; // on the first iteration, there will be NO previous item
    for (PostalCode current : postalCodeIndex.getCityOrder()) {
      if (previous == null) { // first iteration
        previous = current;   // capture current as the previous
        continue;             // skip the rest of the loop to get the next current . . . then you will have a legitimate previous and current
      } // end case of first record where we capture previous
      assertTrue(String.format("previous must be less than current %s %s",current.getCity(),previous.getCity()),previous.getCity().compareTo(current.getCity()) <= 0);
      // TODO implement your "assertTrue(...)" code here to identify what MUST be true for the test to be valid. You must fill in the ...
    } // end for
    	citychecksum = calculateChecksum(postalCodeIndex.getCityOrder());
    // TODO Iterate through the collection to calculate a "checksum".
    // TODO The "checksum" will accumul
  } // end sortCityTest()

  @Test
  public void sortLatitudeTest() {
	  Long latitudeCheckSum;
	  PostalCode previous = null;
	  
	  for(PostalCode current : postalCodeIndex.getLatitudeOrder()) {
		  if(previous == null) {
			  previous = current;
			  continue;
		  }
		  assertTrue(String.format("previous must be less than current %s %s",current.getLatitude(),previous.getLatitude()),previous.getLatitude()<=current.getLatitude());
	  }
	  latitudeCheckSum = calculateChecksum(postalCodeIndex.getLatitudeOrder());
    // TODO Create code to test
  } // end sortLatitudeTest()

  @Test
  public void sortLongitudeTest() {
	  Long longitudeCheckSum;
	  PostalCode previous = null;
	  
	  for(PostalCode current : postalCodeIndex.getLongitudeOrder()) {
		  if(previous == null) {
			  previous = current;
			  continue;
		  }
		  assertTrue(String.format("previous must be less than current %s %s", previous.getLongitude(),current.getLongitude()),previous.getLongitude() >= current.getLongitude());
	  }
	  longitudeCheckSum = calculateChecksum(postalCodeIndex.getLongitudeOrder());
	  
	  assertTrue("Checksum test", longitudeCheckSum == calculateChecksum(postalCodeIndex.getCodeOrder()));
    // TODO Create code to test
  } // end sortLatitudeTest()

  @Test
  public void sortCodeOrderTest() {
	Long codeOrderCheckSum;
	PostalCode previous = null;
	
	for(PostalCode current : postalCodeIndex.getCodeOrder()) {
		if(previous == null) {
			previous = current;
			continue;
		}
		assertTrue("Random code order",previous != current);
	}
	originalChecksum = calculateChecksum(postalCodeIndex.getCodeOrder());
    // TODO The original data is SUPPOSED to be in Code order, but is it?
    // TODO This @Test method will check that it is.
    // TODO Create code to test
  } // end sortLatitudeTest()


  /**
   * A static method that sweeps through the supplied list, performing a summation of the hashcode value for each item in the list.
   * The final long-value captures a unique value for all the aggregated data. It can be used to compare with checksums for other lists. 
   * If the checksums match, then the data sets are deemed to contain the same collection of items (though in a different order).
   * NOTE: This is NOT a jUnit @Test case. It is a helper method that is used by other parts of the jUnit class.
   *
   * @param list The List can be a reference to any of the lists.
   * @return
   */
  private static long calculateChecksum(ObservableList<PostalCode> list) {
	  Long checksum = 0L;
	  for (PostalCode check : list) {
		   checksum += (long) check.hashCode();
	  }
	  return checksum;
    // TODO Create a "long" variable to act as an accumulator for the "checksum"
    // TODO Iterate through the collection
    // TODO   add the hashCode() for each PostalCode object into the "checksum" 
    // TODO return the newly calculated checksum;
  } // end calculateChecksum()
} // end class JUnitPostalCodeLoadSortTest