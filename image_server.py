from threading import Semaphore, Thread
import socket
import time
import math
import picamera
import io
import cv2
assert cv2.__version__[0] == '3', 'The fisheye module needs opencv version 3'
import numpy as np

resolution = (720, 1280)
strength = 2.5
zoom = 1.1
half_width = resolution[0]/2
half_height = resolution[1]/2
if strength == 0:
    strength = 0.00001

correction_radius = math.sqrt(resolution[0] ** 2 + resolution[1] ** 2) / strength
mapping_x = np.zeros((resolution[0],resolution[1]), dtype=np.float32)
mapping_y = np.zeros((resolution[0],resolution[1]), dtype=np.float32)
new_image = np.zeros((resolution[0],resolution[1], 3), dtype=int)

turn = Semaphore(value=1)
writeGuard = Semaphore(value = 1)
waitingReaders = Semaphore(value = 2)
activeReaders = 0
threads = []

for x in range(resolution[0]):
    for y in range(resolution[1]):
        new_x = x - half_width
        new_y = y - half_height
        
        distance = math.sqrt(new_x ** 2 + new_y ** 2)
        r = distance / correction_radius
        
        if r == 0:
            theta = 1
        else:
            theta = math.atan(r) / r
        
        source_x = int(half_width + theta * new_x * zoom)
        source_y = int(half_height + theta * new_y * zoom)
        if 0 <= source_x < resolution[0] and 0 <= source_y < resolution[1]:
            mapping_x[x][y] = source_x
            mapping_y[x][y] = source_y

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("localhost", 40000))
server.listen(5)

def create_image():
    print "Created image"
    stream = io.BytesIO()

    with picamera.PiCamera() as camera:
        camera.rotation = 180
        camera.awb_mode = 'fluorescent'
        camera.resolution = (1280, 720)
        camera.exposure_mode = 'backlight'
        camera.capture(stream, format='jpeg')
        time.sleep(0.5)

        data = np.fromstring(stream.getvalue(), dtype=np.uint8)
        image = cv2.imdecode(data, 1)

        undistorted_img = new_image = cv2.remap(image, mapping_y, mapping_x, cv2.INTER_LINEAR)
        cv2.imwrite('/mnt/rd/image.jpg', undistorted_img)


def writer():
    while True:
        turn.acquire()
        writeGuard.acquire()
        create_image()
        writeGuard.release()
        turn.release()

def reader(clientsocket):
    global activeReaders
    while True:
        data = clientsocket.recv(1)
        if data == '\x02':
            print "reader thread: acquiring turn!"
            turn.acquire()
            turn.release()

            print "reader thread: acquiring waitingReaders!"
            waitingReaders.acquire()
            if activeReaders == 0:
                writeGuard.acquire()
            activeReaders += 1
            waitingReaders.release()

            print "reader thread: sending 2 to client socket"
            clientsocket.send('\x01')
            print "reader thread: recv from client socket"
            clientsocket.recv(1)

            print "reader thread: acquiring waitingReaders!"
            waitingReaders.acquire()
            activeReaders -= 1
            if activeReaders == 0:
                writeGuard.release()
            waitingReaders.release()


t = Thread(target=writer)
t.daemon = True
t.start()
threads.append(t)

while True:
    clientsocket, addr = server.accept() 
    print "New reader"
    read_thread = Thread(target=reader, args=(clientsocket,))
    read_thread.daemon = True
    read_thread.start()
    threads.append(read_thread)

        
