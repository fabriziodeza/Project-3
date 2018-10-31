import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

public class MapDataTest {

	MapData test = new MapData(2018, 8, 30, 17, 45, "data");
	

	
	@Test
	public void createFileNameTest() {
		String actual = test.createFileName(2018, 8, 30, 17, 45, "data");
		String expected = "data/201808301745.mdf";
		assertEquals(expected, actual);
	}

	@Test
	public void testGetIndexOf() throws IOException {
		test.parseFile();
		Integer actual = test.getIndexOf("STID");
		Integer expected = 0;
		assertEquals(actual, expected);
	}

	@Test
	public void testGetStatistics() throws IOException {
		test.parseFile();
		Statistics actual = test.getStatistics(StatsType.MAXIMUM, "TAIR");
		Statistics expected = test.getStatistics(StatsType.MAXIMUM, "TAIR");;
		//new Statistics(0, null, null, 0, null);
		
		assertEquals(actual, expected);
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
