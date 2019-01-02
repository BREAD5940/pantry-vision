package org.team5940.pantry.vision.processing;


/**
 * This is the processing component of pantry vision. This lib
 * takes data in from the vision processing node after it's been
 * processed and accounts for latency. This will also be able to
 * do things like reference frame conversions, provided it's given
 * that information. 
 */


public class ReferenceFrameConversion {
    double[] cameraPose, targetAngles;

    /**
     * The distance calculation mode of this instance of 
     * pantry-vision. This changes how the straight line 
     * distance to the target is calculated.
     */


    public ReferenceFrameConversion(double[] cameraPose) {
        this.cameraPose = cameraPose;
    }

    /**
     * Convert the target theta_x, theta_y angle measured by the camera
     * into the theta_x, theta_y angle relative to the robot
     * @param cameraTargetLoc in (yaw, pitch) [(theta_x, theta_y)] format
     */
    public double[] convertToRobotAngleReferenceFrame(double[] cameraTargetLoc) {
        cameraTargetLoc[0] += cameraPose[3];
        cameraTargetLoc[1] +=cameraPose[4];
        return cameraTargetLoc;
    }

    /**
     * Convert a target (x, y, z) from the camera's referenc frame
     * into the robot's reference frame. Make sure that you've already normalized the
     * target angle! otherwise things will break
     * @param x,y,z of the target (paralel to the robot reference frame! if not call convertToRobotAngleReferenceFrame first!)
     * @return x,y,z normalized to the robot reference frame
     */
    public double[] convertToRobotDistanceReferenceFrame(double[] targetXYZ) {
        targetXYZ[0] -= cameraPose[0];
        targetXYZ[1] -= cameraPose[1];
        targetXYZ[2] -= cameraPose[2];
        return targetXYZ;
    }


    
}