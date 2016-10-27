package libPurple;

public class PIDvalue {
	
	public double kP;
	public double kI;
	public double kD;
	
	public PIDvalue(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	public double getkP() {
		return kP;
	}

	public void setkP(double kP) {
		this.kP = kP;
	}

	public double getkI() {
		return kI;
	}

	public void setkI(double kI) {
		this.kI = kI;
	}

	public double getkD() {
		return kD;
	}

	public void setkD(double kD) {
		this.kD = kD;
	}

}
