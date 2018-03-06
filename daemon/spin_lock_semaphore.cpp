#include "spin_lock_semaphore.hpp"

Spin_Lock_Semaphore::Spin_Lock_Semaphore(int initial): pseudo_mutex ATOMIC_FLAG_INIT {
    count = initial;
}

void Spin_Lock_Semaphore::signal() {
    while (pseudo_mutex.test_and_set(std::memory_order_acquire)) {}
    count++;
    pseudo_mutex.clear();
}

void Spin_Lock_Semaphore::wait() {
    while (true) {
        while (pseudo_mutex.test_and_set(std::memory_order_acquire)) {}
        if (count != 0) {
            count--;
            pseudo_mutex.clear();
            break;
        }
        pseudo_mutex.clear();
    }
}
