from gaussian.cal_gaussian_func import cal_gaussian_func
from gaussian.cal_sigma import cal_sigma
from gaussian.cal_wind_type import cal_wind_type
from gaussian.cal_downwind_crosswind import cal_downwind_crosswind
import json

def cal_position_map(la, lg, windDirection, distance, Q, u, h):
    windType = cal_wind_type(u)
    plotList = getPoint(distance)

    # Calculate standard gaussin plume model
    gaussian = getGaussian(plotList, Q, u, h, windType, windDirection)
    # Merge gaussian and plotList
    position = mergeGaussianAndPlot(la, lg, plotList, gaussian)

    return position

def mergeGaussianAndPlot(la, lg, plotList, gaussian):
        position = []
        for i in range(len(plotList)):
                buffer = la + (plotList[i][1]/111111), lg + (plotList[i][0]/111111), gaussian[i]
                position.append(buffer)
        return position

def getGaussian(plotList, Q, u, h, windType, windDirection):
        gassian = []
        for i in plotList:
                downwind = cal_downwind_crosswind(i[0], i[1], u, windDirection)[0]
                crosswind = cal_downwind_crosswind(i[0], i[1], u, windDirection)[1]
                sigma_z = cal_sigma(windType, downwind)[0]
                sigma_y = cal_sigma(windType, downwind)[1]
                if downwind > 0:
                        buffer = int(cal_gaussian_func(downwind, crosswind, Q, u, sigma_z, sigma_y, h)*(10**5))
                else:
                        buffer = 0
                gassian.append(buffer)
        return gassian

def getPoint(distance):
        plotList = []
        for i in range (distance * -1, distance + 1, 50):
                for j in range (distance * -1, distance + 1, 50):
                        if not (i == 0 and j == 0):
                                plotList.append((j, i))
        return plotList