import cv2
import socket

class Image_Client(object):
    def __init__(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect(("localhost", 40000))
    
    def get_image(self):
        self.socket.send('\x02')
        if self.socket.recv(1) == '\x01':
            image = cv2.imread("/mnt/rd/image.jpg")
            ret, jpeg = cv2.imencode('.jpg', image)
            return jpeg.tobytes()
        else:
            raise IOError("Incorrect response from image server")
