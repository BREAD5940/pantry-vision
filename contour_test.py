import cv2
import numpy as np 
 
def isWhithinRange(value, expected, tolerence):
    upperLimit = expected + tolerence
    lowerLimit = expected - tolerence

    if (value > lowerLimit) & (value < upperLimit):
        return True
    else:
        return False

# read and scale down image
# wget https://bigsnarf.files.wordpress.com/2017/05/hammer.png

iteration = 11

while iteration < 200:
    imgpath = '2016-vision-master\\imgproc\\RealFullField\\%s.jpg' % iteration
    img = cv2.pyrDown(cv2.imread(imgpath, cv2.IMREAD_UNCHANGED))

    print iteration

    iteration = iteration + 4


    # threshold image
    # ret, threshed_img = cv2.threshold(cv2.cvtColor(img, cv2.COLOR_BGR2GRAY),
    #                 127, 255, cv2.THRESH_BINARY)

    # let's try an hsv threshold
    frame_HSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    threshed_img = cv2.inRange(frame_HSV, (80, 10, 120), (125, 255, 225))

    # find contours and get the external one
    image, contours, hier = cv2.findContours(threshed_img, cv2.RETR_TREE,
                    cv2.CHAIN_APPROX_SIMPLE)
    
    cv2.imshow("literally an image", img)
    cv2.imshow("threshed image", threshed_img)



    # with each contour, draw boundingRect in green
    # a minAreaRect in red and
    # a minEnclosingCircle in blue

    imageToMarkup = img
    contoursMatched = 0
    for c in contours:
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

        # print "aspect ratio: %s extent: %s area: %s" % (aspect_ratio, extent, area)


        # if ( aspect_ratio > 1 ) & (aspect_ratio < 2) & ( extent < 0.3 ) & ( area > 25 ):
        if isWhithinRange(aspect_ratio, 1.4, 0.4) & isWhithinRange(extent, 0.2, 0.1) & ( area > 25 ):
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
    
    cv2.waitKey(0)

    cv2.destroyAllWindows()
        # frame = cv.imread(",1)
