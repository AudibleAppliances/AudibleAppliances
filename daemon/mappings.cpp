#include "mappings.hpp"
#include <math.h>

cv::Mat * Mapping::get_mapping_x() {
    return mapping_x;
}

cv::Mat * Mapping::get_mapping_y() {
    return mapping_y;
}

Mapping::Mapping(double zoom, double strength, bool rotate) {
    this->zoom = zoom;
    this->strength = strength;
    
    double correction_radius = sqrt(pow(resolution_x, 2) + pow(resolution_y, 2)) / strength;
    
    this->mapping_x = new cv::Mat(resolution_y, resolution_x, CV_32FC1);
    this->mapping_y = new cv::Mat(resolution_y, resolution_x, CV_32FC1);
    
    float half_width = resolution_x/2;
    float half_height = resolution_y/2;
    
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
                if (rotate) {
                    mapping_x->at<float>(y,x) = source_x;
                    mapping_y->at<float>(y,x) = resolution_y - source_y;
                } else {
                    mapping_x->at<float>(y,x) = source_x;
                    mapping_y->at<float>(y,x) = source_y;
                }
            }
        }
    }
}
