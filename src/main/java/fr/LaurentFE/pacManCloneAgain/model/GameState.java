package fr.LaurentFE.pacManCloneAgain.model;

import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;

public class GameState {

  public GameMap gameMap;
  private boolean isRunning;

  public GameState() {
    gameMap = new GameMap(GameConfig.DEFAULT_MAP_TILE_HEIGHT,
        GameConfig.DEFAULT_MAP_TILE_WIDTH);
    isRunning = false;
  }

  public boolean isRunning() {
    return isRunning;
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
