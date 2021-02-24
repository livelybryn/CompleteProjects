#include <iostream>
#include "shelpers.hpp"
#include <unistd.h>

int main() {
    std::string line;
    while (std::getline(std::cin, line)) {
        std::vector<Command> commands = getCommands(tokenize(line)); // gets all commands

        if (line == "exit") {
            exit(1);
        }
        // if command is "cd"
        if (strcmp(commands[0].exec.c_str(), "cd") == 0) {
            // if no input after cd
            if (commands[0].argv[1] == nullptr) {
                // change directory to home and error check
                if (chdir(getenv("HOME")) == -1) {
                    perror("Change directory to home failed");
                }
            } else {
                // change directory to input and error check
                if (chdir(commands[0].argv[1]) == -1) {
                    perror("Change directory to input failed");
                }
            }
            continue;
        }

        for (auto c: commands) { // loop through all commands
            // if there is an "=" in the command
            if (c.exec.find("=") != std::string::npos) {
                std::string name = c.exec.substr(0, c.exec.find("=")); // get string before the "="
                std::string value = c.exec.substr(name.size() + 1); // get string after the "="
                setenv(name.c_str(), value.c_str(), 1);
                continue;
            }

            pid_t pid = fork();

            if (pid == -1) {
                perror("Fork failed"); // error check
                exit(1);
            } else if (pid == 0) {
                // child
                if (c.fdStdin != 0) {
                    if (dup2(c.fdStdin, 0) == -1) { // error check for stdin
                        perror("dup2 of stdin");
                    }
                }
                if (c.fdStdout != 1) {
                    if (dup2(c.fdStdout, 1) == -1) { // error check stdout
                        perror("dup2 of stdout");
                    }
                }

                if (execvp(const_cast<char *>(c.exec.c_str()), const_cast<char *const *>(c.argv.data())) == -1) { // run command
                    perror("Execvp failed"); // error check
                    exit(1);
                }
            } else if (pid > 0) {
                // parent
                if (wait(0) == -1) {
                    perror("Wait failed"); // error check
                    exit(1);
                }
                if (c.fdStdin != 0) {
                    if (close(c.fdStdin) == -1) { // error check for closing stdin
                        perror("closing stdin");
                    }
                }
                if (c.fdStdout != 1) {
                    if (close(c.fdStdout) == -1) { // error check for closing stdout
                        perror("closing stdout");
                    }
                }
            }
        }
    }
    return 0;
}


