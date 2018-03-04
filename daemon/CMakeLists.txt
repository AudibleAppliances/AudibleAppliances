cmake_minimum_required(VERSION 3.7)
project(daemon)

set(CMAKE_CXX_STANDARD 11)
find_package(Threads)
find_package(OpenCV REQUIRED)

set(SOURCE_FILES
        semaphore.cpp
        semaphore.hpp
	mappings.cpp
	mappings.hpp
        main.cpp)

add_executable(daemon ${SOURCE_FILES})

target_link_libraries(daemon ${CMAKE_THREAD_LIBS_INIT})
target_link_libraries(daemon ${OpenCV_LIBS})
