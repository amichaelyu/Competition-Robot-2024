package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.ClimberConstants;

public class Climber extends SubsystemBase {
  private final TalonFX rightClimberMotor = new TalonFX(ClimberConstants.rightClimberMotorID, Constants.CAN_BUS_NAME);
  private final TalonFX leftClimberMotor = new TalonFX(ClimberConstants.leftClimberMotorID, Constants.CAN_BUS_NAME);
  private final DigitalInput limitSwitchRightTop;
  private final DigitalInput limitSwitchRightBottom;

  private static final Climber INSTANCE = new Climber();

  public static Climber getInstance() {
    return INSTANCE;
  }

  /** Creates a new Climber. */
  private Climber() {
    limitSwitchRightTop = new DigitalInput(ClimberConstants.LIMIT_SWITCH_TOP_ID);
    limitSwitchRightBottom = new DigitalInput(ClimberConstants.LIMIT_SWITCH_BOTTOM_ID);

    rightClimberMotor.getConfigurator().apply(ClimberConstants.talonFXConfigs);
    leftClimberMotor.getConfigurator().apply(ClimberConstants.talonFXConfigs);

    rightClimberMotor.setNeutralMode(NeutralModeValue.Brake);
    leftClimberMotor.setNeutralMode(NeutralModeValue.Brake);
//    m_rightClimberMotor.setPosition(0);
//    m_leftClimberMotor.setPosition(0);

    rightClimberMotor.setInverted(true);
    leftClimberMotor.setInverted(true);
  }

  public void periodic() {
    SmartDashboard.putBoolean("climber Top", !limitSwitchRightTop.get());
    SmartDashboard.putBoolean("climber bottom", !limitSwitchRightBottom.get());
  }

  public void move(double pwr) {
    // - is up
    // + is down
    
    if (!limitSwitchRightBottom.get() && pwr > 0) {
      rightClimberMotor.set(0);
      leftClimberMotor.set(0);
      return;
    }
    else if (!limitSwitchRightTop.get() && pwr < 0) {
      rightClimberMotor.set(0);
      leftClimberMotor.set(0);
      return;
    }
    rightClimberMotor.set(pwr);
    leftClimberMotor.set(pwr);
  }
 
  public void stop() {
    rightClimberMotor.stopMotor();
    leftClimberMotor.stopMotor();
  }
}