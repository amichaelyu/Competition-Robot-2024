package frc.robot.subsystems;

import com.team3181.lib.util.LimelightHelpers;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.FieldConstants;
import frc.robot.FieldConstants.Speaker;

public class Limelight extends SubsystemBase {

    public enum Pipelines {
        APRIL(0);

        private final int num;
        Pipelines(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    public enum LED {
        PIPELINE(0), OFF(1), BLINK(2), ON(3);

        private final int num;

        LED(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    public enum CameraMode {
        VISION_PROCESSING(0), DRIVER_CAMERA(1);

        private final int num;

        CameraMode(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    private final String limelightName = "";
    private final NetworkTable limelight = LimelightHelpers.getLimelightNTTable(limelightName);

    public Limelight() {
        setLEDs(LED.OFF);
        setPipeline(Pipelines.APRIL);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("distance to target", distanceToTarget());
        SmartDashboard.putBoolean("has target", hasTarget());
    }

    public boolean hasTarget() {
        return LimelightHelpers.getTV(limelightName);
    }

    public Pose2d getBotPose() {
        NetworkTableEntry botposeEntry = null;
        if (LimelightHelpers.getTV(limelightName)) {
//            if (DriverStation.getAlliance().isPresent()) {
//                if (DriverStation.getAlliance().get() == Alliance.Blue) {
//                    botposeEntry = limelight.getEntry("botpose_wpiblue");
//                } else if (DriverStation.getAlliance().get() == Alliance.Red) {
//                    botposeEntry = limelight.getEntry("botpose_wpired");
//                }
                botposeEntry = limelight.getEntry("botpose_wpiblue");
                return new Pose2d(botposeEntry.getDoubleArray(new double[7])[0], botposeEntry.getDoubleArray(new double[7])[1], new Rotation2d());
//            }
        }
        return null;
    }

    // in meters
    public double distanceToTarget() {
        Pose2d botPose = getBotPose();
        if (botPose != null) {
                if (DriverStation.getAlliance().isPresent()) {
                    Pose2d adjustedSpeaker = FieldConstants.allianceFlipper(new Pose2d(Speaker.centerSpeakerOpening.getX(), Speaker.centerSpeakerOpening.getY(), new Rotation2d()), DriverStation.getAlliance().get());
                    return distance(botPose, adjustedSpeaker);
                }
        }
        return 0.0;
    }

    public void setPipeline(Pipelines pipeline) {
        limelight.getEntry("pipeline").setDouble(pipeline.getNum());
    }

    public void setCameraModes(CameraMode camera) {
        limelight.getEntry("camMode").setDouble(camera.getNum());
    }

    public void setLEDs(LED led) {
        limelight.getEntry("ledMode").setDouble(led.getNum());
    }

    private double distance(Pose2d pose1, Pose2d pose2) {
        Pose2d relPose = pose1.relativeTo(pose2);
        return Math.sqrt(Math.pow(relPose.getX(), 2) + Math.pow(relPose.getY(), 2));
    }

}