package org.team5940.pantry.vision.processing.latencycompensation;

// import org.team5940.pantry.vision.processing.Kinematics.latencyMode;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;



public class latencyMoving {
    ADXRS450_Gyro gyro;

    /** 
     * A log of a bunch of different angles and their associated timestamps.
     * Stores the timestamp [0] and value [1].
     */
    private int logLen = 50;
    private double[][] locationLog = new double[logLen][4];
    private double deltaAngle, selectedOldAngle;

    public latencyMoving(ADXRS450_Gyro gyro) {
        this.gyro = gyro;
    }

    private void updateCache(){
        // this.oldAngle3 = this.oldAngle2;
        // this.oldAngle2 = this.oldAngle1;
        // this.oldAngle1 = this.locationLog; // shift everything down, the "current angle" (now outdated) is moved to the cache
        // this.locationLog[0] = Timer.getFPGATimestamp();
        // this.locationLog[1] = gyro.getAngle();
        for(int i=logLen - 1; i>0; i--){
            locationLog[i] = locationLog[i-1];
        }
    }

    public void init(double dLeft, double dRight){
        locationLog[0][0] = Timer.getFPGATimestamp();
        locationLog[0][1] = gyro.getAngle();
        locationLog[0][2] = dLeft;
        locationLog[0][3] = dRight;
        // Fill the array with the starting values so things don't get c o n f u s e d
        for (int i=1; i<logLen; i++){
            locationLog[i] = locationLog[0];
        }
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
        if ( locationLog[0] - imageTimestamp < 20 ) {
            selectedOldAngle = oldAngle1[1];
        }
        else if ( locationLog[0] - imageTimestamp < 40 ) {
            selectedOldAngle = oldAngle2[1];
        }
        else if ( locationLog[0] - imageTimestamp < 20 ) {
            selectedOldAngle = oldAngle3[1];
        }
        else {
            // The angle is too old, IDK man
            selectedOldAngle = locationLog[1]; // just set it to zero (TODO make sure this is actuallly zero)
        }
        // Calculate the angle delta
        deltaAngle = locationLog[1] - selectedOldAngle;

        /* Therefore, we should "move" the target over by the delta to compensate
        if deltaAngle is positive that means that we are more clockwise than we
        used to be, so the target has effectivelly phased counter-clockwise
        relative to us. therefore we decrement the targetYaw by the delta
        */ 
        targetYaw -= deltaAngle;
        return targetYaw;
    }

}