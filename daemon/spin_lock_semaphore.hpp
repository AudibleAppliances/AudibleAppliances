#ifndef SPIN_LOCK_SEMAPHORE
#define SPIN_LOCK_SEMAPHORE

#include "semaphore.hpp"
#include <mutex>
#include <atomic>
#include <condition_variable>


class Spin_Lock_Semaphore : public Semaphore {
    private:
        std::atomic_flag pseudo_mutex;
        std::atomic_uint count;
    public:
        Spin_Lock_Semaphore(int initial);
        void signal();
        void wait();
};
#endif
