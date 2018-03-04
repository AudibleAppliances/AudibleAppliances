#include <math.h>
#include <stdlib.h>
#include <opencv2/opencv.hpp>
#include <stdio.h>
#include <ctime>
#include <raspicam/raspicam_cv.h>
#include "mappings.hpp"
using namespace cv;
using namespace std;


int main(int argc, char** argv) {
    Mapping mapping(1.2, 2.5);
    raspicam::RaspiCam_Cv Camera;
    Camera.set(CV_CAP_PROP_FORMAT, CV_8UC1);
    Camera.set(CV_CAP_PROP_FRAME_WIDTH, mapping.resolution_x);
    Camera.set(CV_CAP_PROP_FRAME_HEIGHT, mapping.resolution_y);
    Camera.set(CV_CAP_PROP_EXPOSURE, 20);
   
    if (!Camera.open()) {
	cerr << "Failed to open the camera" << endl;
    }
    
    Mat* new_image = new Mat(mapping.resolution_y, mapping.resolution_x, mapping.type);

    double total_time = 0;
    clock_t begin = clock();

    Mat image;
    Camera.grab();
    Camera.retrieve(image);

    remap(image, *new_image, *mapping.get_mapping_x(), *mapping.get_mapping_y(), INTER_LINEAR);
    image.release();
    imwrite("new_image.jpg", *new_image);
    clock_t end = clock();
    total_time += double(end - begin) / CLOCKS_PER_SEC;
    Camera.release();
    cout << total_time << endl;

    return 0;
}
