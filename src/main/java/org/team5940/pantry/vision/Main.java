package org.team5940.pantry.vision;

import java.util.Random;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

    public static void main(String[] args) {

        // System.getProperties().set("java.library.path", "libs/libopencv_java401.dylib");  

        GripPipeline pipeline = new GripPipeline();

        String filePath = "images/2019/LoadingStraightDark48in.jpg";

        Mat img = Imgcodecs.imread(filePath);

        pipeline.process(img);

        var contours = pipeline.getFindContoursOutput();

        Mat image32S = new Mat();
        img.convertTo(image32S, CvType.CV_32SC1);


        Random r = new Random();
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(contours, contours, i, new Scalar(r.nextInt(255), r.nextInt(255), r.nextInt(255)), -1);
        }
        //Converting Mat back to Bitmap
        Utils.matToBitmap(contours, currentBitmap);
        imageView.setImageBitmap(currentBitmap);


        // image32S = new Mat();
        // contourImg.convertTo(image32S, CvType.CV_32SC1);

        // Imshow imshow = new Imshow("Contours");
        // imshow.showImage(contourImg); 

        

        // HighGui.toBufferedImage(contourImg);

    }




    static void print(Object o) {
        System.out.println(o);
    }

}