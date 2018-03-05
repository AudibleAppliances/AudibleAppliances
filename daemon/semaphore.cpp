#include "semaphore.hpp"

Semaphore::Semaphore() {
    count = 0;
}

Semaphore::Semaphore(int initial) {
    count = initial;
}

void Semaphore::signal() {
    std::unique_lock<decltype(mutex)> lock(mutex);
    count++;
    cv.notify_one();
}

void Semaphore::wait() {
    std::unique_lock<decltype(mutex)> lock(mutex);
    while (count == 0) {
        cv.wait(lock);
    }
    count--;
}
