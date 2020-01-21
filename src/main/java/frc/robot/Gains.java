package frc.robot;

public class Gains {
	public final double kP;
	public final double kI;
	public final double kD;
	public final double kIz;
	public final double kFF;
	public final double kMinOutput;
	public final double kMaxOutput;

	public Gains(double _kP, double _kI, double _kD, double _kIz, double _kFF, double _kMinOutput, double _kMaxOutput){
		kP = _kP;
		kI = _kI;
		kD = _kD;
		kIz = _kIz;
		kFF = _kFF;
		kMinOutput = _kMinOutput;
		kMaxOutput = _kMaxOutput;
	}
}