cmake_minimum_required(VERSION 3.17)
project(MallocReplacement)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fsanitize=address")
set(CMAKE_LINKER_FLAGS "${CMAKE_LINKER_FLAGS} -fsanitize=address")
set(CMAKE_CXX_STANDARD 14)

add_executable(MallocReplacement main.cpp MemoryManager.cpp MemoryManager.h)