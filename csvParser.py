import matplotlib as mpl
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.cbook as cbook
from numpy import arange, sin, pi
import math

# with open('scope_9.csv', 'rb') as csvfile:
#     reader = csv.reader(csvfile, delimiter=',')
#     for row in reader:
#         print ', '.join(row)

fig = plt.figure()


ax = fig.add_subplot(111)

import numpy as np
data = np.genfromtxt('scope_9.csv', delimiter=',', skip_header=1,
                     skip_footer=0, names=['second', 'Ampere'])


# Find all the places where the sign changes, and put them in a list
SignChangeTimeStamps = []
for i, v in enumerate(data['Ampere']):
    if i == 0:
        change = False
    elif v < 0 and data['Ampere'][i-1] > 0:
        change = True
        # print "true"
        SignChangeTimeStamps.append(data['second'][i-1])
    elif v > 0 and data['Ampere'][i-1] < 0:
        change = True
        # print "true"
        SignChangeTimeStamps.append(data['second'][i-1])
    else:
        change = False


# We assume that there are 3 zero crossings in the data set, and use this to determine the cycle time by averaging the delta and multiplying it by 2
# Shift it over by the first zero crossing times negative one, so that the first zero crossing is at x=0. Also record min/max values
timeToShiftBy = SignChangeTimeStamps[0] * -1
i = 0
min = 0
max = 0
for x in np.nditer(data):
    dautum = data['second'][i]
    dautum += timeToShiftBy
    data['second'][i] = dautum
    if data['Ampere'][i] > max:
        max = data['Ampere'][i]
    if data['Ampere'][i] < min:
        min = data['Ampere'][i]
    i += 1

# Also shift the sign change time stamps
i=0
for indecies in SignChangeTimeStamps:
    i += 1
    SignChangeTimeStamps[i-1] += timeToShiftBy


# Derive the average frequency, magnitude and initial slope (and make the slope negative if necessary)
averageFrequecy = ((SignChangeTimeStamps[2] - SignChangeTimeStamps[1]) + (SignChangeTimeStamps[1] - SignChangeTimeStamps[0]) )
averageMagnitude = (abs(min) + abs(max))/2
initialSlope = data['Ampere'][15] - data['Ampere'][5]
# print initialSlope
if initialSlope < 0:
    averageMagnitude = averageMagnitude * -1


# Try to make a array with the optimal sine wave in it
optimalSineWave = []
i = 0
for x in range(np.size(data)):
    # print x
#     # optimalSineWave[0] = optimalSineWave['second'][i-1]
    # optimalSineWave.append( (averageMagnitude * sin(2 * 3.1415926 * data['second'][x] ) / averageFrequecy) )
    # print data['second'][x]
    print (averageMagnitude * sin(2 * 3.1415926 * data['second'][x] ) / averageFrequecy)



# plot the stuff
ax.plot(data['second'], optimalSineWave, linewidth=3, color='c')
ax.plot(data['second'], data['Ampere'], color='k', label='the data')
ax.axhline(linewidth=1, color='k')
i=0
for time in SignChangeTimeStamps:
    i+=1
    ax.axvline(x=SignChangeTimeStamps[i-1], color='g')


ax.set_title("Current vs time")
ax.set_xlabel("Time in seconds")
ax.set_ylabel("Current in amperes")


plt.show()