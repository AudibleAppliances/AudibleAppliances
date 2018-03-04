#include <mutex>
#include <atomic>
#include <condition_variable>

class Semaphore {
    private:
        std::mutex mutex;
        std::condition_variable cv;
        std::atomic_uint count;
    public:
        Semaphore();
        Semaphore(int initial);
        void signal();
        void wait();
};
