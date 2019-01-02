package org.team5940.pantry.vision.processing;


/**
 * This is the processing component of pantry vision. This lib
 * takes data in from the vision processing node after it's been
 * processed and accounts for latency. This will also be able to
 * do things like reference frame conversions, provided it's given
 * that information. 
 */


public class Kinematics {
    double[] cameraPose;

    /**
     * The distance calculation mode of this instance of 
     * pantry-vision. This changes how the straight line 
     * distance to the target is calculated.
     */


    public Kinematics(double[] cameraPose) {
        this.cameraPose = cameraPose
    }


    
}