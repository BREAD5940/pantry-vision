import cv2
import numpy as np 
import os.path
import time

current_milli_time = lambda: int(round(time.time() * 1000))



 
def isWhithinRange(value, expected, tolerence):
    upperLimit = expected + tolerence
    lowerLimit = expected - tolerence

    if (value > lowerLimit) & (value < upperLimit):
        return True
    else:
        return False

def contourTracingShit(aspect_ratio, extent, area, imageToMarkup, c, x, y, w, h):
    # get the min area rect
    rect = cv2.minAreaRect(c)
    box = cv2.boxPoints(rect)
    # convert all coordinates floating point values to int
    box = np.int0(box)

    topmiddlepoint = findTheTopMiddleOfAbox(box)

    # Draw a circle at the point to aim at - TODO POST THIS TO NETWORK TABLES
    cv2.circle(imageToMarkup, (topmiddlepoint[0], topmiddlepoint[1]), 3, (255,100,255), thickness=6, lineType=8, shift=0)

    # draw a red 'nghien' rectangle
    cv2.drawContours(imageToMarkup, [box], 0, (0, 0, 255))

    print "Top middle of the contour is at %s" % topmiddlepoint

def findTheTopMiddleOfAbox(box):
    boxSorted = box[box[:,1].argsort()]
    point1 = [boxSorted[0][0], boxSorted[0][1]]
    point2 = [boxSorted[1][0], boxSorted[1][1]]
    delta_points = [point2[0] - point1[0], point2[1]-point1[1]]
    output_point = [0,0]
    output_point[0] = point1[0] + delta_points[0]/2
    output_point[1] = point1[1] + delta_points[1]/2
    return output_point


iteration = 0

testingMode = False

# framesToCheck = [202,245, 254, 255, 256, 257, 258, 259, 260, 266, 267, 268, 269, 277, 422, 423, 424, 425, 426, 427, 428, 429, 430, 435, 440, 445, 448, 478, 489, 490, 491, 492, 493]
# print len(framesToCheck)

while iteration < 542:
# for x in range(len(framesToCheck)):

    startTime = int((time.time() * 1000))

    # imgpath = '2016-vision-master\\imgproc\\RealFullField\\%s.jpg' % framesToCheck[x]
    imgpath = '2016-vision-master\\imgproc\\RealFullField\\%s.jpg' % iteration

    # iteration = framesToCheck[x]
    iteration += 1

    if os.path.exists(imgpath):

        img = cv2.pyrDown(cv2.imread(imgpath, cv2.IMREAD_UNCHANGED))
        print "iteration: %s" % iteration

        # let's try an hsv threshold
        frame_HSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        threshed_img = cv2.inRange(frame_HSV, (80, 10, 120), (125, 255, 225))

        # Constants
        kernel = np.ones((2,2),np.uint8)
        erosion = cv2.erode(img,kernel,iterations = 1)

        dilated_image = cv2.dilate(threshed_img,kernel,iterations = 1)
        if testingMode is True:
            cv2.imshow("dilation ", dilated_image)

        # find contours and get the external one
        image, contours, hier = cv2.findContours(dilated_image, cv2.RETR_TREE,
                        cv2.CHAIN_APPROX_SIMPLE)
        if testingMode is True:
            cv2.imshow("literally an image, loop %s" % iteration, img)
            cv2.imshow("threshed image, loop %s" % iteration, threshed_img)

        imageToMarkup = img
        contoursMatched = 0
        loop = 0
        for c in contours:
            loop += 1
            # get the bounding rect
            x, y, w, h = cv2.boundingRect(c)
        
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
                
            # 

            matched_contour_stats = [0, 0, 0, 0, 0, 0, 0]

            # if (area > 100):
                # print "contour number %s aspect ratio: %s extent: %s area: %s" % (loop, aspect_ratio, extent, area)
            
            if ( aspect_ratio > 0.9 ) & (aspect_ratio < 3) & ( extent > 0.12 ) & ( extent < 0.35 )  & ( area > 160 ):
                contoursMatched += 1

                # Do everything in one complex condition
                if ((contoursMatched > 1) & (area > matched_contour_stats[2])) or (contoursMatched <= 1):
                    # 
                    matched_contour_stats = [aspect_ratio, extent, area, x, y, w, h]
                    matched_contour = c

        contourTracingShit(aspect_ratio, extent, area, imageToMarkup, matched_contour, matched_contour_stats[3], matched_contour_stats[4], matched_contour_stats[5], matched_contour_stats[6])

        # print"%s,%s,%s" % (aspect_ratio, extent, area)

        cv2.imshow("contours", imageToMarkup)

        if testingMode is True:
            cv2.imshow("threshed image plus markup", threshed_img)

        endTime = startTime = int((time.time() * 1000))
        delta_time = endTime - startTime
        print("Delta time: %s" % delta_time)

        cv2.waitKey(0)

        cv2.destroyAllWindows()