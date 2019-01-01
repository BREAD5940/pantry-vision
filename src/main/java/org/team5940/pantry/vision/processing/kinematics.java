package org.team5940.pantry.vision.processing;


/**
 * This is the processing component of pantry vision. This lib
 * takes data in from the vision processing node after it's been
 * processed and accounts for latency. This will also be able to
 * do things like reference frame conversions, provided it's given
 * that information. 
 */


public class Kinematics {
    double[] res, fov, pose, crosshair;

    /**
     * The distance calculation mode of this instance of 
     * pantry-vision. This changes how the straight line 
     * distance to the target is calculated.
     */
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

    public Kinematics(double[] res, double[] fov, double[] pose, double crosshair[], distanceMode distancemode, latencyMode latencymode) {
        this.res = res;
        this.fov = fov;
        this.pose = pose;
        this.crosshair = crosshair;
        this.latencymode = latencymode;
        this.distancemode = distancemode;
    }
}