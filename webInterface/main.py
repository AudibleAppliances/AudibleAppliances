# main.py
from flask import Flask, render_template, Response, request, stream_with_context
from image_client import Image_Client
import json

config_file = "/home/pi/config.json"
app = Flask(__name__)
my_image_client = Image_Client()


def gen(image_client):
    while True:
        frame = image_client.get_image()
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
    r =  Response(stream_with_context(gen(my_image_client)),
                  mimetype='multipart/x-mixed-replace; boundary=frame')
    return r

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
        if content_type == "lcd_text_10" or content_type == "lcd_text_11":
            config["boxes"]["program"] = {
                left_percentage: config["boxes"]["text_10"]["left_percentage"],
                top_percentage: min(config["boxes"]["text_10"]["top_percentage"], config["boxes"]["text_11"]["top_percentage"]),
                width: config["boxes"]["text_10"]["width"] + config["boxes"]["text_11"]["width"],
                height: max(config["boxes"]["text_10"]["height"], config["boxes"]["text_11"]["height"])
            }
    with open(config_file, 'w') as fp:
        json.dump(config, fp)
    return ("Box successfully created", 200, "")

@app.route('/set_voice', methods=['POST'])
def set_voice():
    content = request.get_json(silent=False, force=True)
    with open(config_file, 'r') as fp:
        content_type = content.pop("voice", None)
        config = json.load(fp)
        if content_type == "male_american":
            config["voice"] = "kal_diphone"
        elif content_type == "female_american":
            config["voice"] = "us1_mbrola"
        elif content_type == "scottish":
            config["voice"] = "cmu_us_awb_arctic_clunits"

    with open(config_file, 'w') as fp:
        json.dump(config, fp)
    return ("Voice successfully set", 200, "")

@app.route('/get_voice', methods=['GET'])
def get_voice():
    with open(config_file, 'r') as fp:
        config = json.load(fp)
        voice = config["voice"]
        if voice == "kal_diphone":
            nice_description = "male_american"
        elif voice == "us1_mbrola":
            nice_description = "female_american"
        else:
            nice_description = "scottish"
        
        return json.dumps({"voice" : nice_description})

@app.route('/get_boxes')
def get_boxes():
    with open(config_file, 'r') as fp:
        return json.dumps({"boxes" : json.load(fp)["boxes"]})


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True, threaded=True)
