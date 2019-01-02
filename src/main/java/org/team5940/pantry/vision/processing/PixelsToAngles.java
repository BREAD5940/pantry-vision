package org.team5940.pantry.vision.processing;

import org.team5940.pantry.vision.config;
import org.team5940.pantry.vision.Main.cameraMode;

public class PixelsToAngles {

    double[] fov, resolution;
    double focal_length;



    public PixelsToAngles(cameraMode cameramode) {
        if (cameramode == cameraMode.LIMELIGHT) {
            this.fov = config.limelight_cam.fov;
            this.resolution = config.limelight_cam.resolution;
        }
        else if (cameramode == cameraMode.LIVECAM) {
            this.fov = config.ms_livecam.fov;
            this.resolution = config.ms_livecam.resolution;
        }
        double x_focal_length = this.resolution[0] / (2*Math.tan(this.fov[0]/2));
        double y_focal_length = this.resolution[1] / (2*Math.tan(this.fov[1]/2));
        this.focal_length = (x_focal_length + y_focal_length) / 2;
    }

    public double pixelsToAngle(double pixels) {
        return Math.toDegrees(
          Math.atan(pixels / this.focal_length));
    }
}