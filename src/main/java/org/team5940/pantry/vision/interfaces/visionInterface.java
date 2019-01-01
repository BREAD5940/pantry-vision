package org.team5940.pantry.vision.interfaces;

interface visionProcessor {

    /**
     * Define the camera pose. The format is X, Y, Z, yaw,
     * pitch, roll. (where 0,0,0,0,0,0 would be the dead 
     * center of the robot pointing straight ahead)
     */
    double[] cameraPose = {0,0,0,0,0,0};

    public double getTargetCount();
    public double[] getTargetRawPosition();
    /** Get the target position in the reference frame of the robot */
    public double[] getTargetPosition();
    public double getTargetArea();
    public double getTargetSkew();
    public double getPipelineLatency();

    public double getTargetRange();
    

}