package org.team5940.pantry.vision.processing;

/**
 * This class calculates the distance to a vision target. The
 * distance can be calculated based on target X and Y height,
 * or based on angle and a desired angle
 */

public class CalculateDistance {
  // double x, y, z;
  double[] targetLoc = {0,0,0};

  /**
   * Calculate the horizontal distance to a target based
   * on the target angle of the goal and the
   * expected angle of the goal.
   * @param camearPose
   * @param goalHeight known altitude of the goal above the ground
   * @param currentAngle of the vision target
   * @return distance to the plane of the goal
   *   (basically the base of the triangle to the goal, with the
   *    hypotinuse as the straight line distance)
   */
  public double horizontalDistanceFromAngle(double[] cameraPose, double goalHeight, double currentAngle) {
    return  (goalHeight - cameraPose[2]) / Math.tan(currentAngle);
  }

  /**
   * Calculate the straight line distance to a target based
   * on the target angle of the goal and the
   * expected angle of the goal.
   * @param camearPose
   * @param goalHeight known altitude of the goal above the ground
   * @param currentAngle of the vision target in degrees
   * @return distance to the plane of the goal
   *   (basically the base of the triangle to the goal, with the
   *    hypotinuse as the straight line distance)
   */
  public double straightLineDistanceFromAngle(double[] cameraPose, double goalHeight, double currentAngle) {
    return  (goalHeight - cameraPose[2]) / Math.toDegrees(Math.sin(currentAngle));
  }

  public double[] calculateXYZ(double[] cameraPose, double[] targetAngles, double goalDistance) {
    targetLoc[0] = (Math.toDegrees(Math.cos(targetAngles[1])) * goalDistance) * (Math.toDegrees(Math.cos(targetAngles[0])));
    targetLoc[1] = (Math.toDegrees(Math.cos(targetAngles[1])) * goalDistance) * (Math.toDegrees(Math.sin(targetAngles[0])));
    targetLoc[2] = goalDistance * Math.toDegrees(Math.sin(targetAngles[1]));
    return targetLoc;
  }

}