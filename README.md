# Pac-Man clone, again, in Java

Please enjoy this recreation of the classic arcade game Pac-Man, in Java.\
Note that by default, the game panel will take ```896*1125 pixels``` in size (not accounting for the window borders around it).\
If your display is not at a high enough resolution, you can change the ```TILE_SIZE``` from ```32``` to ```16``` in the following file on ```line 9``` :\
```<project folder>/src/main/java/fr/LaurentFE/pacManCloneAgain/model/GameConfig.java```

## How to run project
Make sure your Java home environment variable is set to the Java version 21, as it is the version used to develop this application.\
This project if built with Maven, so make also sure to have Maven installed.

Build and run with command line directly from the project folder :\
```mvn clean compile exec:java```\

If you have built the game for the first time, no need to rebuild it everytime, you can simply run :\
```mvn exec:java```

## Controls
Move with ```directional arrows``` or ```Z Q S D```

## State of the project
All originally intended features are now implemented.

## Context of the project
### WHAT
A clone of the classic arcade game Pac-Man, in Java.\
This project is created after a code review with an experienced Java developer, also experienced in game development.\
It aims to have the same final product as the previous [PacManClone](https://github.com/LaurentFE/PacManClone) project, but with better practices, and code organisation.

### HOW
Developed in Java JDK 21

Built with Maven : https://maven.apache.org/ 

IDE : https://www.jetbrains.com/idea/ (Community Edition)\
Java Code Style : https://google.github.io/styleguide/javaguide.html (Automated in IDE)

### WHY
The goals are :
- To create a video game purely in Java
- To implement the better practices outlined by the code review with an experienced Java Developer.

### SCOPE
The target result is :
- Have a fully navigable maze
- Pac-Man can be moved around the maze, respecting constraints
- 4 Ghosts chase Pac-Man with different behaviours to reach him
- Ghosts can chase, disperse, be frightened, and go resurrect at their base
- Ghosts can kill Pac-Man if they catch him
- Pac-Man can eat pellets to increase score
- Pac-Man can eat a power pellet to make them frightened
- Pac-Man can eat a frightened ghost, increasing score and forcing the ghost to go resurrect at their base
- Game is finished when all pellets have been eaten or all lives are lost
