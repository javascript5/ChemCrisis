from gaussian.cal_sigma import cal_sigma
from gaussian.cal_position_map import cal_position_map
def gaussian(windDirection, distance, la, lg, Q, u, h):

    # You need to understand the "Gaussian plume function" before read this project
    # Source : http://courses.washington.edu/cewa567/Plumes.PDF
    
    # ------------ Variable Description ------------
    # Q = Mass emission rate
    # u = Wind speed evaluated at effective release height
    # h = Effective stack height, including rise of the hot plume near the source
    # windDirection = Direction of wind, examples N(Nort), E(East)

    position = cal_position_map(la, lg, windDirection, distance, Q, u, h)
    return position
# main(47.26369829, 1000, 13.729960, 100.778602, 40, 1.5, 20)