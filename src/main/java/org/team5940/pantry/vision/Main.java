package org.team5940.pantry.vision;

import edu.wpi.first.wpilibj.vision.VisionRunner;

// import org.team5940.pantry.vision.io.NetworkTableIO;
// import edu.wpi.first.wpilibj.ADXRS450_Gyro;

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
    public void main(String[] args) {
       GripPipeline pipeline = new GripPipeline();

    }



}