# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.17

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Disable VCS-based implicit rules.
% : %,v


# Disable VCS-based implicit rules.
% : RCS/%


# Disable VCS-based implicit rules.
% : RCS/%,v


# Disable VCS-based implicit rules.
% : SCCS/s.%


# Disable VCS-based implicit rules.
% : s.%


.SUFFIXES: .hpux_make_needs_suffix_list


# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = "/Users/brynkoldewyn/Library/Application Support/JetBrains/Toolbox/apps/CLion/ch-0/203.6682.181/CLion.app/Contents/bin/cmake/mac/bin/cmake"

# The command to remove a file.
RM = "/Users/brynkoldewyn/Library/Application Support/JetBrains/Toolbox/apps/CLion/ch-0/203.6682.181/CLion.app/Contents/bin/cmake/mac/bin/cmake" -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/MallocReplacement.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/MallocReplacement.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/MallocReplacement.dir/flags.make

CMakeFiles/MallocReplacement.dir/main.cpp.o: CMakeFiles/MallocReplacement.dir/flags.make
CMakeFiles/MallocReplacement.dir/main.cpp.o: ../main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/MallocReplacement.dir/main.cpp.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/MallocReplacement.dir/main.cpp.o -c /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/main.cpp

CMakeFiles/MallocReplacement.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/MallocReplacement.dir/main.cpp.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/main.cpp > CMakeFiles/MallocReplacement.dir/main.cpp.i

CMakeFiles/MallocReplacement.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/MallocReplacement.dir/main.cpp.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/main.cpp -o CMakeFiles/MallocReplacement.dir/main.cpp.s

CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.o: CMakeFiles/MallocReplacement.dir/flags.make
CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.o: ../MemoryManager.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.o -c /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/MemoryManager.cpp

CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/MemoryManager.cpp > CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.i

CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/MemoryManager.cpp -o CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.s

# Object files for target MallocReplacement
MallocReplacement_OBJECTS = \
"CMakeFiles/MallocReplacement.dir/main.cpp.o" \
"CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.o"

# External object files for target MallocReplacement
MallocReplacement_EXTERNAL_OBJECTS =

MallocReplacement: CMakeFiles/MallocReplacement.dir/main.cpp.o
MallocReplacement: CMakeFiles/MallocReplacement.dir/MemoryManager.cpp.o
MallocReplacement: CMakeFiles/MallocReplacement.dir/build.make
MallocReplacement: CMakeFiles/MallocReplacement.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Linking CXX executable MallocReplacement"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/MallocReplacement.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/MallocReplacement.dir/build: MallocReplacement

.PHONY : CMakeFiles/MallocReplacement.dir/build

CMakeFiles/MallocReplacement.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/MallocReplacement.dir/cmake_clean.cmake
.PHONY : CMakeFiles/MallocReplacement.dir/clean

CMakeFiles/MallocReplacement.dir/depend:
	cd /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug /Users/brynkoldewyn/brynKoldewyn/CS6013/MallocReplacement/cmake-build-debug/CMakeFiles/MallocReplacement.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/MallocReplacement.dir/depend

