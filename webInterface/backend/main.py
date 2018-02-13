# main.py
from flask import Flask, render_template, Response, request
from camera import VideoCamera
import json

app = Flask(__name__)

def gen(camera):
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

@app.route('/')
def index():
    return render_template("div_test.html")

@app.route('/myStyle.css')
def style():
    return render_template("myStyle.css")

@app.route('/functions.js')
def javascript():
    return render_template("functions.js")

@app.route('/video_feed')
def video_feed():
    return Response(gen(VideoCamera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

@app.route('/new_box', methods=['POST'])
def new_box():
    content = request.get_json(silent=False, force=True)
    with open("boxes.json", 'r') as fp:
        content_type = content.pop("type", None)
        config = json.load(fp)
        config["boxes"][content_type] = content
    with open("boxes.json", 'w') as fp:
        json.dump(config, fp)
    return ("Box successfully created", 200, "")


@app.route('/get_boxes')
def get_boxes():
    with open("boxes.json", 'r') as fp:
        return json.dumps({"boxes" : json.load(fp)["boxes"]})


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True, threaded=True)
