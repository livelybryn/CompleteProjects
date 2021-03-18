//
// Created by Bryn Koldewyn on 3/15/21.
//

#include "MemoryManager.h"
#include <sys/mman.h>
#include <functional>
#include <iostream>

MemoryManager::MemoryManager(size_t size) {
    table =
            reinterpret_cast<TableEntry*>(mmap(0, size, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANON, -1, 0));
    tableSize = 0;
    tableCapacity = size;
}

MemoryManager::TableEntry::TableEntry(bool active, void* ptr, size_t size) {
    this->active = active;
    this->ptr = ptr;
    this->size = size;
}

void* MemoryManager::allocate(size_t bytesToAllocate) {
    if (.60 < (float) tableSize / (float) tableCapacity) { // if the table is over 60% full, grow tables
        growTable();
    }
    // call map with size
    void *ptr = mmap(nullptr, bytesToAllocate, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANON, -1, 0);
    // create new entry with ptr to memory and size of memory
    TableEntry newEntry = TableEntry(true, ptr, bytesToAllocate);
    // add entry to hashTable
    this->insertEntry(newEntry, tableCapacity);
    tableSize++;
    return ptr;
}

void MemoryManager::deallocate(void *ptr) {
    for (int i = 0; i < tableCapacity; i++) {
        if (table[i].active) {
            if (table[i].ptr == ptr) { // look for ptr
                munmap(table[i].ptr, table[i].size); // Deallocate that table's memory
                deleteEntry(table[i]);
                break;
            }
        }
    }
}

void MemoryManager::insertEntry(TableEntry &newEntry, int capacity) {
    std::hash<void*> hash_fun; // create hash function
    size_t index = (hash_fun(newEntry.ptr)) % capacity; // get ptr's hash
    if (!(loopThroughArray(index, capacity - 1, newEntry))) {
        loopThroughArray(0, index, newEntry);
    }
}

bool MemoryManager::loopThroughArray(int start, int end, TableEntry &newEntry) {
    for (int i = start; i < end; i++) {
        if (!table[i].active) { // check if spot is empty
            std::swap(table[i], newEntry);
            return true;
        }
    }
    return false;
}

void MemoryManager::deleteEntry(TableEntry &newEntry) {
    newEntry.ptr = nullptr;
    newEntry.active = false;
    newEntry.size = -1;
    tableSize--;
}

void MemoryManager::growTable() {
    std::cout << "******** GROWING TABLE *********" << std::endl;

    MemoryManager biggerTable = MemoryManager(tableCapacity * 2); // pointer to memory

    for (int i = 0; i < tableCapacity; i++) { // 0 -> tableCapacity (10)
        if (this->table[i].active) { // if table is active, move it to new table
            biggerTable.insertEntry(table[i], (tableCapacity * 2));
        }
    }
    std::swap(tableCapacity, biggerTable.tableCapacity);
    std::swap(table, biggerTable.table);

}

MemoryManager::~MemoryManager() {
    if (tableSize != 0) {
        for (int i = 0; i < tableCapacity; i++) {
            if (table[i].ptr != nullptr) {
                deallocate(table[i].ptr);
            }
        }
    }
    munmap(table, (tableCapacity));

}