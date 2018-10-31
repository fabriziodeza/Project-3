
public class Observation extends AbstractObservation{

	private double value;
	private String stid;

	public Observation(double value, String stid) {
		
		this.value = value;
		this.stid = stid;
		
		if(this.value <= -900) {
			
			super.valid = false;
			
		} else {
			
			super.valid = true;
		}
		
		

	}

	public double getValue() {
		return value;

	}

	public boolean isValid() {
		return valid;

	}

	public String getStid() {
		return stid;
	}

	public String toString() {
		
		String string = null;
		
		return string;
		
	}
}
