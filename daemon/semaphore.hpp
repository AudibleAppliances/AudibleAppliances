#include <mutex>
#include <atomic>
#include <condition_variable>

class Semaphore {
    public:
        virtual void signal() = 0;
        virtual void wait() = 0;
};
