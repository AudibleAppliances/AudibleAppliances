# main.py
from flask import Flask, render_template, Response, request
from camera import VideoCamera
import json

config_file = "/home/pi/config.json"
app = Flask(__name__)

def gen(camera):
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

@app.route('/')
def index():
    return render_template("index.html")

@app.route('/select_components.html')
def select_components():
    return render_template("select_components.html")

@app.route('/myStyle.css')
def style():
    return render_template("myStyle.css")

@app.route('/functions.js')
def javascript1():
    return render_template("functions.js")

@app.route('/ui_functions.js')
def javascript2():
    return render_template("ui_functions.js")

@app.route('/index_functions.js')
def javascript3():
    return render_template("index_functions.js")

@app.route('/video_feed')
def video_feed():
    return Response(gen(VideoCamera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

@app.route('/new_box', methods=['POST'])
def new_box():
    content = request.get_json(silent=False, force=True)
    with open(config_file, 'r') as fp:
        content_box = content.pop("box", None)
        content_type = content_box.pop("type", None)
        spoken = content.pop("spoken", None)
        config = json.load(fp)
        config["boxes"][content_type] = content_box
        config["spoken_fields"][content_type] = spoken
    with open(config_file, 'w') as fp:
        json.dump(config, fp)
    return ("Box successfully created", 200, "")

@app.route('/set_voice', methods=['POST'])
def set_voice():
    content = request.get_json(silent=False, force=True)
    with open(config_file, 'r') as fp:
        content_type = content.pop("voice", None)
        config = json.load(fp)
        config["voice"] = content_type
    with open(config_file, 'w') as fp:
        json.dump(config, fp)
    return ("Voice successfully set", 200, "")

@app.route('/get_voice', methods=['GET'])
def get_voice():
    with open(config_file, 'r') as fp:
        config = json.load(fp)
        return json.dumps({"voice" : config["voice"]})

@app.route('/set_frequency', methods=['POST'])
def set_frequency():
    content = request.get_json(silent=False, force=True)
    with open(config_file, 'r') as fp:
        content_type = content.pop("frequency", None)
        config = json.load(fp)
        config["frequency"] = content_type
    with open(config_file, 'w') as fp:
        json.dump(config, fp)
    return ("frequency successfully set", 200, "")

@app.route('/get_frequency', methods=['GET'])
def get_frequency():
    with open(config_file, 'r') as fp:
        config = json.load(fp)
        return json.dumps({"frequency" : config["frequency"]})

@app.route('/get_boxes')
def get_boxes():
    with open(config_file, 'r') as fp:
        return json.dumps({"boxes" : json.load(fp)["boxes"]})


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True, threaded=True)
