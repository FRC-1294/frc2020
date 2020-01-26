package frc.robot.subsystems;

import frc.robot.commands.DelayCommand;
import frc.robot.commands.MoveByCommand;
import frc.robot.commands.TurnByCommand;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax; //Shacuando was here
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Gains;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveAutoSubsystem extends SubsystemBase {
  public CANSparkMax frontLeftSpark = new CANSparkMax(3, MotorType.kBrushless);
  public CANPIDController frontLeftPID = frontLeftSpark.getPIDController();
  public CANSparkMax frontRightSpark = new CANSparkMax(1, MotorType.kBrushless);
  public CANPIDController frontRightPID = frontRightSpark.getPIDController();
  public CANSparkMax rearLeftSpark = new CANSparkMax(4, MotorType.kBrushless);
  public CANPIDController rearLeftPID = rearLeftSpark.getPIDController();
  public CANSparkMax rearRightSpark = new CANSparkMax(2, MotorType.kBrushless);
  public CANPIDController rearRightPID = rearRightSpark.getPIDController();

  XboxController driveJoystick = new XboxController(0);
  JoystickButton driveXButton = new JoystickButton(driveJoystick, 3);
  //AHRS navX = new AHRSE(SPI.Port.kMXP);

  public static boolean lock = false;
  public static boolean sequence = false;
  public static int step = 0;
  public static int currentAngle = 0;
  public static int[] amountTraveled = new int[] {0, 0};
  static final Gains kGains = new Gains(0.2, 0.00001, 0.2, 0.0, 0.0, -0.5, 0.5);
  Timer timer = new Timer();
  double prevTime = 0;
  SequentialCommandGroup autoPath;

  public DriveAutoSubsystem() {
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

    autoPath = new SequentialCommandGroup(
    new MoveByCommand(5*12),
    new DelayCommand(2000),
    new TurnByCommand(180),
    new DelayCommand(2000),
    new MoveByCommand(5*12));

    timer.start();
  }

  @Override
  public void periodic() {
    if (timer.get() - prevTime > 0.5) {
      SmartDashboard.putBoolean("Functions/Lock", lock);
      prevTime = timer.get();
    }

    if (driveJoystick.getStartButtonPressed()) {
      CommandScheduler.getInstance().cancelAll();
    }

    if (driveJoystick.getYButtonPressed() && !lock || sequence) {
      sequence = true;

      //if (!detectObstacle.isScheduled()) {
      //  autoPath.cancel();
      //  CommandScheduler.getInstance().schedule(avoidObstacle);
      //  if(!avoidObstacle.isScheduled()) {
      //    CommandScheduler.getInstance().schedule(detectObstacle);
          autoPath = new SequentialCommandGroup();
          autoPath.addCommands(new DelayCommand(2000));
          if (step == 0) {
            autoPath.addCommands(new MoveByCommand(5*12));
            autoPath.addCommands(new DelayCommand(2000));
          }
          if (step <= 1) {
            autoPath.addCommands(new TurnByCommand(180));
            autoPath.addCommands(new DelayCommand(2000));
          }
          if (step <= 2) {
            autoPath.addCommands(new MoveByCommand(5*12));
          }
      //    CommandScheduler.getInstance().schedule(autoPath);
      //  }
      //}

      if (!autoPath.isScheduled()) {
       CommandScheduler.getInstance().schedule(autoPath);
      }
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
      CommandScheduler.getInstance().schedule(new MoveByCommand(5*12));
      lock = true;
    }
    else if (driveJoystick.getBButtonPressed() && !lock && !sequence) {
      CommandScheduler.getInstance().schedule(new MoveByCommand(-5*12));
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