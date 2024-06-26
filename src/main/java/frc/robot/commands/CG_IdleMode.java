package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants.TilterConstants;

public class CG_IdleMode extends ParallelCommandGroup {
    public CG_IdleMode() {
        super(
                new ShooterVoltage(0),
                new TilterSetpointPosition(TilterConstants.START_POSITION)
        );
    }
}