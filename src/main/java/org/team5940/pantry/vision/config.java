package org.team5940.pantry.vision;

public class config {
    // public static double[] crosshair = {0,0}; // non final because crosshair might need to move

    public static class limelight_cam {
        public static final double[] pose = {0,0,0,0,0};
        public static final double[] resolution = {320,240};
        public static final double[] fov = {54,41};
    }
    public static class ms_livecam {
        public static final double[] pose = {0,0,0,0,0};
        public static final double[] resolution = {320,240};
        public static final double[] fov = {60,34.3}; // TODO calibrate this!
    }
}