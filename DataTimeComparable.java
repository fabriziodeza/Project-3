import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

public interface DataTimeComparable {

	boolean newerThan(GregorianCalendar inDateTimeUTC);

	boolean olderThan(GregorianCalendar inDateTimeUTC);

	boolean sameAs(GregorianCalendar inDateTimeUTC);

	boolean newerThan(ZonedDateTime inDateTimeUTC);

	boolean olderThan(ZonedDateTime inDateTimeUTC);

	boolean sameAs(ZonedDateTime inDateTimeUTC);

}
