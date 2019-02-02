import numpy as np
def cal_downwind_crosswind(x, y, u, windDirection):
    # wx = u * sin(angle - 180) * pi/180
    wx = u * np.sin((windDirection - 180) * np.pi/180)
    wy = u * np.cos((windDirection - 180) * np.pi/180)

    # dot_product = (wx * x) + (wy * y)
    # Magnitude = u * SQRT(x^2 * y^2)
    # Subtended = ACOS(dot_product/(magnitude + (exp(-15))))
    # Hypotenuse = SQRT(x^2 + y^2)

    dot_product = (wx * x) + (wy * y)
    magnitude = u * np.sqrt(x**2 + y**2)
    subtended = np.arccos(dot_product/(magnitude + (np.exp(-15))))
    hypotenuse = np.sqrt(x**2 + y**2)

    # Downwind = COS(subtended) * hypotenuse
    # Crosswind = SIN(subtended) * hypotenuse

    downwind = np.cos(subtended) * hypotenuse
    crosswind = np.sin(subtended) * hypotenuse

    return (downwind, crosswind)