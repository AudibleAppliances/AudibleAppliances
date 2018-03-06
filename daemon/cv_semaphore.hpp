#ifndef CV_SEMAPHORE
#define CV_SEMAPHORE

#include <mutex>
#include <atomic>
#include <condition_variable>
#include "semaphore.hpp"

class CV_Semaphore : public Semaphore {
    private:
        std::mutex mutex;
        std::condition_variable cv;
        std::atomic_uint count;
    public:
        CV_Semaphore(int initial);
        void signal();
        void wait();
};

#endif
