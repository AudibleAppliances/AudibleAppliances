import cv2
import socket

class ImageClient(object):
    def __init__(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect(("localhost", 40000))

    def get_frame(self):
        self.socket.send('\x02')
        if self.socket.recv(1) == 1:
            image = cv2.imread("/mnt/rd/image.jpg")
            ret, jpeg = cv2.imencode('.jpg', image)
            return jpeg.tobytes()
        else:
            raise IOError("Incorrect response from image server")


