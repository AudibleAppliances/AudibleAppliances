#include <math.h>
#include <stdlib.h>
#include <opencv2/opencv.hpp>
#include <stdio.h>
#include <ctime>
#include <raspicam/raspicam_cv.h>
using namespace cv;
using namespace std;


int main(int argc, char** argv) {
    raspicam::RaspiCam_Cv Camera;
    Camera.set(CV_CAP_PROP_FORMAT, CV_8UC1);
    Camera.set(CV_CAP_PROP_FRAME_WIDTH, resolution_x);
    Camera.set(CV_CAP_PROP_FRAME_HEIGHT, resolution_y);
    Camera.set(CV_CAP_PROP_EXPOSURE, 20);
   
    if (!Camera.open()) {
	cerr << "Failed to open the camera" << endl;
    }

    Mat* new_image = new Mat(resolution_y, resolution_x, type);

    double total_time = 0;
    clock_t begin = clock();

    Mat image;
    Camera.grab();
    Camera.retrieve(image);

    remap(image, *new_image, *mapping_x, *mapping_y, INTER_LINEAR);
    image.release();
    imwrite("new_image.jpg", *new_image);
    clock_t end = clock();
    total_time += double(end - begin) / CLOCKS_PER_SEC;
    Camera.release();
    cout << total_time / 100 << endl;

    return 0;
}
