/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.ShootingBall;
import frc.robot.subsystems.UltrasonicSubsystem;
import frc.robot.subsystems.TwentyThreeStabWounds;

public class AlignToShoot extends CommandBase {
  DriveAutoSubsystem m_driveAuto;
  UltrasonicSubsystem m_ultra;
  ShootingBall m_shooter;
  TwentyThreeStabWounds m_vision;

  AutoPath autoPath;
  DictatorLocator alignToTarget;
  ShooterCommand shooterCommand;
  UltraFuseCommand ultraFuse;

  Timer timer = new Timer();

  boolean isFinished;
  boolean left1;
  boolean left2;
  boolean shooter;
  boolean shooterReady;
  boolean loopComplete;
  int iterations;
  int targetAngle;
  int xTarget;
  int yTarget;
  int shooterSpeed;
  int shootDis;
  double[] startAmount;

  final int autoPathMargin = 2;
  final int robotFollowDis = 5*12;
  final int shootRPM = 4200;
  final int shootMargin = 50;
  final double shootTime = 5.0;

  int step = 0;

  public AlignToShoot(DriveAutoSubsystem driveAuto, UltrasonicSubsystem ultra, ShootingBall shooter, TwentyThreeStabWounds vision, int targetDis) {
    m_driveAuto = driveAuto;
    m_ultra = ultra;
    m_shooter = shooter;
    m_vision = vision;

    shootDis = targetDis;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    addRequirements(m_driveAuto);
    addRequirements(m_ultra);

    timer.start();
    timer.reset();
    resetVars();

    startAmount = new double[] {m_driveAuto.getAmountTraveled(0), m_driveAuto.getAmountTraveled(1)};
    left1 = false;
    left2 = false;
    autoPath = new AutoPath(xTarget, yTarget, left1, left2, m_driveAuto);
    ultraFuse = new UltraFuseCommand(m_driveAuto, m_ultra);
    shooterCommand = new ShooterCommand(m_shooter, shootTime);
    alignToTarget = new DictatorLocator(m_vision, m_driveAuto);

    ultraFuse.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //distance left to travel
    double xRem = Math.abs(xTarget - m_driveAuto.getAmountTraveled(0) + startAmount[0]);
    double yRem = Math.abs(yTarget - m_driveAuto.getAmountTraveled(1) + startAmount[1]);

    //checkShooter();

    //if current leg of path finished, schedule next in sequence
    if (!autoPath.isScheduled() && !alignToTarget.isScheduled() && !shooterCommand.isScheduled() && ultraFuse.isScheduled()) {
      if (Math.abs(xRem) <= autoPathMargin && Math.abs(yRem) <= autoPathMargin) {
        //align with hoop and shoot && get shooter ready
        if (step == 0) {
          alignToTarget.schedule();
          shooter = true;
          step++;
        }
        //move until shooting distance
        else if (step == 1) {
          xTarget = shootDis;
          left1 = false;
          left2 = false;
          targetAngle = 270;
          xRem = Math.abs(xTarget - m_driveAuto.getAmountTraveled(0));
          autoPath = new AutoPath(xRem, 0, left1, left2, m_driveAuto);
          autoPath.schedule();
          step++;
        }
        //SHOOT
        else if (step == 2) {
          if (shooterReady) {
            shooterCommand = new ShooterCommand(m_shooter, shootTime);
            shooterCommand.schedule();
            shooter = false;
            step++;
          }
        }
        //end command
        else if (step == 3) {
          isFinished = true;
        }
      } 
    }
    

    //if obstacle detected during PID
    if (!ultraFuse.isScheduled()) {
      //if stopping necessary
      if ((!m_driveAuto.getTurning() && !alignToTarget.isScheduled())) {
        autoPath.cancel();

        if (m_ultra.getSensourLeft() <= m_ultra.MIN_DIS || m_ultra.getSensourRight() <= m_ultra.MIN_DIS) {
          //avoid?
        }
        else {
          //resechedule path if obstacle avoided
          if (xRem >= autoPathMargin || yRem >= autoPathMargin) {
            autoPath = new AutoPath(xRem, yRem, left1, left2, m_driveAuto);
            autoPath.schedule();
          }
        }
      }
      //keep ultraFuse running to check if obstacle moves
      ultraFuse.schedule();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveAuto.setFrontLeftSpeed(0);
    m_driveAuto.setFrontRightSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }

  public void checkShooter() {
    if (shooter) {
      shooterSpeed = m_shooter.getShooterVelocity();
      m_shooter.setShooterPID(shootRPM);

      boolean atSpeed = false;
      boolean timeHold = false;

      if (Math.abs(shooterSpeed) <= shootRPM+shootMargin && Math.abs(shooterSpeed) >= shootRPM+shootMargin) {
        atSpeed = true;
      }

      if (atSpeed) {
        if(timer.get() >= 1){
          timeHold = true;
        }
      }
      else {
        timer.reset();
      }

      if(atSpeed && timeHold) {
        shooterReady = true;
      }
      else {
        shooterReady = false;
      }
    }
    else {
      m_shooter.setShooter(0);
    }
  }

  public void resetVars() {
    isFinished = false;
    left1 = false;
    left2 = false;
    shooter = false;
    shooterReady = false;
    loopComplete = false;
  }
}