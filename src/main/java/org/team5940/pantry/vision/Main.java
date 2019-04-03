package org.team5940.pantry.vision;

import java.util.ArrayList;
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

        // for (int i = 0; i < contours.size(); i++) {
        //     Imgproc.drawContours(yes, contours, i, new Scalar(0, 255, 255), -1);
        // }

        // print(yes.rows());

        ArrayList<DetectedContour> contourList = new ArrayList<>();

        // Random  = new Random();
        for (MatOfPoint contour : contours) {
            print("finding bouding rectangle...");

            var buffer = new BetterPoint(0.03 * (double) yes.rows(), 0.03 * (double) yes.rows());

            print(buffer);

            var boundingRect = Imgproc.boundingRect(contour);

            Rect rectCrop = new  Rect(new BetterPoint(boundingRect.tl()).minus(buffer), new Size(boundingRect.width + buffer.x*2, boundingRect.height + buffer.y*2)); 
            Mat croppedImg = image32S.submat(rectCrop);

            Imgcodecs.imwrite("images/output/markedUPMemed.jpg", croppedImg);

            DetectedContour newContour = new DetectedContour(croppedImg, boundingRect.tl(), contour);
            contourList.add(newContour);

            // var tl_ = boundingRect.tl();
            // var br = new BetterPoint(boundingRect.br()).plus(buffer);
            // var tl = new BetterPoint(tl_);
            // var tr = new BetterPoint(boundingRect.tl().x + boundingRect.width, boundingRect.tl().y).plus(
            //     new Point(
            //         buffer.x, -buffer.y
            //     )
            // );
            // var bl = new BetterPoint(boundingRect.tl().x, boundingRect.tl().y + boundingRect.height).plus(
            //     new Point(
            //         -buffer.x, buffer.y
            //     ));
            // tl = tl.minus(buffer);

            // Imgproc.drawMarker(yes, tl, new Scalar(0, 0, 255));
            // Imgproc.drawMarker(yes, tr, new Scalar(0, 0, 255));
            // Imgproc.drawMarker(yes, bl, new Scalar(0, 0, 255));
            // Imgproc.drawMarker(yes, br, new Scalar(0, 0, 255));



            // print(point);


            // Imgproc.drawContours(img, contours, i, new Scalar(r.nextInt(255), r.nextInt(255), r.nextInt(255)), -1);
        }

        Imgcodecs.imwrite("images/output/CargoStraightDark48in.jpg", image32S);
        Imgcodecs.imwrite("images/output/markedUP.jpg", yes);



        // image32S = new Mat();
        // contourImg.convertTo(image32S, CvType.CV_32SC1);

        // Imshow imshow = new Imshow("Contours");
        // imshow.showImage(contourImg); 

        

        // HighGui.toBufferedImage(contourImg);

    }

    public static class DetectedContour {

        Mat image;
        Point imageCornerLoc;
        MatOfPoint contour;

        public DetectedContour(Mat image, Point imageCornerLoc, MatOfPoint contour) {
            this.image = image;
            this.imageCornerLoc = imageCornerLoc;
            this.contour = contour;
        }

    }


    static void print(Object o) {
        System.out.println(o);
    }

}