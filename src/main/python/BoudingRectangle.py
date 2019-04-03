import cv2 as cv2
import numpy as np
from scipy.spatial import distance as dist
import math
# from enum import Enum
# import random 

try:
    from cv2 import cv2
except ImportError:
    pass

class GripPipeline:
    """
    An OpenCV pipeline generated by GRIP.
    """

    def __init__(self):
        """initializes all values to presets or None if need to be set
        """

        self.__hsv_threshold_hue = [15.9558030341169, 137.8198178573477]
        self.__hsv_threshold_saturation = [65.519019296701, 255.0]
        self.__hsv_threshold_value = [69.45015658363164, 255.0]

        self.hsv_threshold_output = None

        self.__find_contours_input = self.hsv_threshold_output
        self.__find_contours_external_only = False

        self.find_contours_output = None

        self.__filter_contours_contours = self.find_contours_output
        self.__filter_contours_min_area = 10.0
        self.__filter_contours_min_perimeter = 0.0
        self.__filter_contours_min_width = 0.0
        self.__filter_contours_max_width = 1000.0
        self.__filter_contours_min_height = 0.0
        self.__filter_contours_max_height = 1000.0
        self.__filter_contours_solidity = [0.0, 100]
        self.__filter_contours_max_vertices = 1000000.0
        self.__filter_contours_min_vertices = 0.0
        self.__filter_contours_min_ratio = 0.0
        self.__filter_contours_max_ratio = 20.0

        self.filter_contours_output = None
        self.__hsv_threshold_input = None



    def process(self, source0):
        """
        Runs the pipeline and sets all outputs to new values.
        """
        # Step HSV_Threshold0:
        self.__hsv_threshold_input = source0
        (self.hsv_threshold_output) = self.__hsv_threshold(self.__hsv_threshold_input, self.__hsv_threshold_hue,
                                                           self.__hsv_threshold_saturation, self.__hsv_threshold_value)

        # Step Find_Contours0:
        self.__find_contours_input = self.hsv_threshold_output
        (self.find_contours_output) = self.__find_contours(self.__find_contours_input,
                                                           self.__find_contours_external_only)

        # Step Filter_Contours0:
        self.__filter_contours_contours = self.find_contours_output
        (self.filter_contours_output) = self.__filter_contours(self.__filter_contours_contours,
                                                               self.__filter_contours_min_area,
                                                               self.__filter_contours_min_perimeter,
                                                               self.__filter_contours_min_width,
                                                               self.__filter_contours_max_width,
                                                               self.__filter_contours_min_height,
                                                               self.__filter_contours_max_height,
                                                               self.__filter_contours_solidity,
                                                               self.__filter_contours_max_vertices,
                                                               self.__filter_contours_min_vertices,
                                                               self.__filter_contours_min_ratio,
                                                               self.__filter_contours_max_ratio)

    @staticmethod
    def __hsv_threshold(input, hue, sat, val):
        """Segment an image based on hue, saturation, and value ranges.
        Args:
            input: A BGR numpy.ndarray.
            hue: A list of two numbers the are the min and max hue.
            sat: A list of two numbers the are the min and max saturation.
            lum: A list of two numbers the are the min and max value.
        Returns:
            A black and white numpy.ndarray.
        """
        out = cv2.cvtColor(input, cv2.COLOR_BGR2HSV)
        return cv2.inRange(out, (hue[0], sat[0], val[0]), (hue[1], sat[1], val[1]))

    @staticmethod
    def __find_contours(input, external_only):
        """Sets the values of pixels in a binary image to their distance to the nearest black pixel.
        Args:
            input: A numpy.ndarray.
            external_only: A boolean. If true only external contours are found.
        Return:
            A list of numpy.ndarray where each one represents a contour.
        """
        if (external_only):
            mode = cv2.RETR_EXTERNAL
        else:
            mode = cv2.RETR_LIST
        method = cv2.CHAIN_APPROX_SIMPLE
        contours, hierarchy = cv2.findContours(input, mode=mode, method=method)
        return contours

    @staticmethod
    def __filter_contours(input_contours, min_area, min_perimeter, min_width, max_width,
                          min_height, max_height, solidity, max_vertex_count, min_vertex_count,
                          min_ratio, max_ratio):
        """Filters out contours that do not meet certain criteria.
        Args:
            input_contours: Contours as a list of numpy.ndarray.
            min_area: The minimum area of a contour that will be kept.
            min_perimeter: The minimum perimeter of a contour that will be kept.
            min_width: Minimum width of a contour.
            max_width: MaxWidth maximum width.
            min_height: Minimum height.
            max_height: Maximimum height.
            solidity: The minimum and maximum solidity of a contour.
            min_vertex_count: Minimum vertex Count of the contours.
            max_vertex_count: Maximum vertex Count.
            min_ratio: Minimum ratio of width to height.
            max_ratio: Maximum ratio of width to height.
        Returns:
            Contours as a list of numpy.ndarray.
        """
        output = []
        for contour in input_contours:
            x, y, w, h = cv2.boundingRect(contour)
            if (w < min_width or w > max_width):
                continue
            if (h < min_height or h > max_height):
                continue
            area = cv2.contourArea(contour)
            if (area < min_area):
                continue
            if (cv2.arcLength(contour, True) < min_perimeter):
                continue
            hull = cv2.convexHull(contour)
            solid = 100 * area / cv2.contourArea(hull)
            if (solid < solidity[0] or solid > solidity[1]):
                continue
            if (len(contour) < min_vertex_count or len(contour) > max_vertex_count):
                continue
            ratio = (float)(w) / h
            if (ratio < min_ratio or ratio > max_ratio):
                continue
            output.append(contour)
        return output


class Box:
    def __init__(self, theBox):
        self.coordinates = theBox

        # self.shortSide = None
        # self.longSide = None
        # self.angle = None
        self.orderedPoints = None

        boxPoints = cv2.boxPoints(theBox)
        self.boxPoints = boxPoints

    def order_points(self, pts):
        # sort the points based on their x-coordinates
        xSorted = pts[np.argsort(pts[:, 0]), :]

        # grab the left-most and right-most points from the sorted
        # x-roodinate points
        leftMost = xSorted[:2, :]
        rightMost = xSorted[2:, :]

        # now, sort the left-most coordinates according to their
        # y-coordinates so we can grab the top-left and bottom-left
        # points, respectively
        leftMost = leftMost[np.argsort(leftMost[:, 1]), :]
        (tl, bl) = leftMost

        # now that we have the top-left coordinate, use it as an
        # anchor to calculate the Euclidean distance between the
        # top-left and right-most points; by the Pythagorean
        # theorem, the point with the largest distance will be
        # our bottom-right point
        D = dist.cdist(tl[np.newaxis], rightMost, "euclidean")[0]
        (br, tr) = rightMost[np.argsort(D)[::-1], :]

        # return the coordinates in top-left, top-right,
        # bottom-right, and bottom-left order

        self.orderedPoints = np.array([tl, tr, br, bl], dtype="float32")

        img = cv2.pyrDown(cv2.imread("/Users/matt/Documents/GitHub/pantry-vision/images/2019/CargoStraightDark48in.jpg",
                                     cv2.IMREAD_UNCHANGED))
        cv2.circle(img, (tl), 4, (255, 0, 0), -1)

        cv2.imshow(img)
        cv2.waitKey(0)
        cv2.destroyAllWindows()

        return self.orderedPoints



pipe = GripPipeline()

loadedImage = cv2.pyrDown(cv2.imread("/Users/matt/Documents/GitHub/pantry-vision/images/2019/CargoStraightDark48in.jpg",
                             cv2.IMREAD_UNCHANGED))

pipe.process(loadedImage)

cropped = loadedImage.copy()

# toAnnotate = pipe.hsv_threshold_output

cv2.imshow('before', pipe.hsv_threshold_output)

contours = pipe.filter_contours_output

contours = np.asarray(contours)

def getRect(contours_):

    toReturn = []

    for c in contours_:
        # get the bounding rect
        x, y, w, h = cv2.boundingRect(c)

        buffer = 7
        # x,y is top left of the box boi
        x -= int(buffer/2)
        y -= int(buffer/2)
        w += buffer
        h += buffer

        # draw a green rectangle to visualize the bounding rect
        # cv2.rectangle(img, (x, y), (x+w, y+h), (0, 255, 0), 1)

        toReturn.append([x, y, x+w, y+h])
    
    return toReturn

def crop(img_, range_):
    img_ = img_[range_[1]:range_[3], range_[0]:range_[2]]

    # print(img_)

    return img_

# a list of rectangles
rectangles = getRect(contours)

# a list of cropped images
visionTape = []

for rect in rectangles:
    newimg = crop(cropped, rect)
    visionTape.append(newimg)

visionTapeConers = []

# find the harris corners and subpixel corners
for iteration, tape in enumerate(visionTape):
    # find Harris corners
    gray = cv2.cvtColor(tape, cv2.COLOR_BGR2GRAY)
    gray = np.float32(gray)
    dst = cv2.cornerHarris(gray, 2, 3, 0.04)
    # dst = cv2.goodFeaturesToTrack(gray, 4, 0.05, 2.0, useHarrisDetector=True)
    dst = cv2.dilate(dst,None)
    ret, dst = cv2.threshold(dst,0.01*dst.max(),255,0)
    dst = np.uint8(dst)

    # find centroids
    ret, labels, stats, centroids = cv2.connectedComponentsWithStats(dst)

    # define the criteria to stop and refine the corners
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 100, 0.001)
    corners = cv2.cornerSubPix(gray,np.float32(centroids),(5,5),(-1,-1),criteria)


    corners = np.delete(corners, (0), axis=0)

    visionTapeConers.append(corners)

    # print(corners.shape)
    # print("centroids")
    # print(centroids)

    # Now draw them
    # res = np.hstack((corners))
    corners = np.int0(corners)
    # print(corners)
    tape[corners[:,1],corners[:,0]]=[0, 0, 255]
    tape[corners[:,1],corners[:,0]] = [0, 255, 0]

annotatedImage = loadedImage.copy()

# shift the corners over so that their coordinates are back in the global scope
for i, corner in enumerate(visionTapeConers):
    
    rect = rectangles[i]
    x = rect[0]
    y = rect[1]

    # cycle through all the corners and offset them
    for c in corner:
        c[0] += x
        c[1] += y

    
    print("CORNER: ")
    # print(corner)

    for point in corner:
        print(point)
        annotatedImage[int(point[1]), int(point[0])] = [0, 0, 255]

    


# to display all the images
for i, tape in enumerate(visionTape):
    cv2.namedWindow("tape %s" % i,cv2.WINDOW_GUI_EXPANDED)
    cv2.resizeWindow("tape %s" % i, 400,600)

    cv2.imshow("tape %s" % i, tape)








# print(boxes)

cv2.namedWindow('image',cv2.WINDOW_NORMAL)
cv2.imshow('image', annotatedImage)
cv2.resizeWindow('image', 800,600)

cv2.waitKey(0)

cv2.destroyAllWindows()



# import cv2
# import numpy as np
# import os.path
# import time
# from enum import Enum
#
# def isWhithinRange(value, expected, tolerence):
#     upperLimit = expected + tolerence
#     lowerLimit = expected - tolerence
#
#     if (value > lowerLimit) & (value < upperLimit):
#         return True
#     else:
#         return False
#
# def contourTracingShit(aspect_ratio, extent, area, imageToMarkup, c, x, y, w, h):
#     # get the min area rect
#     rect = cv2.minAreaRect(c)
#     box = cv2.boxPoints(rect)
#     # convert all coordinates floating point values to int
#     box = np.int0(box)
#
#     topmiddlepoint = findTheTopMiddleOfAbox(box)
#
#     # Draw a circle at the point to aim at - TODO POST THIS TO NETWORK TABLES
#     cv2.circle(imageToMarkup, (topmiddlepoint[0], topmiddlepoint[1]), 3, (255,100,255), thickness=6, lineType=8, shift=0)
#
#     # draw a red 'nghien' rectangle
#     cv2.drawContours(imageToMarkup, [box], 0, (0, 0, 255))
#
#     print("Top middle of the contour is at %s" % topmiddlepoint)
#
# def findTheTopMiddleOfAbox(box):
#     boxSorted = box[box[:,1].argsort()]
#     point1 = [boxSorted[0][0], boxSorted[0][1]]
#     point2 = [boxSorted[1][0], boxSorted[1][1]]
#     delta_points = [point2[0] - point1[0], point2[1]-point1[1]]
#     output_point = [0,0]
#     output_point[0] = point1[0] + delta_points[0]/2
#     output_point[1] = point1[1] + delta_points[1]/2
#     return output_point
#
# # class GripPipeline:
#     # """
#     # An OpenCV pipeline generated by GRIP.
#     # """
#
#     # def __init__():
#         # """initializes all values to presets or None if need to be set
#         # """
#
# __hsv_threshold_hue = [43.70503597122302, 86.79117147707979]
# __hsv_threshold_saturation = [139.88309352517985, 255.0]
# __hsv_threshold_value = [96.31294964028778, 255.0]
#
# hsv_threshold_output = None
#
# __find_contours_input = hsv_threshold_output
# __find_contours_external_only = False
#
# find_contours_output = None
#
#
# def process(source0):
#     # """
#     # Runs the pipeline and sets all outputs to new values.
#     # """
#     # Step HSV_Threshold0:
#     __hsv_threshold_input = source0
#     (hsv_threshold_output) = __hsv_threshold(__hsv_threshold_input, __hsv_threshold_hue, __hsv_threshold_saturation, __hsv_threshold_value)
#
#     # Step Find_Contours0:
#     __find_contours_input = hsv_threshold_output
#     (find_contours_output) = __find_contours(__find_contours_input, __find_contours_external_only)
#
#
# # @staticmethod
# def __hsv_threshold(input, hue, sat, val):
#     # """Segment an image based on hue, saturation, and value ranges.
#     # Args:
#     #     input: A BGR numpy.ndarray.
#     #     hue: A list of two numbers the are the min and max hue.
#     #     sat: A list of two numbers the are the min and max saturation.
#     #     lum: A list of two numbers the are the min and max value.
#     # Returns:
#     #     A black and white numpy.ndarray.
#     # """
#     out = cv2.cvtColor(input, cv2.COLOR_BGR2HSV)
#     return cv2.inRange(out, (hue[0], sat[0], val[0]),  (hue[1], sat[1], val[1]))
#
# # @staticmethod
# def __find_contours(input, external_only):
#     # """Sets the values of pixels in a binary image to their distance to the nearest black pixel.
#     # Args:
#     #     input: A numpy.ndarray.
#     #     external_only: A boolean. If true only external contours are found.
#     # Return:
#     #     A list of numpy.ndarray where each one represents a contour.
#     # """
#     if(external_only):
#         mode = cv2.RETR_EXTERNAL
#     else:
#         mode = cv2.RETR_LIST
#     method = cv2.CHAIN_APPROX_SIMPLE
#     contours = cv2.findContours(input, mode=mode, method=method)
#     return contours
#
#
# img = cv2.pyrDown(cv2.imread("/Users/matt/Documents/GitHub/pantry-vision/images/2019/LoadingStraightDark48in.jpg", cv2.IMREAD_UNCHANGED))
#
# threshedImg =  __hsv_threshold(img, __hsv_threshold_hue, __hsv_threshold_saturation, __hsv_threshold_value)
#
# # foundContours = __find_contours(threshedImg, __find_contours_external_only)
#
# contours =
#
# annotated = img.copy()
#
# cv2.drawContours(annotated, foundContours, -1, (0,255,0), 3)
#
#
# cv2.imshow("image", annotated)
#
# cv2.waitKey(0)
#
# cv2.destroyAllWindows()