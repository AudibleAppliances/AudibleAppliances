#include <iostream>
#include <thread>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <atomic>
#include "semaphore.hpp"


void create_image() {
    std::cout << "Created image" << std::endl;
}

void writer(Semaphore &turn, Semaphore &write_guard) {
    while (true) {
        std::cout << "Writer thread : Acquired turn" << std::endl;
        turn.wait();
        write_guard.wait();
        create_image();
        write_guard.signal();
        turn.signal();
    }
}

void reader(int socket, std::atomic_int &active_readers, Semaphore &turn, Semaphore &waiting_readers, Semaphore &write_guard) {
    char buffer[1] = {0};
    char response[1] = {1};
        read(socket, buffer, 1);
        if (buffer[0] == 2) {
	    std::cout << "Reader thread: Acquiring turn" << std::endl;
	    turn.wait();
	    turn.signal();

	    std::cout << "Reader thread: Acquiring waiting_readers" << std::endl;
	    waiting_readers.wait();
	    if (std::atomic_load(&active_readers) == 0) {
	        write_guard.wait();
	    }
	    std::atomic_fetch_add(&active_readers, 1);
	    waiting_readers.signal();

	    std::cout << "Reader thread: Sending 1 to client socket" << std::endl;
	    
	    send(socket, response, 1, 0);
	    std::cout << "Reader thread: Wait to hear from client to read" << std::endl;
	    read(socket, buffer, 1);

	    std::cout << "Reader thread: Acquiring waiting_readers" << std::endl;
	    waiting_readers.wait();
	    std::atomic_fetch_sub(&active_readers, 1);
	    if (std::atomic_load(&active_readers) == 0) {
                write_guard.signal();
            }
	    waiting_readers.signal();
	}
}

int main() {
    Semaphore turn(1);
    Semaphore write_guard(1);
    Semaphore waiting_readers(1);
    std::atomic_int active_readers(0);
    
    struct sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = htonl(INADDR_ANY);
    address.sin_port = htons(40000);
    int address_length = sizeof(address);

    std::thread writer_thread (writer, std::ref(turn), std::ref(write_guard));
    int server_socket = socket(AF_INET, SOCK_STREAM, 0);
    bind(server_socket, (struct sockaddr *)&address, sizeof(address));
    listen(server_socket, 5);

    while (true) {
        int client_socket = accept(server_socket, (struct sockaddr *)&address, (socklen_t*)&address_length);
        std::cout << "New reader" << std::endl;
    	std::thread reader_thread (reader, client_socket, std::ref(active_readers), std::ref(turn), std::ref(waiting_readers), std::ref(write_guard));
    }
    writer_thread.join();
    return 0;
}
