package frc.robot.subsystems;

import frc.robot.commands.DelayCommand;
import frc.robot.commands.MoveByCommand;
import frc.robot.commands.TurnByCommand;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax; //Shacuando was here
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Gains;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveAutoSubsystem extends SubsystemBase {
  public CANSparkMax frontLeftSpark = new CANSparkMax(3, MotorType.kBrushless);
  public CANPIDController frontLeftPID = frontLeftSpark.getPIDController();
  public CANSparkMax frontRightSpark = new CANSparkMax(1, MotorType.kBrushless);
  public CANPIDController frontRightPID = frontRightSpark.getPIDController();
  public CANSparkMax rearLeftTalon = new CANSparkMax(4, MotorType.kBrushless);
  public CANPIDController rearLeftPID = rearLeftTalon.getPIDController();
  public CANSparkMax rearRightTalon = new CANSparkMax(2, MotorType.kBrushless);
  public CANPIDController rearRightPID = rearRightTalon.getPIDController();

  XboxController driveJoystick = new XboxController(0);
  JoystickButton driveXButton = new JoystickButton(driveJoystick, 3);
  //AHRS navX = new AHRSE(SPI.Port.kMXP);

  public static boolean lock = false;
  public static boolean sequence = false;
  public int step = 0;
  static final Gains kGains = new Gains(0.2, 0.00001, 0.2, 0.0, 0.0, -0.5, 0.5);
  Timer timer = new Timer();
  double prevTime = 0;

  public DriveAutoSubsystem() {
    frontLeftSpark.setOpenLoopRampRate(1);
    frontRightSpark.setOpenLoopRampRate(1);
    frontLeftSpark.setClosedLoopRampRate(1);
    frontRightSpark.setClosedLoopRampRate(1);
    rearLeftTalon.setClosedLoopRampRate(1);
    rearRightTalon.setClosedLoopRampRate(1);
    rearLeftTalon.setClosedLoopRampRate(1);
    rearRightTalon.setClosedLoopRampRate(1);
    rearRightTalon.follow(frontRightSpark);
    rearLeftTalon.follow(frontLeftSpark);
    setPidControllers(frontLeftPID);
    setPidControllers(frontRightPID);
    setPidControllers(rearLeftPID);
    setPidControllers(rearRightPID);

    timer.start();
  }

  @Override
  public void periodic() {
    if (timer.get() - prevTime > 0.5) {
      SmartDashboard.putBoolean("Functions/Lock", lock);
      // SmartDashboard.putNumber("PID/leftEncoder", frontLeftSpark.getEncoder().getPosition());
      // SmartDashboard.putNumber("PID/rightEncoder", frontRightSpark.getEncoder().getPosition());
      // SmartDashboard.putNumber("PID/leftSpeed", frontLeftSpark.getEncoder().getVelocity());
      // SmartDashboard.putNumber("PID/rightSpeed", frontRightSpark.getEncoder().getVelocity());
      prevTime = timer.get();
    }

    if (driveJoystick.getStartButtonPressed()) {
      CommandScheduler.getInstance().cancelAll();
    }

    if (driveJoystick.getYButtonPressed() && !lock || sequence) {
      sequence = true;

      //IF OVERRIDE (ex if robot detected, then avoid)

      //ELSE continue the sequence (if this doesn't work, create commands as objects and use .getFinished())
      if (step == 0 && !lock) {
        System.out.println("Step 0");
        CommandScheduler.getInstance().schedule(new MoveByCommand(26 * 12));
      }
      else if (step == 1 && !lock) {
        System.out.println("Step 1");
        CommandScheduler.getInstance().schedule(new DelayCommand(2000));
      }
      else if (step == 2 && !lock) {
        System.out.println("Step 2");
        CommandScheduler.getInstance().schedule(new TurnByCommand(180));
      }
      else if (step == 3 && !lock) {
        CommandScheduler.getInstance().schedule(new DelayCommand(2000));
      }
      else if (step == 4 && !lock) {
        CommandScheduler.getInstance().schedule(new MoveByCommand(26 * 12));
      }
      else if (step == 5 && !lock) {
        sequence = false;
        step = 0;
      }

      // COMMAND SCHEDULAR SYNTAX (in case we need it again):
      // CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
      //   new MoveByCommand(26*12),
      //   new DelayCommand(2000),
      //   new TurnByCommand(180),
      //   new DelayCommand(2000),
      //   new MoveByCommand(26*12)));
    }
    else if (driveJoystick.getBumper(Hand.kRight) && !lock && !sequence) {
      CommandScheduler.getInstance().schedule(new TurnByCommand(-90));
      lock = true;
    }
    else if (driveJoystick.getBumper(Hand.kLeft) && !lock && !sequence) {
      CommandScheduler.getInstance().schedule(new TurnByCommand(90));
      lock = true;
    }
    else if (driveJoystick.getAButtonPressed() && !lock && !sequence) {
      CommandScheduler.getInstance().schedule(new MoveByCommand(192));
      lock = true;
    }
    else if (driveJoystick.getBButtonPressed() && !lock && !sequence) {
      CommandScheduler.getInstance().schedule(new MoveByCommand(-192));
      lock = true;
    }
  }

  public void setPidControllers (CANPIDController pidController) {
    pidController.setP(kGains.kP);
    pidController.setI(kGains.kI);
    pidController.setD(kGains.kD);
    pidController.setIZone(kGains.kIz);
    pidController.setFF(kGains.kFF);
    pidController.setOutputRange(kGains.kMinOutput, kGains.kMaxOutput);
  }
}