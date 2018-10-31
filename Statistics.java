import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

public class Statistics extends Observation {

	protected String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss z";
	protected DateTimeFormatter format;
	private GregorianCalendar utcDateTime;
	private ZonedDateTime zdtDateTime;
	private int numberOfReportingStations;
	StatsType statType;

	public Statistics(double value, String stid, GregorianCalendar dateTime, int numberOfValidStations,
			StatsType inStatType) {

		// doesnt test
		super(value, stid);
		
		this.numberOfReportingStations = numberOfValidStations;
		this.statType = inStatType;

	}

	public Statistics(double value, String stid, ZonedDateTime dateTime, int numberOfValidStations,
			StatsType inStatType) {
		// doesnt test
		super(value, stid);
		this.zdtDateTime = dateTime;
		this.numberOfReportingStations = numberOfValidStations;
		this.statType = inStatType;
	}

	public GregorianCalendar createDateFromString(String dateTimeStr) throws ParseException {

		SimpleDateFormat newform = new SimpleDateFormat(DATE_TIME_FORMAT);
		newform.parse(dateTimeStr);
		return (GregorianCalendar) (newform.getCalendar());
	}

	public ZonedDateTime createZDateFromString(String dateTimeStr) {
		return zdtDateTime;

	}

	public String createStringFromDate(GregorianCalendar calendar) {

		SimpleDateFormat newform = new SimpleDateFormat(DATE_TIME_FORMAT);
		return (String) newform.format(DATE_TIME_FORMAT);

	}

	public String createStringFromDate(ZonedDateTime calendar) {
		return DATE_TIME_FORMAT;

	}

	public int getNumberOfReportingStations() {

		return numberOfReportingStations;
	}

	public String getUTCDateTimeString() {

		return DATE_TIME_FORMAT;

	}

	public boolean newerThan(GregorianCalendar inDateTime) {

		return utcDateTime.before(inDateTime);

	}

	public boolean olderThan(GregorianCalendar inDateTime) {

		return utcDateTime.after(inDateTime);
	}

	public boolean sameAs(GregorianCalendar inDateTime) {

		return utcDateTime.equals(inDateTime);
	}

	public boolean newerThan(ZonedDateTime inDateTime) {

		return zdtDateTime.isBefore(inDateTime);

	}

	public boolean olderThan(ZonedDateTime inDateTime) {

		return zdtDateTime.isAfter(inDateTime);
	}

	public boolean sameAs(ZonedDateTime inDateTime) {

		return zdtDateTime.equals(inDateTime);
	}

	public String toString() {
		return DATE_TIME_FORMAT;
	}

}
