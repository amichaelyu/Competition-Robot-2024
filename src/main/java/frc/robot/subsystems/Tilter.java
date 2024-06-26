package frc.robot.subsystems;

import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.TilterConstants;

public class Tilter extends SubsystemBase {
  private final TalonFX m_tilterMotor = new TalonFX(TilterConstants.tilterMotorID, Constants.CAN_BUS_NAME);
  private boolean isHomed;
  private double setpoint = 0;
  private DigitalInput limitSwitchTop;
  private final DigitalInput limitSwitchBottom;

  private static final Tilter INSTANCE = new Tilter();

  public static Tilter getInstance() { return INSTANCE; }

  /**
   * Creates a new Tilter.
   */
  private Tilter() {
    limitSwitchBottom = new DigitalInput(TilterConstants.kLIFTER_LIMIT_BOTTOM);

    m_tilterMotor.getConfigurator().apply(TilterConstants.talonFXConfigs);
    m_tilterMotor.setPosition(TilterConstants.START_POSITION);

//    SmartDashboard.putNumber("tilter p", 1);
//    SmartDashboard.putNumber("tilter magicVel", 10);
//    SmartDashboard.putNumber("tilter magicAcc", 10);

    isHomed = true;
  }

  @Override
  public void periodic() {
//    Slot0Configs slotConfig = TilterConstants.talonFXConfigs.Slot0;
//    slotConfig.kP = SmartDashboard.getNumber("tilter p", 1);
//    m_tilterMotor.getConfigurator().apply(slotConfig);
//
//    MotionMagicConfigs magicConfig = TilterConstants.talonFXConfigs.MotionMagic;
//    magicConfig.MotionMagicCruiseVelocity = SmartDashboard.getNumber("tilter magicVel", 10);
//    magicConfig.MotionMagicAcceleration = SmartDashboard.getNumber("tilter magicAcc", 10);
//    m_tilterMotor.getConfigurator().apply(magicConfig);
//      setVoltage(SmartDashboard.getNumber("tilter voltage", 0));
//    SmartDashboard.putNumber("tilter applied current", m_tilterMotor.getTorqueCurrent().getValue());
//    SmartDashboard.putNumber("tilter applied voltage", m_tilterMotor.getMotorVoltage().getValue());

    SmartDashboard.putNumber("tilter rotations", m_tilterMotor.getPosition().getValue());
    SmartDashboard.putBoolean("At Bottom", isAtBottom());
    SmartDashboard.putBoolean("tilter at setpoint", atSetpoint());
    if (isAtBottom()) {
      homed();
//      System.out.println("Lifter at Bottom; not going down.");
    }
  }

  public void setVoltage(double voltage) {
      if (isAtBottom() && voltage < 0) {
        m_tilterMotor.setControl(new VoltageOut(0));
//        System.out.println("Lifter at Bottom; not going down.");
      }
      else{
        m_tilterMotor.setControl(new VoltageOut(voltage));
      }
    }

  private void resetEncoder() {
    m_tilterMotor.setPosition(0);
  }

  private void homed() {
    isHomed = true;
    resetEncoder();
  }

  public boolean atSetpoint() {
    return (getPosition() > (setpoint - TilterConstants.PID_TOLERANCE)) && (getPosition() < (setpoint + TilterConstants.PID_TOLERANCE));
  }

  public void setPosition(double position) {
//    if (isAtBottom() && position < m_tilterMotor.getPosition().getValue()) {
//      m_tilterMotor.set(0);
//    }
    setpoint = MathUtil.clamp(position, 0, TilterConstants.MAX_POSITION);
    if (isHomed) {
      if (m_tilterMotor.getPosition().getValue() > setpoint) {
        m_tilterMotor.setControl(new MotionMagicVoltage(setpoint).withFeedForward(-m_tilterMotor.getPosition().getValue() * 0.04));
      }
      else {
        m_tilterMotor.setControl(new MotionMagicVoltage(setpoint).withFeedForward(m_tilterMotor.getPosition().getValue() * 0.04));
      }
    }
  }

  public double getVoltage() {
    return m_tilterMotor.getMotorVoltage().getValue();
  }

  public double getPosition() {
    return m_tilterMotor.getPosition().getValue();
  }

  public double getVelocity() {
    return m_tilterMotor.getVelocity().getValue();
  }

  public void move(double pwr) {
    if (isAtBottom() && pwr < 0) {
      m_tilterMotor.set(0);
      System.out.println("Lifter at Bottom; not going down.");
    }
    else{
      m_tilterMotor.set(pwr);
    }
  }
 
  public void stop() {
    m_tilterMotor.setControl(new VoltageOut(0));
  }

  public boolean isAtBottom() {
		return !limitSwitchBottom.get();
    }
  }