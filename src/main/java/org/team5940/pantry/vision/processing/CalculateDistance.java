package org.team5940.pantry.vision.processing;

/**
 * This class calculates the distance to a vision target. The
 * distance can be calculated based on target X and Y height,
 * or based on angle and a desired angle
 */

public class CalculateDistance {
  // double x, y, z;
  double[] targetLoc = {0,0,0};
  double a, b, c;

  /**
   * Calculate the horizontal distance to a target based
   * on the target angle of the goal and the
   * expected angle of the goal.
   * @param cameraPose
   * @param goalHeight known altitude of the goal above the ground
   * @param currentAngle of the vision target
   * @return distance to the plane of the goal
   *   (basically the base of the triangle to the goal, with the
   *    hypotinuse as the straight line distance)
   */
  public double horizontalDistanceFromAngle(double[] cameraPose, double goalHeight, double currentAngle) {
    return  (goalHeight - cameraPose[2]) / Math.tan(Math.toRadians(currentAngle));
  }

  /**
   * Calculate the straight line distance to a target based
   * on the target angle of the goal and the
   * expected angle of the goal.
   * @param cameraPose[]
   * @param goalHeight known altitude of the goal above the ground
   * @param currentAngle of the vision target in degrees
   * @return distance to the goal, straight line (hypotinuse) distance
   */
  public double straightLineDistanceFromAngle(double[] cameraPose, double goalHeight, double currentAngle) {
    a = horizontalDistanceFromAngle(cameraPose, goalHeight, currentAngle);
    b = goalHeight;
    c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    return c;
  }

  /**
   * Calculate the XYZ position of a target based on the angles and distance to the goal
   * 
   * @param targetAngles[] in the format yaw, pitch
   * @param goalDistance straight line goal distance
   * @return a double[] in the form x, y, z
   */
  public double[] calculateXYZ(double[] targetAngles, double goalDistance) {
    targetLoc[1] = Math.cos(Math.toRadians(targetAngles[1])) * goalDistance * Math.cos(Math.toRadians(targetAngles[0]));
    targetLoc[0] = Math.cos(Math.toRadians(targetAngles[1])) * goalDistance * Math.sin(Math.toRadians(targetAngles[0]));
    targetLoc[2] = goalDistance * Math.sin(Math.toRadians(targetAngles[1]));
    return targetLoc;
  }

}