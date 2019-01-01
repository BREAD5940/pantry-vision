package org.team5940.pantry.vision.processing;

/**
 * This is the processing component of pantry vision. This lib
 * takes data in from the vision processing node after it's been
 * processed and accounts for latency. This will also be able to
 * do things like reference frame conversions, provided it's given
 * that information. 
 */


public class kinematics {
    double xRes, yRes, xFOV, yFOV;

    public kinematics(double[] cameraInfo) {
        this.xRes = cameraInfo[0];
        this.yRes = cameraInfo[1];
        this.xFOV = cameraInfo[2];
        this.yFOV = cameraInfo[3];
    }
}