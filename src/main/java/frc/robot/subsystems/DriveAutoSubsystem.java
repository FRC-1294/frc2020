package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.CANSparkMax; //Shacuando was here
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Gains;
import frc.robot.Robot;
import frc.robot.commands.DictatorLocator;

public class DriveAutoSubsystem extends SubsystemBase {
  private CANSparkMax frontLeftSpark = new CANSparkMax(Constants.frontLeftSpark, MotorType.kBrushless);
  private CANPIDController frontLeftPID = frontLeftSpark.getPIDController();
  private CANSparkMax frontRightSpark = new CANSparkMax(Constants.frontRightSpark, MotorType.kBrushless);
  private CANPIDController frontRightPID = frontRightSpark.getPIDController();
  private CANSparkMax rearLeftSpark = new CANSparkMax(Constants.rearLeftSpark, MotorType.kBrushless);
  private CANPIDController rearLeftPID = rearLeftSpark.getPIDController();
  private CANSparkMax rearRightSpark = new CANSparkMax(Constants.rearRightSpark, MotorType.kBrushless);
  private CANPIDController rearRightPID = rearRightSpark.getPIDController();

  private SpeedControllerGroup sparkDriveLeft = new SpeedControllerGroup(frontLeftSpark, rearLeftSpark);
  private SpeedControllerGroup sparkDriveRight = new SpeedControllerGroup(frontRightSpark, rearRightSpark);
  private DifferentialDrive sparkDrive = new DifferentialDrive(sparkDriveLeft,sparkDriveRight);

  public final XboxController driveJoystick = new XboxController(Constants.driveJoystick);

  private final double targetPositionRotations = 0.54;
  private static int currentAngle;
  private static double[] amountTraveled = new double[] {0, 0};
  private final Gains defaultPID = new Gains(0.2, 0.00001, 0.6, 0.0, 0.0, -0.5, 0.5, 0);
  private final Gains lowDisPID = new Gains(0.2, 0.00001, 0.4, 0.0, 0.0, -0.7, 0.7, 1);
  private Timer timer = new Timer();
  private boolean isTurning = false;

  private DictatorLocator visionMove = new DictatorLocator(Robot.cassius, this);

  public DriveAutoSubsystem() {
    frontLeftSpark.restoreFactoryDefaults(true);
    frontRightSpark.restoreFactoryDefaults(true);
    rearLeftSpark.restoreFactoryDefaults(true);
    rearRightSpark.restoreFactoryDefaults(true);

    frontLeftSpark.setMotorType(MotorType.kBrushless);
    frontRightSpark.setMotorType(MotorType.kBrushless);
    rearLeftSpark.setMotorType(MotorType.kBrushless);
    rearRightSpark.setMotorType(MotorType.kBrushless);

    frontLeftSpark.getEncoder();
    frontRightSpark.getEncoder();
    rearLeftSpark.getEncoder();
    rearRightSpark.getEncoder();

    frontLeftSpark.setSmartCurrentLimit(60);
    frontRightSpark.setSmartCurrentLimit(60);
    rearLeftSpark.setSmartCurrentLimit(60);
    rearRightSpark.setSmartCurrentLimit(60);

    frontLeftSpark.setOpenLoopRampRate(1);
    frontRightSpark.setOpenLoopRampRate(1);
    frontLeftSpark.setClosedLoopRampRate(1);
    frontRightSpark.setClosedLoopRampRate(1);
    rearLeftSpark.setClosedLoopRampRate(1);
    rearRightSpark.setClosedLoopRampRate(1);
    rearLeftSpark.setClosedLoopRampRate(1);
    rearRightSpark.setClosedLoopRampRate(1);
    setPidControllers(frontLeftPID, defaultPID, defaultPID.kSlot);
    setPidControllers(frontRightPID, defaultPID, defaultPID.kSlot);
    setPidControllers(rearLeftPID, defaultPID, defaultPID.kSlot);
    setPidControllers(rearRightPID, defaultPID, defaultPID.kSlot);
    setPidControllers(frontLeftPID, lowDisPID, lowDisPID.kSlot);
    setPidControllers(frontRightPID, lowDisPID, lowDisPID.kSlot);
    setPidControllers(rearLeftPID, lowDisPID, lowDisPID.kSlot);
    setPidControllers(rearRightPID, lowDisPID, lowDisPID.kSlot);
    setMode("coast");

    frontLeftSpark.setInverted(false);
    frontRightSpark.setInverted(true);
    rearLeftSpark.setInverted(false);
    rearRightSpark.setInverted(false);

    rearLeftSpark.follow(frontLeftSpark);
    rearRightSpark.follow(frontRightSpark);
    
    timer.start();
  }

  @Override
  public void periodic() {
    sparkDrive.feed();
    sparkDrive.feedWatchdog();
    
    SmartDashboard.putString("AmountTraveled", getAmountTraveled(0) + " , " + getAmountTraveled(1));
    SmartDashboard.putNumber("currentAngle", getCurrentAngle());
    SmartDashboard.putNumber("LeftUltra", Robot.ultrasonic.getSensourLeft());
    SmartDashboard.putNumber("RightUltra", Robot.ultrasonic.getSensourRight());

    if (driveJoystick.getStartButtonPressed()) {
      CommandScheduler.getInstance().cancelAll();
    }

    if (driveJoystick.getBumper(Hand.kRight)) {
      setMode("brake");
    }
    else {
      setMode("coast");
    }

    if (driveJoystick.getAButtonPressed() && !visionMove.isScheduled()) {
      visionMove.schedule();
    }

    //frontLeftSpark.set(0.1);
    //frontRightSpark.set(0.1);
    //rearLeftSpark.set(0.1);
    //rearRightSpark.set(0.1);
    arcadeDrive(driveJoystick.getY(Hand.kLeft), driveJoystick.getX(Hand.kRight));
  }

  public void arcadeDrive(double forward, double turn) {
    sparkDrive.arcadeDrive(turn*0.5, -forward);
  }

  public void setTurning(boolean val) {
    isTurning = val;
  }

  public void setMode(String type) {
    if (type == "brake") {
      frontLeftSpark.setIdleMode(IdleMode.kBrake);
      frontRightSpark.setIdleMode(IdleMode.kBrake);
      rearLeftSpark.setIdleMode(IdleMode.kBrake);
      rearRightSpark.setIdleMode(IdleMode.kBrake);
    } else if (type == "coast") {
      frontLeftSpark.setIdleMode(IdleMode.kCoast);
      frontRightSpark.setIdleMode(IdleMode.kCoast);
      rearLeftSpark.setIdleMode(IdleMode.kCoast);
      rearRightSpark.setIdleMode(IdleMode.kCoast);
    }
  }

  private void setPidControllers (CANPIDController pidController, Gains pidSet, int slot) {
    pidController.setP(pidSet.kP, slot);
    pidController.setI(pidSet.kI, slot);
    pidController.setD(pidSet.kD, slot);
    pidController.setIZone(pidSet.kIz, slot);
    pidController.setFF(pidSet.kFF, slot);
    pidController.setOutputRange(pidSet.kMinOutput, pidSet.kMaxOutput, slot);
  }

  public int getCurrentAngle() {
    return currentAngle;
  }

  public double getFrontLeftSpeed() {
    return frontLeftSpark.get();
  }

  public double getFrontRightSpeed() {
    return frontRightSpark.get();
  }

  public double getFrontLeftVelocity() {
    return frontLeftSpark.getEncoder().getVelocity();
  }

  public double getFrontRightVelocity() {
    return frontRightSpark.getEncoder().getVelocity();
  }

  public double getFrontLeftPosition() {
    return frontLeftSpark.getEncoder().getPosition();
  }

  public double getFrontRightPosition() {
    return frontRightSpark.getEncoder().getPosition();
  }

  public double getRearLeftPosition() {
    return rearLeftSpark.getEncoder().getPosition();
  }

  public double getRearRightPosition() {
    return rearRightSpark.getEncoder().getPosition();
  }

  public double getMoveByFactor() {
    return targetPositionRotations;
  }

  public double getAmountTraveled(int id) {
    return amountTraveled[id];
  }

  public boolean getTurning() {
    return isTurning;
  }

  public void setCurrentAngle(int val) {
    currentAngle = val;
  }

  public void setAmountTraveled(int id, double val) {
    amountTraveled[id] = val;
  }

  public void setRamp(double time) {
    this.frontLeftSpark.setClosedLoopRampRate(0.5);
    this.frontRightSpark.setClosedLoopRampRate(0.5);
    this.rearLeftSpark.setClosedLoopRampRate(0.5);
    this.rearRightSpark.setClosedLoopRampRate(0.5);

    this.frontLeftSpark.setOpenLoopRampRate(0.5);
    this.frontRightSpark.setOpenLoopRampRate(0.5);
    this.rearLeftSpark.setOpenLoopRampRate(0.5);
    this.rearRightSpark.setOpenLoopRampRate(0.5);
  }

  public void setFrontLeftPID(double val, ControlType controlType, int slot) {
    this.frontLeftPID.setReference(val, controlType, slot);
  }
  
  public void setFrontRightPID(double val, ControlType controlType, int slot) {
    this.frontRightPID.setReference(val, controlType, slot);
  }

  public void setRearLeftPID(double val, ControlType controlType, int slot) {
    this.rearLeftPID.setReference(val, controlType, slot);
  }

  public void setRearRightPID(double val, ControlType controlType, int slot) {
    this.rearRightPID.setReference(val, controlType, slot);
  }

  public void resetEncoders() {
    this.frontLeftSpark.getEncoder().setPosition(0);
    this.frontRightSpark.getEncoder().setPosition(0);
    this.rearLeftSpark.getEncoder().setPosition(0);
    this.rearRightSpark.getEncoder().setPosition(0);
  }

  public void setFrontLeftSpeed(double val) {
    this.frontLeftSpark.set(val);
  }

  public void setFrontRightSpeed(double val) {
    this.frontRightSpark.set(val);
  }

  public void setRearLeftSpeed(double val) {
    this.rearLeftSpark.set(val);
  }

  public void setRearRightSpeed(double val) {
    this.rearRightSpark.set(val);
  }
}