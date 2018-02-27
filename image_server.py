import socket
import time

request_queue = []
number_of_active_requests = 0

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("localhost", 40000))
server.listen(5)

def create_image():
    print "Image created"

def need_to_write():
    return last_time_read + 0.5 < time.time()

create_image()
last_time_read = time.time()


while True:
    if need_to_write and number_of_active_requests == 0:
        create_image()
        last_time_read = time.time()
        for s in request_queue:
            bytes = bytearray()
            bytes.append(1)
            s.send(bytes)
            number_of_active_requests += 1
        request_queue = []
    
    
    clientsocket, addr = server.accept()
    data = clientsocket.recv(1)
    if data == 1:
        if need_to_write:
            request_queue.append(clientsocket)
        else:
            number_of_active_requests += 1
            bytes = bytearray()
            bytes.append(1)
            clientsocket.send(bytes)
    elif data == 0:
        number_of_active_requests -= 1


    clientsocket.close()
