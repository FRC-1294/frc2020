package frc.robot.subsystems;

import frc.robot.commands.AutoNavCommand;
import frc.robot.commands.TurnByCommand;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax; //Shacuando was here
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.Gains;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class DriveAutoSubsystem extends SubsystemBase {
  private final CANSparkMax frontLeftSpark = new CANSparkMax(Constants.frontLeftSpark, MotorType.kBrushless);
  private final CANPIDController frontLeftPID = frontLeftSpark.getPIDController();
  private final CANSparkMax frontRightSpark = new CANSparkMax(Constants.frontRightSpark, MotorType.kBrushless);
  private final CANPIDController frontRightPID = frontRightSpark.getPIDController();
  private final CANSparkMax rearLeftSpark = new CANSparkMax(Constants.rearLeftSpark, MotorType.kBrushless);
  private final CANPIDController rearLeftPID = rearLeftSpark.getPIDController();
  private final CANSparkMax rearRightSpark = new CANSparkMax(Constants.rearRightSpark, MotorType.kBrushless);
  private final CANPIDController rearRightPID = rearRightSpark.getPIDController();
  //private final WPI_TalonSRX intakeTalon = new WPI_TalonSRX(Constants.intakeTalon);

  private final SpeedControllerGroup leftSide = new SpeedControllerGroup(frontLeftSpark, rearLeftSpark);
  private final SpeedControllerGroup rightSide = new SpeedControllerGroup(frontRightSpark, rearRightSpark);
  
  private final DifferentialDrive drive = new DifferentialDrive(leftSide, rightSide);

  private final XboxController driveJoystick = new XboxController(Constants.driveJoystick);
 // private final XboxController gameJoystick = new XboxController(1);

  private final double targetPositionRotations = 0.54;
  private static int currentAngle;
  private static double[] amountTraveled = new double[] {0, 0};
  private final Gains kGains = new Gains(0.2, 0.00001, 0.4, 0.0, 0.0, -0.3, 0.3);
  private Timer timer = new Timer();
  private double prevTime = 0;

  private boolean shouldSwootch = true;
  private boolean isTurning = false;

  AutoNavCommand autLeft;
  AutoNavCommand autRight;

  public DriveAutoSubsystem() {
    // frontLeftSpark.restoreFactoryDefaults(true);
    // frontRightSpark.restoreFactoryDefaults(true);
    // rearLeftSpark.restoreFactoryDefaults(true);
    // rearRightSpark.restoreFactoryDefaults(true);

    frontLeftSpark.setMotorType(MotorType.kBrushless);
    frontRightSpark.setMotorType(MotorType.kBrushless);
    rearLeftSpark.setMotorType(MotorType.kBrushless);
    rearRightSpark.setMotorType(MotorType.kBrushless);

    frontLeftSpark.setOpenLoopRampRate(1);
    frontRightSpark.setOpenLoopRampRate(1);
    frontLeftSpark.setClosedLoopRampRate(1);
    frontRightSpark.setClosedLoopRampRate(1);
    rearLeftSpark.setClosedLoopRampRate(1);
    rearRightSpark.setClosedLoopRampRate(1);
    rearLeftSpark.setClosedLoopRampRate(1);
    rearRightSpark.setClosedLoopRampRate(1);
    rearRightSpark.follow(frontRightSpark);
    rearLeftSpark.follow(frontLeftSpark);
    setPidControllers(frontLeftPID);
    setPidControllers(frontRightPID);
    setPidControllers(rearLeftPID);
    setPidControllers(rearRightPID);

    frontLeftSpark.setInverted(false);
    frontRightSpark.setInverted(false);
    rearLeftSpark.setInverted(false);
    rearRightSpark.setInverted(false);
//shakuando was here
    timer.start();
  }

  @Override
  public void periodic() {
  //  driveJoystick.setRumble(RumbleType.kLeftRumble, 1);
  //  driveJoystick.setRumble(RumbleType.kRightRumble, 1);
  //  gameJoystick.setRumble(RumbleType.kLeftRumble, 1);
  //  gameJoystick .setRumble(RumbleType.kRightRumble, 1);

    SmartDashboard.putString("AmountTraveled", amountTraveled[0] + " , " + amountTraveled[1]);
    SmartDashboard.putNumber("currentAngle", currentAngle);

    if (driveJoystick.getStartButtonPressed()) {
      CommandScheduler.getInstance().cancelAll();
    }

    if (driveJoystick.getXButtonPressed()) {
      CommandScheduler.getInstance().schedule(new TurnByCommand(-90, this));
    }
    else if (driveJoystick.getBButtonPressed()) {
      CommandScheduler.getInstance().schedule(new TurnByCommand(90, this));
    }

    arcadeDrive(driveJoystick.getY(Hand.kLeft), driveJoystick.getX(Hand.kRight));
  }

  public void arcadeDrive(double forward, double turn) {
   // if (Math.abs(forward) <= 0.1 ||  Math.abs(turn) <= 0.1) {
      drive.arcadeDrive(-forward, turn*0.5);
   // }
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

  private void setPidControllers (CANPIDController pidController) {
    pidController.setP(kGains.kP);
    pidController.setI(kGains.kI);
    pidController.setD(kGains.kD);
    pidController.setIZone(kGains.kIz);
    pidController.setFF(kGains.kFF);
    pidController.setOutputRange(kGains.kMinOutput, kGains.kMaxOutput);
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

  public void setFrontLeftPID(double val, ControlType controlType) {
    this.frontLeftPID.setReference(val, controlType);
  }
  
  public void setFrontRightPID(double val, ControlType controlType) {
    this.frontRightPID.setReference(val, controlType);
  }

  public void setRearLeftPID(double val, ControlType controlType) {
    this.rearLeftPID.setReference(val, controlType);
  }

  public void setRearRightPID(double val, ControlType controlType) {
    this.rearRightPID.setReference(val, controlType);
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