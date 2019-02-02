import numpy as np

def cal_gaussian_func(x, y, Q, u, sigma_z, sigma_y, h):
    # In ground-level concerntration it use only x,y direction
    CONST_Z = 0

    # split the "Gaussian Calculation" to 4 variable for easy to read
    solution_1 = Q/(2 * np.pi * u * sigma_y * sigma_z)
    exp_1 = np.exp(((-1) * (CONST_Z - h)**2) / (2 * (sigma_z**2)))
    exp_2 = np.exp(((-1) * (CONST_Z + h)**2) / (2 * (sigma_z**2)))
    exp_3 = np.exp((-1) * (y**2) / (2 * (sigma_y**2)))

    result = solution_1 * (exp_1 + exp_2) * exp_3

    return result