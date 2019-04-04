package org.team5940.pantry.vision;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

    public static void main(String[] args) {

        // System.getProperties().set("java.library.path", "libs/libopencv_java401.dylib");  

        GripPipeline pipeline = new GripPipeline();

        String filePath = "images/2019/CargoStraightDark48in.jpg";

        Mat img = Imgcodecs.imread(filePath);

        pipeline.process(img);

        var hsvImage = pipeline.hsvThresholdOutput();

        var contours = pipeline.filterContoursOutput();


        Mat image32S = new Mat();
        img.convertTo(image32S, CvType.CV_32SC1);
        var yes = img;

        ArrayList<DetectedContour> contourList = new ArrayList<>();

        for (MatOfPoint contour : contours) {
            print("finding bouding rectangle...");

            var buffer = new BetterPoint(0.03 * (double) yes.rows(), 0.03 * (double) yes.rows());

            print(buffer);

            var boundingRect = Imgproc.boundingRect(contour);

            var yes = Imgproc.minAreaRect(contour);

            Rect rectCrop = new  Rect(new BetterPoint(boundingRect.tl()).minus(buffer), new Size(boundingRect.width + buffer.x*2, boundingRect.height + buffer.y*2)); 
            Mat croppedImg = image32S.submat(rectCrop);

            Imgcodecs.imwrite("images/output/markedUPMemed.jpg", croppedImg);

            DetectedContour newContour = new DetectedContour(croppedImg, boundingRect.tl(), contour);
            contourList.add(newContour);

        }

        Imgcodecs.imwrite("images/output/CargoStraightDark48in.jpg", image32S);
        Imgcodecs.imwrite("images/output/markedUP.jpg", yes);

        ListIterator<DetectedContour> contourListIterator= contourList.listIterator();
        while (contourListIterator.hasNext()) {
            var nextIndex = contourListIterator.nextIndex();
            var nextContour = contourListIterator.next();

            Imgcodecs.imwrite(String.format("images/output/markedUPNumero%s.jpg", nextIndex), nextContour.image);
        }

    }

    public static class DetectedContour {

        Mat image;
        Point imageCornerLoc;
        MatOfPoint contour;
        Point[] corners_;

        public DetectedContour(Mat image, Point imageCornerLoc, MatOfPoint contour) {
            this.image = image;
            this.imageCornerLoc = imageCornerLoc;
            this.contour = contour;

            detectCorners(image);
        }

        public void detectCorners(Mat image) {
            // # find Harris corners
            // gray = cv2.cvtColor(tape, cv2.COLOR_BGR2GRAY)
            var gray = new Mat();
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
            // gray = np.float32(gray)
            // dst = cv2.cornerHarris(gray, 2, 3, 0.04)
            var dst = new Mat();
            Imgproc.cornerHarris(gray, dst, 2, 3, 0.04);
            // dst = cv2.dilate(dst,None)
            // ret, dst = cv2.threshold(dst,0.01*dst.max(),255,0)
            // dst = Imgproc.threshold(dst, dst, 0.01*dst.maxv, 255, 0);
            // dst = np.uint8(dst)
        
            // # find centroids
            // ret, labels, stats, centroids = cv2.connectedComponentsWithStats(dst)
            Mat labels = new Mat(), stats = new Mat(), centroids = new Mat();
            Imgproc.connectedComponentsWithStats(dst, labels, stats, centroids);

            print("centroids");
            print(centroids);

            // # define the criteria to stop and refine the corners
            // criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 100, 0.001)
            // corners = cv2.cornerSubPix(gray,np.float32(centroids),(5,5),(-1,-1),criteria)
        }

    }


    static void print(Object o) {
        System.out.println(o);
    }

}