#include <math.h>
#include <opencv2/opencv.hpp>
#include <ctime>
#include <raspicam/raspicam_cv.h>
#include <iostream>
#include <thread>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <atomic>
#include "semaphore.hpp"
#include "mappings.hpp"

void create_image(Mapping &mapping, raspicam::RaspiCam_Cv &camera);
void writer(Semaphore &turn, Semaphore &write_guard, Mapping &mapping,
	    raspicam::RaspiCam_Cv &camera);
void reader(int socket, std::atomic_int &active_readers, Semaphore &turn,
            Semaphore &waiting_readers, Semaphore &write_guard);



int main(int argc, char *argv[]) {
    std::cout << "Creating concurrecny control variables" << std::endl;
    Semaphore turn(1);
    Semaphore write_guard(1);
    Semaphore waiting_readers(1);
    std::atomic_int active_readers(0);
    
    std::cout << "Creating and initialising mapping object" << std::endl;
    Mapping mapping(1.2, 2.5, true);
    
    std::cout << "Initialising Raspberry Pi camera" << std::endl;
    raspicam::RaspiCam_Cv camera;
    camera.set(CV_CAP_PROP_FORMAT, CV_8UC3);
    camera.set(CV_CAP_PROP_FRAME_WIDTH, mapping.resolution_x);
    camera.set(CV_CAP_PROP_FRAME_HEIGHT, mapping.resolution_y);
    camera.set(CV_CAP_PROP_EXPOSURE, 30);
    camera.set(CV_CAP_PROP_MODE, 2);
    
    if (!camera.open()) {
	    std::cerr << "Failed to open the camera" << std::endl;
        return 1;
    }
    
    std::cout << "Initialising server sockets" << std::endl;
    struct sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = inet_addr("127.0.0.1");
    address.sin_port = htons(40000);
    int address_length = sizeof(address);
    
    std::thread writer_thread (writer, std::ref(turn), std::ref(write_guard), std::ref(mapping), std::ref(camera));
    int server_socket = socket(AF_INET, SOCK_STREAM, 0);
    bind(server_socket, (struct sockaddr *)&address, sizeof(address));
    listen(server_socket, 5);
    
    while (true) {
        int client_socket = accept(server_socket, (struct sockaddr *)&address, (socklen_t*)&address_length);
        std::cout << "New reader" << std::endl;
        std::thread reader_thread (reader, client_socket, std::ref(active_readers),
                                   std::ref(turn), std::ref(waiting_readers), std::ref(write_guard));
        reader_thread.detach();
    }
    writer_thread.join();
    camera.release();
    return 0;
}

void create_image(Mapping &mapping, raspicam::RaspiCam_Cv &camera) {
    static cv::Mat *new_image = new cv::Mat(mapping.resolution_y, mapping.resolution_x, mapping.type);
    static cv::Mat image;
    camera.grab();
    camera.retrieve(image);
    cv::remap(image, *new_image, *mapping.get_mapping_x(), *mapping.get_mapping_y(), cv::INTER_LINEAR);
    image.release();
    cv::imwrite("/mnt/rd/image.jpg", *new_image);
}

void writer(Semaphore &turn, Semaphore &write_guard, Mapping &mapping, raspicam::RaspiCam_Cv &camera) {
    while (true) {
//        std::cout << "Writer thread : Acquired turn" << std::endl;
        turn.wait();
        write_guard.wait();
        double total_time = 0;
        clock_t begin = clock();
        create_image(std::ref(mapping), std::ref(camera));
        clock_t end = clock();
        total_time += double(end - begin) / CLOCKS_PER_SEC;
//    std::cout << "Created image in " << total_time << "s" << std::endl;
        write_guard.signal();
        turn.signal();
    }
}

void reader(int socket, std::atomic_int &active_readers, Semaphore &turn,
            Semaphore &waiting_readers, Semaphore &write_guard) {
    char buffer[1] = {0};
    char response[1] = {1};
    while (true) {
        read(socket, buffer, 1);
        if (buffer[0] == 2) {
//            std::cout << "Reader thread: Acquiring turn" << std::endl;
            turn.wait();
            turn.signal();

//            std::cout << "Reader thread: Acquiring waiting_readers" << std::endl;
            waiting_readers.wait();
            if (std::atomic_load(&active_readers) == 0) {
                write_guard.wait();
            }
            std::atomic_fetch_add(&active_readers, 1);
            waiting_readers.signal();

//            std::cout << "Reader thread: Sending 1 to client socket" << std::endl;
            send(socket, response, 1, 0);
            
//            std::cout << "Reader thread: Wait to hear from client to read" << std::endl;
            read(socket, buffer, 1);

//            std::cout << "Reader thread: Acquiring waiting_readers" << std::endl;
            waiting_readers.wait();
            std::atomic_fetch_sub(&active_readers, 1);
            if (std::atomic_load(&active_readers) == 0) {
                    write_guard.signal();
                }
            waiting_readers.signal();
        }
	}
}
