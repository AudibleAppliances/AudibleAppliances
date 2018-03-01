from threading import Semaphore, Thread
import socket
import time
import picamera
import io
import cv2
assert cv2.__version__[0] == '3', 'The fisheye module needs opencv version 3'
import numpy as np

DIM=(720, 480)
K=np.array([[367.9031945760577, 0.0, 355.23742564196584],
            [0.0, 370.74439191610304, 254.26740477788147],
            [0.0, 0.0, 1.0]])
D=np.array([[-0.10733381367197396], [0.3374033541361976], [-0.4841315731348919], [0.23140318250430025]])

turn = Semaphore(value=1)
writeGuard = Semaphore(value = 1)
waitingReaders = Semaphore(value = 2)
activeReaders = 0
threads = []

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("localhost", 40000))
server.listen(5)

def create_image():
    stream = io.BytesIO()

    with picamera.PiCamera() as camera:
        time.sleep(0.1)
        camera.rotation = 180
        camera.awb_mode = 'fluorescent'
        camera.exposure_mode = 'backlight'
        camera.capture(stream, format='jpeg')

        data = np.fromstring(stream.getvalue(), dtype=np.uint8)
        image = cv2.imdecode(data, 1)

        undistorted_img = cv2.fisheye.undistortImage(image, K, D, Knew=K, new_size=DIM)
        cv2.imwrite('~/Downloads/test1234.jpg', image)


def writer():
    while True:
        time.sleep(0.5)
        turn.acquire()
        writeGuard.acquire()
        create_image()
        writeGuard.release()
        turn.release()

def reader(clientsocket):
    global activeReaders
    while True:
        data = clientsocket.recv(1)
        if data == '\x00':
            turn.acquire()
            turn.release()

            waitingReaders.acquire()
            if activeReaders == 0:
                writeGuard.acquire()
            activeReaders += 1
            waitingReaders.release()

            clientsocket.send('\x01')
            clientsocket.recv(1)

            waitingReaders.acquire()
            activeReaders -= 1
            if activeReaders == 0:
                writeGuard.release()
            waitingReaders.release()


t = Thread(target=writer)
t.start()
threads.append(t)

while True:
    clientsocket, addr = server.accept()
    read_thread = Thread(target=reader, args=(clientsocket,))
    read_thread.start()
    threads.append(read_thread)


