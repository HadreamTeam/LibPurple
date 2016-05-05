package libPurple;

import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogPressureSensor {
	
	AnalogInput input;
	double VCC = 5; // The given voltage.
	
	public AnalogPressureSensor(int channel)
	{
		input = new AnalogInput(channel);
	}
	
	public double readPressure()
	{
		double Vout = input.getVoltage();
		double p = 250 * (Vout / VCC) - 25;
		return p;
	}

}
