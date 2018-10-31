import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class MapData {

	private HashMap<String, ArrayList<Observation>> dataCatalog = new HashMap<String, ArrayList<Observation>>();
	private EnumMap<StatsType, TreeMap<String, Statistics>> statistics = new EnumMap<StatsType, TreeMap<String, Statistics>>(
			StatsType.class);
	private TreeMap<String, Integer> paramPositions = null;

	private int NUMBER_OF_MISSING_OBSERVATIONS = 10;
	private Integer numberOfStations = null;
	private String TAIR = "TAIR";
	private String TA9M = "TA9M";
	private String SRAD = "SRAD";
	private String STID = "STID";
	private String MESONET = "Mesonet";
	private String fileName;
	private GregorianCalendar utcDateTime;

	private String directory;

	// Creates the MapData object
	public MapData(int year, int month, int day, int hour, int minute, String directory) {

		this.directory = directory;

		utcDateTime = new GregorianCalendar(year, month, day, hour, minute);

	}

	public String createFileName(int year, int month, int day, int hour, int minute, String directory) {

		fileName = String.format("%s/%04d%02d%02d%02d%02d.mdf", directory, year, month, day, hour, minute);

		return fileName;

	}

	// Saves string name (srad, tair, etc) and assigns it a number paramid

	public Integer getIndexOf(String inParamStr) {
		
		int test = paramPositions.get(inParamStr);
		return test;

	}
	
	private void parseParamHeader(String inParamStr) {

		paramPositions = new TreeMap<String, Integer>();
		numberOfStations = 0;
		String[] temp = inParamStr.trim().split("\\s+");
		for (int i = 0; i < temp.length; i++) {

			numberOfStations++;
			paramPositions.put(temp[i], i);
		}
	}

	private void prepareDataCatalog() {
		HashMap<String, ArrayList<Observation>> dataCatalog = new HashMap<String, ArrayList<Observation>>();
	}

	public void parseFile() throws IOException {

		ArrayList<Observation> sradData = new ArrayList<Observation>();
		ArrayList<Observation> tairData = new ArrayList<Observation>();
		ArrayList<Observation> ta9mData = new ArrayList<Observation>();

		BufferedReader br = new BufferedReader(new FileReader(this.createFileName(utcDateTime.get(Calendar.YEAR),
				utcDateTime.get(Calendar.MONTH), utcDateTime.get(Calendar.DATE), utcDateTime.get(Calendar.HOUR_OF_DAY),
				utcDateTime.get(Calendar.MINUTE), directory)));

		br.readLine();
		br.readLine();

		parseParamHeader(br.readLine());

		// int count = 0;

		String record = br.readLine();

		while (record != null) {

			String[] temp = record.trim().split("\\s+");

			sradData.add(new Observation(Double.parseDouble(temp[getIndexOf(SRAD)]), temp[getIndexOf(STID)]));
			tairData.add(new Observation(Double.parseDouble(temp[getIndexOf(TAIR)]), temp[getIndexOf(STID)]));
			ta9mData.add(new Observation(Double.parseDouble(temp[getIndexOf(TA9M)]), temp[getIndexOf(STID)]));

			// Both double and value saved and souted so thats good

			record = br.readLine();

		}

		br.close();

		prepareDataCatalog();

		dataCatalog.put(SRAD, sradData);
		dataCatalog.put(TAIR, tairData);
		dataCatalog.put(TA9M, ta9mData);

		calculateStatistics();

	}

	private void calculateStatistics() {

		calculateAllStatistics();

	}

	

	private void calculateAllStatistics() {

		TreeMap<String, Statistics> treeMapMax = new TreeMap<String, Statistics>();
		TreeMap<String, Statistics> treeMapMin = new TreeMap<String, Statistics>();
		TreeMap<String, Statistics> treeMapAvg = new TreeMap<String, Statistics>();

		Set<String> parameterId = dataCatalog.keySet();

		for (String paramId : parameterId) {

			ArrayList<Observation> data = dataCatalog.get(paramId);

			
			Statistics tempAvg = new Statistics(0, paramId, utcDateTime, NUMBER_OF_MISSING_OBSERVATIONS, null);
			Statistics tempMax = new Statistics(0, paramId, utcDateTime, NUMBER_OF_MISSING_OBSERVATIONS, null);
			Statistics tempMin = new Statistics(1000, paramId, utcDateTime, NUMBER_OF_MISSING_OBSERVATIONS, null);

			double total = 0;
			for (Observation obs : data) {

				double tempValue = obs.getValue();
						
				if (obs.isValid() && data != null) {
					total = total + tempValue;
				}
				
			
				if (obs.isValid() && obs != null && obs.getValue() > tempMax.getValue()) {
					tempMax = new Statistics(tempValue, obs.getStid(), utcDateTime, numberOfStations, StatsType.MAXIMUM);
				}

				
				if (obs.isValid() && obs != null && obs.getValue() < tempMin.getValue()) {
					tempMin = new Statistics(tempValue, obs.getStid(), utcDateTime, numberOfStations, StatsType.MINIMUM);
				}

			}
			
			double Avg = total / data.size();
			tempAvg = new Statistics(Avg, MESONET, utcDateTime, numberOfStations, StatsType.AVERAGE);
			
			if (paramId == TA9M) {
				treeMapMin.put(paramId, tempMin);
				treeMapMax.put(paramId, tempMax);
				treeMapAvg.put(paramId, tempAvg);
			} else if (paramId == TAIR) {
				treeMapMin.put(paramId, tempMin);
				treeMapMax.put(paramId, tempMax);
				treeMapAvg.put(paramId, tempAvg);
			} else if (paramId == SRAD) {
				treeMapMin.put(paramId, tempMin);
				treeMapMax.put(paramId, tempMax);
				treeMapAvg.put(paramId, tempAvg);			
			}
			
			statistics.put(StatsType.MAXIMUM, treeMapMax);
			statistics.put(StatsType.MINIMUM, treeMapMin);
			statistics.put(StatsType.AVERAGE, treeMapAvg);
			
		}
		
		
	}

	public Statistics getStatistics(StatsType type, String paramid) {

		return statistics.get(type).get(paramid);

	}

	public String toString() {

		String thing =

				("=========================================================\n" + "===" + " "
						+ String.format("%04d" + "-" + "%02d" + "-" + "%02d  ", utcDateTime.get(Calendar.YEAR),
								utcDateTime.get(Calendar.MONTH), utcDateTime.get(Calendar.DATE))
						+

						String.format("%02d" + ":" + "%02d" + " ===\n", utcDateTime.get(Calendar.HOUR_OF_DAY),
								utcDateTime.get(Calendar.MINUTE))

						+ "=========================================================\n" +

						"Maximum Air Temperature[1.5m] = " +

						String.format("%.1f" + " C at " + "%s\n", getStatistics(StatsType.MAXIMUM, "TAIR").getValue(),
								getStatistics(StatsType.MAXIMUM, "TAIR").getStid())
						+

						"Minimum Air Temperature[1.5m] = " +

						String.format("%.1f" + " C at " + "%s\n", getStatistics(StatsType.MINIMUM, "TAIR").getValue(),
								getStatistics(StatsType.MINIMUM, "TAIR").getStid())
						+

						"Average Air Temperature[1.5m] = " +

						String.format("%.1f" + " C at " + "%s\n", getStatistics(StatsType.AVERAGE, "TAIR").getValue(),
								getStatistics(StatsType.AVERAGE, "TAIR").getStid())
						+

						"=========================================================\n\n"

						+

						"=========================================================\n"

						+ "Maximum Air Temperature[9.0m] = " +

						String.format("%.1f" + " C at " + "%s\n", getStatistics(StatsType.MAXIMUM, "TA9M").getValue(),
								getStatistics(StatsType.MAXIMUM, "TA9M").getStid())
						+

						"Minimum Air Temperature[9.0m] = " +

						String.format("%.1f" + " C at " + "%s\n", getStatistics(StatsType.MINIMUM, "TA9M").getValue(),
								getStatistics(StatsType.MINIMUM, "TA9M").getStid())
						+

						"Average Air Temperature[9.0m] = " +

						String.format("%.1f" + " C at " + "%s\n", getStatistics(StatsType.AVERAGE, "TA9M").getValue(),
								getStatistics(StatsType.AVERAGE, "TA9M").getStid())
						+

						"=========================================================\n\n" +

						"=========================================================\n"
						+ "Maximum Solar Radiation[1.5m] = " +

						String.format("%.1f" + " W/m^2 at " + "%s\n",
								getStatistics(StatsType.MAXIMUM, "SRAD").getValue(),
								getStatistics(StatsType.MAXIMUM, "SRAD").getStid())
						+

						"Minimum Solar Radiation[1.5m] = " +

						String.format("%.1f" + " W/m^2 at " + "%s\n",
								getStatistics(StatsType.MINIMUM, "SRAD").getValue(),
								getStatistics(StatsType.MINIMUM, "SRAD").getStid())
						+

						"Average Solar Radiation[1.5m] = " +

						String.format("%.1f" + " W/m^2 at " + "%s\n",
								getStatistics(StatsType.AVERAGE, "SRAD").getValue(),
								getStatistics(StatsType.AVERAGE, "SRAD").getStid())
						+ "=========================================================");

		return thing;

	}

}
