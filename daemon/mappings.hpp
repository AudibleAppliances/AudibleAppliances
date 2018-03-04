#include <opencv2/opencv.hpp>

class Mapping {
    private:
        static const int type = 16;
        static const int resolution_x = 1280;
        static const int resolution_y = 720;
        double zoom;
        double strength;
        cv::Mat * mapping_x;
        cv::Mat * mapping_y;
    public:
        Mapping(double zoom, double strength);
        cv::Mat * get_mapping_x();
        cv::Mat * get_mapping_y();
};
