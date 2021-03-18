#include <iostream>
#include "MemoryManager.h"
#include <chrono>
#include <algorithm>

/**
 * This method prints out all table ptr's in a MemoryManager
 * @param manager the manager with the table to print out
 */
void printTable(MemoryManager &manager, std::string message) {
    std::cout << message << std::endl;
    for (int i = 0; i < manager.tableCapacity; i++){
        std::cout << "Table " << i << " : " << manager.table[i].ptr << std::endl;
    }
    std::cout << "Size: " << manager.tableSize << std::endl;
    std::cout << "Capacity: " << manager.tableCapacity << std::endl;
}

/**
 * This method times how fast it takes to allocate memory using my malloc replacement and the regular malloc replacement.
 * It times how long it takes to allocate memory for an int 100 times and finds the average time it takes.
 */
void timingTest() {
    MemoryManager timingTest = MemoryManager(200);

    int numOfLoops = 100;
    auto totalDuration = 0;
    auto totalDurationMalloc = 0;

    for (int i = 0; i < numOfLoops; i++) { // loops 100 times
        int *warmUpMalloc = (int *) malloc(sizeof(int)); // c++ malloc
        int *warmUpAllocate = (int *) (timingTest.allocate(sizeof(int))); // my malloc replacement

        // Test for my malloc replacement
        auto start = std::chrono::high_resolution_clock::now();
        int *testInt1 = (int *) (timingTest.allocate(sizeof(int)));
        auto stop = std::chrono::high_resolution_clock::now();

        auto duration = (stop - start); // total time for one loop

        // Test for c++ malloc
        auto startMalloc = std::chrono::high_resolution_clock::now();
        int *testInt1Malloc = (int *) malloc(sizeof(int));
        auto stopMalloc = std::chrono::high_resolution_clock::now();

        auto durationMalloc = (stopMalloc - startMalloc); // total time for one loop

        totalDuration += duration.count(); // calculates total time for all loops
        totalDurationMalloc += durationMalloc.count(); // calculates total time for all loops

        timingTest.deallocate(warmUpAllocate); // deallocates my malloc replacement
        free(warmUpMalloc); // deallocates c++ malloc
        timingTest.deallocate(testInt1); // deallocates my malloc replacement
        free(testInt1Malloc); // deallocates c++ malloc
    }

    auto averageDuration = totalDuration / numOfLoops;
    auto averageDurationMalloc = totalDurationMalloc / numOfLoops;

    std::cout << "Time taken by my allocate is: " << averageDuration << " nanoseconds" << std::endl;
    std::cout << "Time taken by malloc is: " << averageDurationMalloc << " nanoseconds" << std::endl;

}
/**
 * INSTRUCTIONS FOR RUNNING TESTS AND BENCHMARK:
 * Just run main. :) That will print table values to the console, showing allocations and deallocations as well as a
 * times in nanoseconds for how long my allocation takes vs. malloc.
 * @return 
 */
int main() {
    // initialize Memory Manager
    MemoryManager testManager = MemoryManager(10);

    // starting empty table
    printTable(testManager, "Starting table: ");

    // allocates memeory for 9 ints, causing the table to need to grow
    int *testInt1 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt2 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt3 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt4 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt5 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt6 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt7 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt8 = (int *) (testManager.allocate(sizeof(int)));
    int *testInt9 = (int *) (testManager.allocate(sizeof(int)));

    printTable(testManager, "Table after 9 insertions and a grow: ");

    // assigning values for ints
    *testInt1 = 5;
    *testInt2 = 6;
    *testInt3 = 7;
    *testInt4 = 4;
    *testInt5 = 3;
    *testInt6 = 2;
    *testInt7 = 1;
    *testInt8 = 2;
    *testInt9 = 5;

    // deallocating and deleting 3 ints
    testManager.deallocate(testInt1);
    testManager.deallocate(testInt2);
    testManager.deallocate(testInt3);

    // printing after 3 deletes
    printTable(testManager, "Table after 3 deletes: ");

    // time test for my malloc replacement and c++ malloc
    timingTest();

    return 0;
}







