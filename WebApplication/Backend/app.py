from flask import Flask, render_template, request, redirect
import hashlib, json, requests
from time import gmtime, strftime
from gaussian.main import gaussian
from urllib.request import urlopen
from firebase import firebase
from pyfcm import FCMNotification
import datetime  , time
firebase = firebase.FirebaseApplication('https://nsc-chemcrisis-6d6a2.firebaseio.com/', None)

app = Flask(__name__)

@app.route("/")
def login():
    return render_template('login.html')
@app.route("/index")
def index():
    accidents = ""
    factorys = ""
    if firebase.get("/accident", None) is not None and firebase.get("/factory", None) is not None:
        accidents = firebase.get('/accident' ,None)
        factorys = firebase.get('/factory' ,None)
    return render_template('index.html', page='index' , accidents=accidents, factorys=factorys)

@app.route("/factory")
def factory():
    factorys = ""
    chemicals = ""
    if firebase.get('/factory' ,None) is not None:
        factorys = firebase.get('/factory' ,None)
    if firebase.get('/chemical' ,None) is not None:
        chemicals = firebase.get('/chemical' ,None)
    date = time.localtime()
    a = time.strftime('%d/%m/%Y/ - %H:%M:%S', date)
    return render_template('factory.html', page='factory' ,factorys=factorys, currentDateTime=a, chemicals=chemicals)
    
@app.route("/deleteFactory", methods=['GET', 'POST'])
def deleteFactory():
    req = request.form
    factoryId = req["factoryId"]
    firebase.delete('/factory', factoryId)
    return redirect("/factory")
    
@app.route("/chemical")
def chemical():
    chemicals = ""
    if firebase.get("/chemical", None) is not None:
        chemicals = firebase.get('/chemical' ,None)
    return render_template('chemical.html', page='chemical' ,chemicals=chemicals)


@app.route("/deleteChemical", methods=['GET', 'POST'])
def deleteChemical():
    if request.method == 'POST':
        req = request.form
        chemicalId = req['chemicalId']
        firebase.delete('/chemical', chemicalId)
    return redirect("/chemical")

@app.route("/addFactoryLocation", methods=['GET', 'POST'])
def addFactoryLocation():
    if request.method == 'POST':
        req = request.form
        locationName = req["locationName"]
        data = {'address':req["address"],'latitude': float(req["latitude"]),'longitude': float(req["longitude"])}
        if firebase.get("/factory/", locationName) is None:
            firebase.put('/factory/',locationName, data)
    return redirect('/factory')

@app.route("/addAccident", methods=['GET','POST'])
def addAccident():
    if request.method == 'POST':
        req = request.form
        factory = req["factory"]
        latitude = req["latitude"]
        latitude = float(latitude)
        longitude= req["longitude"]
        longitude = float(longitude)
        dateTime= req["dateTime"]
        massEmissionRate = req["massEmissionRate"]
        massEmissionRate = float(massEmissionRate)
        effectiveStackHeight = req["effectiveStackHeight"]
        effectiveStackHeight = float(effectiveStackHeight)
        accidentChemical = req["accidentChemical"][:-2]
        wind_data = load_weather(latitude, longitude)
        windDeg = wind_data['deg']
        windSpeed = wind_data['speed']
        dataSet =  gaussian(windDeg, 1000, latitude, longitude, massEmissionRate, windSpeed, effectiveStackHeight)
        data = {'dateTime':dateTime,'massEmissionRate':massEmissionRate,'effectiveStackHeight':effectiveStackHeight,'accidentPosition':dataSet,'accidentChemical':accidentChemical}
        history = {'factoryId':factory,'dateTime':dateTime,'massEmissionRate':massEmissionRate,'effectiveStackHeight':effectiveStackHeight,'accidentChemical':accidentChemical}
        firebase.put('/accident/',factory, data)
        firebase.post('/history/',history)
        sendNotification(factory)
        
    return redirect('/index')

def sendNotification(factory ):
    push_service = FCMNotification(api_key="AIzaSyDXMxfSwPddFDdyeaf0OVyvZEog5ThuFRI")
    data = {
    "content" : factory
    }
    title = "ท่านอยู่ในจุดเกิดเหตุการรั่วไหลของสารเคมี"
    message = ("กรุณาออกจากจุดเกิดเหตุโดยด่วน เปิดเพื่อให้พวกเรานำทางให้สิ (จุดเกิดเหตุ โรงงาน %s)" , factory)
    result = push_service.notify_topic_subscribers(topic_name="NEWS",message_title=title, message_body=message,data_message=data)
    print(result)

def load_weather(lat, lon):
        api_url = "http://api.openweathermap.org/data/2.5/weather"
        app_id = "2bf0eabb1c8e45b42cad96ac6037afa6"
        request = requests.get(url=api_url, params=dict(APPID=app_id, lat=lat, lon=lon ))
        wind_data = json.loads(request.content)
        return wind_data['wind']

@app.route("/removeAccident", methods=['GET','POST'])
def removeAccident():
    if request.method == 'POST':
        req = request.form
        accidentId = req['accidentId']
        firebase.delete('/accident', accidentId)
    return redirect('/index')

@app.route("/addChemical", methods=['GET', 'POST'])
def addChemical():
    if request.method == 'POST':
        req = request.form
        chemicalName = req["chemicalName"]
        chemicalThaiName = req["chemicalThaiName"]
        molecularWeight = req["molecularWeight"]
        violenceOfChemical = req["violenceOfChemical"]
        effectsOfChemical = req["effectsOfChemical"]
        data = {'chemicalName':chemicalName,'chemicalThaiName':chemicalThaiName,'molecularWeight':molecularWeight,'violenceOfChemical':violenceOfChemical,'effectsOfChemical':effectsOfChemical}
        if firebase.get("/chemical/", chemicalName) is None:
            firebase.put('/chemical/',chemicalName, data)
    return redirect("/chemical")

if __name__ == "__main__":
    app.run(debug=True)
