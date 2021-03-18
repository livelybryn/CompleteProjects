//
// Created by Bryn Koldewyn on 3/15/21.
//

#ifndef MALLOCREPLACEMENT_MEMORYMANAGER_H
#define MALLOCREPLACEMENT_MEMORYMANAGER_H


#include <cstddef>

class MemoryManager {

private:
    struct TableEntry {
        bool active = false;
        void* ptr;
        size_t size;

        TableEntry(bool active, void* ptr, size_t size);
    };

public:

    void* allocate(size_t bytesToAllocate);
    void deallocate(void* ptr);
    MemoryManager(size_t size);
    ~MemoryManager();

    TableEntry* table;
    size_t tableSize;
    size_t tableCapacity;
    void insertEntry(TableEntry &newEntry, int capacity);
    void deleteEntry(TableEntry &newEntry);
    void growTable();
    bool loopThroughArray(int start, int end, TableEntry &newEntry);
};


#endif //MALLOCREPLACEMENT_MEMORYMANAGER_H
