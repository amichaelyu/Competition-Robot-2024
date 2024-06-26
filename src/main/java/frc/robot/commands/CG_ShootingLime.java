package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_ShootingLime extends ParallelDeadlineGroup {
    public CG_ShootingLime() {
        super(
            new SequentialCommandGroup(
                    new SwerveRotatePose(),
                    new ShooterTilterArmed(),
                    new IndexerKick()
            ),
            new TilterPose(),
            new ShooterPose()
        );
    }
}