#include <math.h>
#include <stdlib.h>
#include <opencv2/opencv.hpp>
#include <stdio.h>
#include <ctime>
#include <raspicam/raspicam_cv.h>
using namespace cv;
using namespace std;


int main(int argc, char** argv) {
    int type = 16;
    int resolution_x = 1280;
    int resolution_y = 720;
    double zoom = 1.2;
    double strength = 2.5;
    if (strength == 0) {
        strength = 0.00001;
    }
    float half_width = resolution_x/2;
    float half_height = resolution_y/2;
    
    raspicam::RaspiCam_Cv Camera;
    Camera.set(CV_CAP_PROP_FORMAT, CV_8UC1);
    Camera.set(CV_CAP_PROP_FRAME_WIDTH, resolution_x);
    Camera.set(CV_CAP_PROP_FRAME_HEIGHT, resolution_y);
    Camera.set(CV_CAP_PROP_EXPOSURE, 20);
   
    if (!Camera.open()) {
	cerr << "Failed to open the camera" << endl;
    }


    double correction_radius = sqrt(pow(resolution_x, 2) + pow(resolution_y, 2)) / strength;

    Mat* mapping_x = new Mat(resolution_y, resolution_x, CV_32FC1);
    Mat* mapping_y = new Mat(resolution_y, resolution_x, CV_32FC1);
    Mat* new_image = new Mat(resolution_y, resolution_x, type);


    for (int x = 0; x < resolution_x; x++) {
        for (int y = 0; y < resolution_y; y++) {
            float new_x = x - half_width;
            float new_y = y - half_height;

            double distance = sqrt(pow(new_x, 2) + pow(new_y, 2));
            double r = distance / correction_radius;

            double theta;
            if (r == 0) {
                theta = 1;
            } else {
                theta = atan(r) / r;
            }

            int source_x = static_cast<int>(half_width + theta * new_x * zoom);
            int source_y = static_cast<int>(half_height + theta * new_y * zoom);


            if ((0 <= source_x) && (source_x < resolution_x) && (0 <= source_y) && (source_x < resolution_x)) {
                mapping_x->at<float>(y,x) = source_x;
                mapping_y->at<float>(y,x) = source_y;
            }
        }
    }

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
