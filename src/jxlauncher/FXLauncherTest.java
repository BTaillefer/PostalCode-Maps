package jxlauncher;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;

import postalcode.PostalCode;
import postalcode.PostalCodeIndex;

public class FXLauncherTest {
	
	private static PostalCodeIndex postalCodeIndex;
	private static long originalCheckSum;
	
	public JUnitPostalCodeLoadSortTest() {
			
	}
	
	public void sortTest() {
		
	}
		
	}

	@Before
	public void setUpClass() throws Exception {
		postalCodeIndex = new PostalCodeIndex();
		try {
			postalCodeIndex.load(new File("postal_codes.csv"));
		}
		catch (FileNotFoundException ex) {
			Logger.getLogger(JUnitPostalCodeLoadSortTest().class.getName().log(Level.SEVERE,null,ex));
		}
	}
	private boolean validateLatitudeSort() {
	
		PostalCode previous = null;
	for(PostalCode current : postalCodeIndex.getCityOrder()) {
		if(previous == null) {
			previous = current;
			continue;
		}
		if(previous.getCityOrder()).compareTo(current.getCity() > 0) {
	System.err.printf(arg0, arg1);
	return false;
}
}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
