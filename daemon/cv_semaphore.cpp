#include "cv_semaphore.hpp"

CV_Semaphore::CV_Semaphore(int initial) {
        count = initial;
}

void CV_Semaphore::signal() {
        std::unique_lock<decltype(mutex)> lock(mutex);
        count++;
        cv.notify_one();
}

void CV_Semaphore::wait() {
        std::unique_lock<decltype(mutex)> lock(mutex);
        while (count == 0) {
                cv.wait(lock);
        }
        count--;
}
