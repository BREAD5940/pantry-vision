package org.team5940.pantry.vision;

import org.team5940.pantry.vision.io.NetworkTableIO;
import org.team5940.pantry.vision.processing.Kinematics;
import org.team5940.pantry.vision.processing.Kinematics.distanceMode;
import org.team5940.pantry.vision.processing.Kinematics.latencyMode;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

/**
 * This is the main vision class. Call it to do everything
 * that you could ever need relating to vision. This class
 * will be able to interface with both a Limelight and a Pi
 * or Kangaroo coprocessor, provided they provide data in
 * the same format. All the other processing is done on the
 * Pi/coprocessor. This means that all that this class will
 * get as inputs from the coprocessor is:
 * <p>
 * tv	Whether the limelight has any valid targets (0 or 1)
 * tx	Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
 * ty	Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
 * ta	Target Area (0% of image to 100% of image)
 * ts	Skew or rotation (-90 degrees to 0 degrees)
 * tl	The pipeline’s latency contribution (ms) Add at least 11ms for image capture latency.
 * <p>
 * This way, we can keep this class abstract from the 
 * specific coprocessor chosen (limelight vs pi vs 'roo)
 * <p>
 * This class will include methods for initilizastion of a 
 * coprocessor interface, periodic updates that return data
 * specific to the robot. Latency compensation will be 
 * calculated as part of that update process and as such,
 * some data will need to be passed in based on what kind of
 * latency compensation is being performed (gyro vs full 
 * encoder mode).
 * <p>
 * The flow of this program should be as follows:
 * <p>
 * Set everything up, init variables, etc
 * On loop call:
 * Querry target status (count)
 * Get the X_px, Y_px, timestamp
 * Based on the mode, perform latsency compensation and rigid
 *    coordinate transforms if necessary
 * 
 */

public class Main {
    NetworkTableIO tableIO;
    ADXRS450_Gyro gyro;
    double[] NTdata;
    double[] crosshairPos;
    double[] cameraRes;
    double[] cameraPose;
    Kinematics kinematics;


    public static enum cameraMode
    {
        LIMELIGHT, LIVECAM;
    }

    public static enum distanceMode
    {
        DIMENSION, AREA, ELEVATION;
    }

    /** 
     * The latency compensation mode of the instance of pantry-vision. This
     * changes what mode latency compensation will run in.
     */
    public static enum latencyMode 
    {
        IN_PLACE, MOVING, NONE;
    }
    /** Enums for mode */
    latencyMode latencymode; 
    distanceMode distancemode;
    cameraMode cameramode;

    /** 
     * Initilize everything we need from our coprocessor (ahem, networktables).
     * Pass in data via a String[]
     * <p>
     * Index 0: tableID
     * 
     * @param args 
     * @param mode for latency compensation
     * 
     * TODO check that they gyro passed in will work as expected
     */
    public void main(String[] args, ADXRS450_Gyro gyro, cameraMode camera, distanceMode distancemode, latencyMode latencymode) {
        tableIO = new NetworkTableIO(args[0]);
        // this.latencymode = latencymode;
        // this.cameramode = cameramode;
        // this.distancemode = distancemode;
        this.gyro = gyro;

        if ( cameramode == cameraMode.LIMELIGHT ) {
            kinematics = new Kinematics(config.limelight_cam.resolution, 
                config.limelight_cam.fov, 
                config.limelight_cam.pose, 
                config.crosshair,
                distancemode,
                latencymode);
        }
        else if ( cameramode == cameramode.LIVECAM ) {
            kinematics = new Kinematics(config.ms_livecam.resolution, 
                config.ms_livecam.fov, 
                config.ms_livecam.pose, 
                config.crosshair,
                distancemode,
                latencymode);
        }


    }

    
    public double getGyro() {
        return gyro.getAngle();
    }

    /** 
     * Return the current target position and update kinematics
     * @return a double[] with target x, y, z and heading, elevation, range.
     * Latency compensation is done in "in place" mode
     */
    public double[] updateInPlace(){
        return kinematics.updateInPlace();
    }
    
    /** 
y     * @return a double[] with target x, y, z and heading, elevation, range.
     * Latency compensation is done in "moving" mode
     */
    public double[] updateMoving(double m_left_pos, double m_right_pos){
        return kinematics.updateMoving(m_left_pos, m_right_pos);
    }

    /** 
     * Return the current target position and update kinematics
     * @return a double[] with target x, y, z and heading, elevation, range.
     * Latency compensation is not done
     */
    public double[] update() {
        return kinematics.update();
    }


}