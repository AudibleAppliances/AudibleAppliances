#include <iostream>
#include <thread>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include "semaphore.hpp"


void create_image() {
    std::cout << "Created image" << std::endl;
}

void writer(Semaphore *turn, Semaphore *write_guard) {
    while (true) {
        std::cout << "Writer thread : Acquired turn" << std::endl;
        turn->wait();
        write_guard->wait();
        create_image();
        write_guard->signal();
        turn->signal();
    }
}

void reader(int socket, int &active_readers) {
    char buffer[256] = {0};
    
    while (true) {
        std::fread(buffer, 256, 2, (FILE*)socket);
        std::cout << buffer << std::endl;
//        if (buffer == 0)
    }
}

int main() {
    Semaphore turn(1);
    Semaphore write_guard(1);
    Semaphore waiting_readers(1);
    int active_readers = 0;
    
    struct sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = htonl(INADDR_ANY);
    address.sin_port = htons(40000);

    std::thread writer_thread (writer, turn, write_guard);
    int server_socket = socket(AF_INET, AF_INET, 0);
    bind(server_socket, (struct sockaddr *)&address, sizeof(address));
    listen(server_socket, 5);
    
    while (true) {
        int client_socket = accept(server_socket, (struct sockaddr *)&address, sizeof(address));
        std::cout << "New reader" << std::endl;
        std::thread reader_thread (reader, client_socket, active_readers);
    }
    writer_thread.join();
    return 0;
}
