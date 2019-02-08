import numpy as np

def cal_sigma(windType, distance):

    # WindType calculate from cal_wind_type.py
    distance = np.abs(distance/1000)
    # Distance is a interested distance in km(kilometer) unit and must be have less than 2 decimal

    # a, b, c, d variable is a variable in "Pasquill Stability Category" 
    # use to calculate Sigma(z), Sigma(y)
    a = 0
    b = 0
    c = 0
    d = 0

    # Class A
    if (windType == "A"):
        c = 24.1670
        d = 2.5334
        if (distance < 0.10):
            a = 122.800
            b = 0.94470
        elif (0.12 <= distance <= 0.15):
            a = 158.080
            b = 1.05420
        elif (0.16 <= distance <= 0.20):
            a = 170.220
            b = 1.09320
        elif (0.21 <= distance <= 0.25):
            a = 179.520
            b = 1.12620
        elif (0.26 <= distance <= 0.30):
            a = 217.410
            b = 1.26440
        elif (0.31 <= distance <= 0.40):
            a = 258.890
            b = 1.40940
        elif (0.40 <= distance <= 0.50):
            a = 346.750
            b = 1.72830
        else:
            a = 453.850
            b = 2.11660

    # Class B 
    elif (windType == "B"):
        c = 18.3330
        d = 1.8096
        if (distance < 0.20):
            a = 90.673
            b = 0.93198
        elif (0.21 <= distance <= 0.40):
            a = 98.483
            b = 0.98332
        else:
            a = 109.300
            b = 1.09710

    # Class C
    elif (windType == "C"):
        a = 61.141
        b = 0.91465
        c = 12.5000
        d = 1.0857
    
    # Class D
    elif (windType == "D"):
        c = 8.3330
        d = 0.72382
        if (distance < 0.30):
            a = 34.459
            b = 0.86974
        elif (0.31 <= distance <= 1.0):
            a = 32.093
            b = 0.81066
        elif (1.01 <= distance <= 3.00):
            a = 32.093
            b = 0.64403
        elif (3.01 <= distance <= 10.00):
            a = 33.504
            b = 0.60486
        elif (10.01 <= distance <= 30.00):
            a = 36.650
            b = 0.56589
        else:
            a = 44.053
            b = 0.51179

    # Class E
    elif (windType == "E"):
        c = 6.2500
        d = 0.54287
        if (distance < 0.10):
            a = 24.260
            b = 0.83660
        elif (0.10 <= distance <= 0.30):
            a = 23.331
            b = 0.81956
        elif (0.31 <= distance <= 1.00):
            a = 21.628
            b = 0.75660
        elif (1.01 <= distance <= 2.00):
            a = 21.628
            b = 0.63077
        elif (2.01 <= distance <= 4.00):
            a = 22.534
            b = 0.57154
        elif (4.01 <= distance <= 10.00):
            a = 24.703
            b = 0.50527
        elif (10.01 <= distance <= 20.00):
            a = 26.970
            b = 0.46713
        elif (20.01 <= distance <= 40.00):
            a = 35.420
            b = 0.37615
        else:
            a = 47.618
            b = 0.29592

    # Now Calculate Sigma from a, b, c, d

    # Sigma(z) = a(distance^b)
    sigma_z = a * (distance**b)

    # Theta = 0.017453293(c - d ln(distance))
    theta = 0.017453293*(c - d*np.log(np.abs(distance+1e-15)))

    # Sigma(y) = 465.11628x(tan Q)
    sigma_y = 465.11628 * distance * np.tan(theta)
    
    return (sigma_z, sigma_y)