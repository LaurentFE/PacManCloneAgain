package fr.LaurentFE.pacManCloneAgain.model;

import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.PacMan;
import fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality.Blinky;
import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;
import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

public class GameState {

  public GameMap gameMap;
  private boolean isRunning;
  public PacMan pacMan;
  public final Set<Ghost> ghosts;

  public GameState() {
    gameMap = new GameMap(GameConfig.DEFAULT_MAP_TILE_HEIGHT,
        GameConfig.DEFAULT_MAP_TILE_WIDTH);
    isRunning = false;
    ghosts = new HashSet<>();
    instantiateEntities();
  }

  private void instantiateEntities() {
    pacMan = new PacMan(GameConfig.DEFAULT_PACMAN_POSITION, GameConfig.DEFAULT_ORIENTATION,
        GameConfig.DEFAULT_MOVE_SPEED, gameMap);
    instantiateGhosts();
  }

  private void instantiateGhosts() {
    ghosts.add(new Ghost(GameConfig.DEFAULT_BLINKY_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.RED, new Blinky(this), gameMap, GameConfig.BLINKY_SCATTER_TILE_INDEX,
        GameConfig.DEFAULT_MOVE_SPEED));
    /*ghosts.add(new Ghost(GameConfig.DEFAULT_PINKY_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.PINK));
    ghosts.add(new Ghost(GameConfig.DEFAULT_INKY_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.CYAN));
    ghosts.add(new Ghost(GameConfig.DEFAULT_CLYDE_POSITION, GameConfig.DEFAULT_ORIENTATION,
        Color.ORANGE));*/
  }

  public Ghost getBlinky() {
    for (Ghost ghost : ghosts) {
      if (ghost.getChasePersonality() instanceof Blinky) {
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
}
