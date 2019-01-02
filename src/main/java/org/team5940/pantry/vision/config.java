package org.team5940.pantry.vision;

public class config {
    // public static double[] crosshair = {0,0}; // non final because crosshair might need to move
    public static double averageLatency = 0.01; // TODO make this calibrate itself? and tune it

    public static class limelight_cam {
        public static final double[] pose = {0,0,0,0,0}; // [0] x offset, [1] y offset, [2] z offset, [3] yaw offset, [4] pitch offset (positive angle is clockwise)
        public static final double[] resolution = {320,240};
        public static final double[] fov = {54,41};
    }
    public static class ms_livecam {
        public static final double[] pose = {0,0,0,0,0};
        public static final double[] resolution = {320,240};
        public static final double[] fov = {60,34.3}; // TODO calibrate this!
    }
}