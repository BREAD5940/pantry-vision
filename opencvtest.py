import cv2
import numpy as np 
import os.path


 
def isWhithinRange(value, expected, tolerence):
    upperLimit = expected + tolerence
    lowerLimit = expected - tolerence

    if (value > lowerLimit) & (value < upperLimit):
        return True
    else:
        return False

# read and scale down image
# wget https://bigsnarf.files.wordpress.com/2017/05/hammer.png

iteration = 251

while iteration < 500:

    imgpath = '2016-vision-master\\imgproc\\RealFullField\\%s.jpg' % iteration

    if os.path.exists(imgpath):

        # imgpath = '2016-vision-master\\imgproc\\RealFullField\\62memeeed.jpg'
        iteration = iteration + 1

        img = cv2.pyrDown(cv2.imread(imgpath, cv2.IMREAD_UNCHANGED))
        print "iteration: %s" % iteration

        


        # threshold image
        # ret, threshed_img = cv2.threshold(cv2.cvtColor(img, cv2.COLOR_BGR2GRAY),
        #                 127, 255, cv2.THRESH_BINARY)

        # let's try an hsv threshold
        frame_HSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        threshed_img = cv2.inRange(frame_HSV, (80, 10, 120), (125, 255, 225))

        # Constants
        kernel = np.ones((2,2),np.uint8)
        erosion = cv2.erode(img,kernel,iterations = 1)

        dilated_image = cv2.dilate(threshed_img,kernel,iterations = 1)
        cv2.imshow("dilation ", dilated_image)

        # find contours and get the external one
        image, contours, hier = cv2.findContours(dilated_image, cv2.RETR_TREE,
                        cv2.CHAIN_APPROX_SIMPLE)
        
        cv2.imshow("literally an image, loop %s" % iteration, img)
        cv2.imshow("threshed image, loop %s" % iteration, threshed_img)




        # with each contour, draw boundingRect in green
        # a minAreaRect in red and
        # a minEnclosingCircle in blue

        imageToMarkup = img
        contoursMatched = 0
        loop = 0
        for c in contours:
            loop += 1
            # get the bounding rect
            x, y, w, h = cv2.boundingRect(c)
            # draw a green rectangle to visualize the bounding rect

        
            # It is the ratio of width to height of bounding rect of the object.
            aspect_ratio = float(w)/h
            area = cv2.contourArea(c)
            rect_area = w*h
            # Extent is the ratio of contour area to bounding rectangle area.
            extent = float(area)/rect_area
            # This is the area 
            area = cv2.contourArea(c)


            # compute the center of the contour
            topmost = tuple(c[c[:,:,1].argmin()][0])

            # print topmost
            string = "%s" % loop    
            font                   = cv2.FONT_HERSHEY_SIMPLEX
            # bottomLeftCornerOfText = (10,500)
            fontScale              = 0.5
            fontColor              = (100,100,155)
            lineType               = 2

            cv2.putText(threshed_img, string, 
                topmost, 
                font, 
                fontScale,
                fontColor,
                lineType)      
                
            # print"%s,%s,%s" % (aspect_ratio, extent, area)

            if (area > 100):
                print "contour number %s aspect ratio: %s extent: %s area: %s" % (loop, aspect_ratio, extent, area)
            
            if ( aspect_ratio > 0.9 ) & (aspect_ratio < 3) & ( extent > 0.12 ) & ( extent < 0.35 )  & ( area > 160 ):
            # if isWhithinRange(aspect_ratio, 1.65, 0.75) & isWhithinRange(extent, 0.2, 0.1) & ( area > 200 ):
                contoursMatched += 1
                # print "Contour matched!!!          ----          aspect ratio: %s extent: %s area: %s" % (aspect_ratio, extent, area)
                
                print"%s,%s,%s" % (aspect_ratio, extent, area)

                cv2.imshow("contours", imageToMarkup)
                cv2.rectangle(imageToMarkup, (x, y), (x+w, y+h), (0, 255, 0), 2)
                # get the min area rect
                rect = cv2.minAreaRect(c)
                box = cv2.boxPoints(rect)
                # convert all coordinates floating point values to int
                box = np.int0(box)
                # draw a red 'nghien' rectangle
                cv2.drawContours(imageToMarkup, [box], 0, (0, 0, 255))
            
                # finally, get the min enclosing circle
                (x, y), radius = cv2.minEnclosingCircle(c)
                # convert all values to int
                center = (int(x), int(y))
                radius = int(radius)
                # and draw the circle in blue
                # imageToMarkup = cv2.circle(imageToMarkup, center, radius, (255, 0, 0), 2)



                
            # else:
                # print "contour does not match!"

        # print "Total contours: %s Contours matched: %s" % (len(contours), contoursMatched)


        # cv2.drawContours(imageToMarkup, contours, -1, (255, 255, 0), 1)
        
        cv2.imshow("contours", imageToMarkup)
        
        cv2.imshow("threshed image plus markup", threshed_img)

        cv2.waitKey(0)

        cv2.destroyAllWindows()
            # frame = cv.imread(",1)
    else:
        iteration += 1
