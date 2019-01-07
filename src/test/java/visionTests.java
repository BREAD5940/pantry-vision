import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.team5940.pantry.vision.processing.CalculateDistance;

import org.team5940.pantry.vision.processing.ReferenceFrameConversion;

public class visionTests {
    CalculateDistance distanceCalc = new CalculateDistance();

    @Test
    public void testDistanceFromAngle() {

        
        double[] cameraPose1 = new double[]{0,0,1,0,0};
        double goalElevation1 = 6.23;
        double targetAngle1 = 57;
        double expected1 = 3.396402;
        double actual1 = distanceCalc.horizontalDistanceFromAngle(cameraPose1, goalElevation1, targetAngle1);
        assertEquals(expected1, actual1, 0.02);

        double[] cameraPose2 = new double[]{0,0,0,0,0};
        double goalElevation2 = 7;
        double targetAngle2 = 21;
        double expected2 = 18.236;
        double actual2 = distanceCalc.horizontalDistanceFromAngle(cameraPose2, goalElevation2, targetAngle2);
        assertEquals(expected2, actual2, 0.02);
    }

    @Test
    public void testStraightLineDistanceFromAngle() {
        //  public double straightLineDistanceFromAngle(double[] cameraPose, double goalHeight, double currentAngle) {
        double[] cameraPose1 = new double[]{0,0,1,0,0};
        double goalElevation1 = 6.23;
        double targetAngle1 = 57;
        double expected1 = 7.095664;
        double actual1 = distanceCalc.straightLineDistanceFromAngle(cameraPose1, goalElevation1, targetAngle1);
        assertEquals(expected1, actual1, 0.02);
        
    }

    @Test
    public void testXYZCalculation() {
        //  public double[] calculateXYZ(double[] cameraPose, double[] targetAngles, double goalDistance) {
        double[] targetAngles1 = new double[]{0, 45};
        double goalDistance1 = Math.sqrt(2);
        double[] expected1 = new double[]{0,1,1};
        double[] actual1 = (distanceCalc.calculateXYZ(targetAngles1, goalDistance1));
        assertArrayEquals(expected1, actual1, 0.02);

        double[] targetAngles2 = new double[]{-30, 60};
        double goalDistance2 = 3;
        double[] expected2 = new double[]{-0.75,1.299,2.598};
        double[] actual2 = (distanceCalc.calculateXYZ(targetAngles2, goalDistance2));
        assertArrayEquals(expected2, actual2, 0.02);

        double[] targetAngles3 = new double[]{30, -60};
        double goalDistance3 = 3;
        double[] expected3 = new double[]{0.75,1.299, -2.598};
        double[] actual3 = (distanceCalc.calculateXYZ(targetAngles3, goalDistance3));
        assertArrayEquals(expected3, actual3, 0.02);

    }

}