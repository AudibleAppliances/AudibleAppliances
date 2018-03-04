#include <mutex>
#include <condition_variable>

class Semaphore {
    private:
        std::mutex mutex;
        std::condition_variable cv;
        unsigned int count;
    public:
        Semaphore();
        Semaphore(int initial);
        void signal();
        void wait();
};
