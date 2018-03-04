cmake_minimum_required(VERSION 3.7)
project(daemon)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES
        semaphore.cpp
        semaphore.hpp
        test.cpp)

add_executable(daemon ${SOURCE_FILES})