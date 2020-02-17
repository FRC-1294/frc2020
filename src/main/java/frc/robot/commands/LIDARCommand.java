/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
<<<<<<< Updated upstream
import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
=======
//import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C;
//import edu.wpi.first.wpilibj.PIDSourceType;

import java.nio.ByteBuffer;

import edu.wpi.first.hal.I2CJNI;
//import edu.wpi.first.wpilibj2.command.PIDCommand;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
>>>>>>> Stashed changes

public class LIDARCommand extends CommandBase {
  /**
   * Creates a new LIDARSubsystem.
   */
<<<<<<< Updated upstream
    
  public Timer timer;
  public double signal;

  public double distanceIn;
  public double distanceCm;

  double wavestart = 0;

  public DigitalInput DOI;

  public float Timer;
  
  public LIDARCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

=======


  private static final byte k_deviceAddress = 0x62;

	private final byte m_port;

	private final ByteBuffer m_buffer = ByteBuffer.allocateDirect(2);
    
  public I2C i2c;
  
  public double offset;
  
  public LIDARCommand(Port port) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_port = (byte) port.value;
		I2CJNI.i2CInitialize(m_port);
  }

  public void startMeasuring() {
		writeRegister(0x04, 0x08 | 32); // default plus bit 5
		writeRegister(0x11, 0xff);
		writeRegister(0x00, 0x04);
	}

	public void stopMeasuring() {
		writeRegister(0x11, 0x00);
	}

	public int getDistance() {
		return readShort(0x8f);
	}

	private int writeRegister(int address, int value) {
		m_buffer.put(0, (byte) address);
		m_buffer.put(1, (byte) value);

		return I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 2);
	}

	private short readShort(int address) {
		m_buffer.put(0, (byte) address);
		I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 1);
		I2CJNI.i2CRead(m_port, k_deviceAddress, m_buffer, (byte) 2);
		return m_buffer.getShort(0);
	}

	// @Override
	// public void setPIDSourceType(PIDSourceType pidSource) {
	// 	if (pidSource != PIDSourceType.kDisplacement) {
	// 		throw new IllegalArgumentException("Only displacement is supported");
	// 	}
	// }

	// @Override
	// public PIDSourceType getPIDSourceType() {
	// 	return PIDSourceType.kDisplacement;
	// }

	//@Override
	public double pidGet() {
		return getDistance();
	}


>>>>>>> Stashed changes
  // Called when the command is initially scheduled.
  @Override
  public void initialize(){ 

<<<<<<< Updated upstream
    
    //Set DOI to be the one on the roborio, DONE
    /*
    set value of the LidAR to 0 (false)
    Set it to 1 (true)

    */

    DOI = new DigitalInput(0);
    //DOI.set(false);
    //Timer = 0;
=======
    SmartDashboard.setDefaultNumber("Offset", 0);
    
    startMeasuring(); //Starts Measuring (Maybe, I have no idea)

>>>>>>> Stashed changes
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {


<<<<<<< Updated upstream
    if(DOI.get()){
      distanceCm += 1;
    }
    else{

      distanceCm *= 2.85714;
      if(distanceCm != 0){
        SmartDashboard.putBoolean("Value", DOI.get());
        SmartDashboard.putNumber("distanceCM", distanceCm);
        System.out.print(distanceCm);
      }
      
      distanceCm = 0;
      
    }
    
    
    

=======
    offset = SmartDashboard.getNumber("Offset", 0);
    SmartDashboard.putNumber("Distance", getDistance()/2.54f + offset);
  
>>>>>>> Stashed changes
  }

  
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

}

<<<<<<< Updated upstream
/*
Timer += 1;

    if(DOI.get()){
      
      if(wavestart == 0){
        wavestart = timer.get();
      }
      
    }
    

    else{
      signal = (timer.get() - wavestart);
      timer.reset();
      wavestart = 0;
      distanceCm = signal*100000;
      distanceIn = distanceCm * 0.03937007874016;


    }

    SmartDashboard.putBoolean("Value", DOI.get());
    SmartDashboard.putNumber("distanceCM", distanceCm);
    SmartDashboard.putNumber("distanceIn", distanceIn);
    */
=======
>>>>>>> Stashed changes
