def cal_wind_type(windSpeed):

    # WindSpeed need to be m/s unit and must be positive number

    # Default WindType
    windType = ""

    # Calculate the type of wind by this solution
    if (windSpeed < 2):
        windType = "A"
    elif (windSpeed >= 2 and windSpeed < 3):
        windType = "B"
    elif (windSpeed >= 3 and windSpeed < 5):
        windType = "C"
    elif (windSpeed >= 5 and windSpeed < 7):
        windType = "D"
    else:
        windType = "E"
    
    return (windType)