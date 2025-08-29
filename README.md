# PacMan clone, again, in Java

## State of the project
Application can load the level from a file, and display it properly in a JFrame.

## Context of the project
### WHAT
A clone of the classic arcade game Pac-Man, in Java.\
This project is created after a code review with a Java developer with experience in game development.\
It aims to have the same final product as the previous PacManClone project, but with better practices, and code organisation.

### HOW
Developed in Java JDK 21

IDE : https://www.jetbrains.com/idea/ (Community Edition)\
Java Code Style : https://google.github.io/styleguide/javaguide.html (Automated in IDE)

### WHY
The goals are :
- To create a video game purely in Java
- To implement the better practices outlined by the code review with an experienced Java Developer.

### SCOPE
The target result is :
- Have a fully navigable maze
- PacMan can be moved around the maze, respecting constraints
- 4 Ghosts chase PacMan with different behaviours to reach him
- Ghosts can chase, disperse, be frightened, and go resurrect at their base
- PacMan can eat pellets to increase score
- PacMan can eat a power pellet to make them frightened
- PacMan can eat a frightened ghost, increasing score and forcing the ghost to go resurrect at their base
- Game is finished when all pellets have been eaten
