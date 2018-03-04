cmake_minimum_required(VERSION 3.7)
project(daemon)

set(CMAKE_CXX_STANDARD 11)
set(raspicam_DIR "/usr/local/lib/cmake")
find_package(Threads REQUIRED)
find_package(OpenCV REQUIRED)
find_package(raspicam REQUIRED)

set(SOURCE_FILES
        semaphore.cpp
        semaphore.hpp
	mappings.cpp
	mappings.hpp
        main.cpp)

add_executable(daemon ${SOURCE_FILES})

target_link_libraries(daemon ${CMAKE_THREAD_LIBS_INIT})
target_link_libraries(daemon ${OpenCV_LIBS})
target_link_libraries(daemon ${raspicam_CV_LIBS})
