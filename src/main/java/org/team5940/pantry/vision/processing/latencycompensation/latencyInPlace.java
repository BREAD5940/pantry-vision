package org.team5940.pantry.vision.processing.latencycompensation;

// import org.team5940.pantry.vision.processing.Kinematics.latencyMode;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;


public class latencyInPlace {
    ADXRS450_Gyro gyro;

    /** 
     * A log of a bunch of different angles and their associated timestamps.
     * Stores the timestamp [0] and value [1].
     */
    private double[] currentAngle = new double[2];
    private double[] oldAngle1 = new double[2];
    private double[] oldAngle2 = new double[2];
    private double[] oldAngle3 = new double[2];
    private double deltaAngle, selectedOldAngle;

    public latencyInPlace(ADXRS450_Gyro gyro) {
        this.gyro = gyro;
    }

    private void updateCache(){
        this.oldAngle3 = this.oldAngle2;
        this.oldAngle2 = this.oldAngle1;
        this.oldAngle1 = this.currentAngle; // shift everything down, the "current angle" (now outdated) is moved to the cache
        this.currentAngle[0] = Timer.getFPGATimestamp();
        this.currentAngle[1] = gyro.getAngle();
    }

    public void init(){
        currentAngle[0] = Timer.getFPGATimestamp();
        currentAngle[1] = gyro.getAngle();
        oldAngle3 = oldAngle2 = oldAngle1 = currentAngle;
    }

    /**
     * Run until the vision command is done (has to be called, I'm to lazy to make
     * this a command). Just feed it a target position in x
     * 
     * @param targetYaw
     * @param imageTimestamp that the image was taken at
     * @return
     */
    public double execute(double targetYaw, double imageTimestamp){
        updateCache();
        if ( currentAngle[0] - imageTimestamp < 20 ) {
            selectedOldAngle = oldAngle1[1];
        }
        else if ( currentAngle[0] - imageTimestamp < 40 ) {
            selectedOldAngle = oldAngle2[1];
        }
        else if ( currentAngle[0] - imageTimestamp < 20 ) {
            selectedOldAngle = oldAngle3[1];
        }
        else {
            // The angle is too old, IDK man
            selectedOldAngle = currentAngle[1]; // just set it to zero (TODO make sure this is actuallly zero)
        }
        // Calculate the angle delta
        deltaAngle = currentAngle[1] - selectedOldAngle;

        /* Therefore, we should "move" the target over by the delta to compensate
        if deltaAngle is positive that means that we are more clockwise than we
        used to be, so the target has effectivelly phased counter-clockwise
        relative to us. therefore we decrement the targetYaw by the delta
        */ 
        targetYaw -= deltaAngle;
        return targetYaw;
    }

}