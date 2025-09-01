package fr.LaurentFE.pacManCloneAgain.model;

import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.PacMan;
import fr.LaurentFE.pacManCloneAgain.model.entities.Pellet;
import fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality.Blinky;
import fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality.Clyde;
import fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality.Inky;
import fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality.Pinky;
import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;
import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

public class GameState {

  public GameMap gameMap;
  private boolean isRunning;
  public PacMan pacMan;
  public final Set<Ghost> ghosts;
  public final Set<Pellet> pellets;
  public int score;
  public int eatenGhosts;

  public GameState() {
    gameMap = new GameMap(GameConfig.DEFAULT_MAP_TILE_HEIGHT,
        GameConfig.DEFAULT_MAP_TILE_WIDTH);
    gameMap.loadMap(GameConfig.DEFAULT_MAP_PATH);
    pellets = gameMap.loadPellets(GameConfig.DEFAULT_PELLET_MAP_PATH);
    isRunning = false;
    ghosts = new HashSet<>();
    instantiateEntities();
    score = 0;
    eatenGhosts = 0;
  }

  private void instantiateEntities() {
    final int lives;
    if (pacMan == null) {
      lives = GameConfig.DEFAULT_LIVES;
    } else {
      lives = pacMan.getLives();
    }
    pacMan = new PacMan(GameConfig.DEFAULT_PACMAN_POSITION, GameConfig.DEFAULT_ORIENTATION,
        GameConfig.DEFAULT_MOVE_SPEED, gameMap, lives);
    instantiateGhosts();
  }

  private void instantiateGhosts() {
    ghosts.clear();
    ghosts.add(new Ghost(GameConfig.DEFAULT_BLINKY_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.RED, new Blinky(this), gameMap, GameConfig.DEFAULT_BLINKY_SCATTER_TILE_INDEX,
        GameConfig.DEFAULT_MOVE_SPEED));
    ghosts.add(new Ghost(GameConfig.DEFAULT_PINKY_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.PINK, new Pinky(this), gameMap, GameConfig.DEFAULT_PINKY_SCATTER_TILE_INDEX,
        GameConfig.DEFAULT_MOVE_SPEED));
    ghosts.add(new Ghost(GameConfig.DEFAULT_INKY_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.CYAN, new Inky(this), gameMap, GameConfig.DEFAULT_INKY_SCATTER_TILE_INDEX,
        GameConfig.DEFAULT_MOVE_SPEED));
    ghosts.add(new Ghost(GameConfig.DEFAULT_CLYDE_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.ORANGE, new Clyde(this), gameMap, GameConfig.DEFAULT_CLYDE_SCATTER_TILE_INDEX,
        GameConfig.DEFAULT_MOVE_SPEED));
  }

  public Ghost getBlinky() {
    for (Ghost ghost : ghosts) {
      if (ghost.getChasePersonality() instanceof Blinky) {
        return ghost;
      }
    }
    return null;
  }

  public Ghost getPinky() {
    for (Ghost ghost : ghosts) {
      if (ghost.getChasePersonality() instanceof Pinky) {
        return ghost;
      }
    }
    return null;
  }

  public Ghost getInky() {
    for (Ghost ghost : ghosts) {
      if (ghost.getChasePersonality() instanceof Inky) {
        return ghost;
      }
    }
    return null;
  }

  public Ghost getClyde() {
    for (Ghost ghost : ghosts) {
      if (ghost.getChasePersonality() instanceof Clyde) {
        return ghost;
      }
    }
    return null;
  }

  public boolean startGame() {
    if (!isRunning) {
      isRunning = true;
      return true;
    }
    return false;
  }

  public boolean stopGame() {
    if (isRunning) {
      isRunning = false;
      return true;
    }
    return false;
  }

  public void resetLevel() {
    instantiateEntities();
  }
}
