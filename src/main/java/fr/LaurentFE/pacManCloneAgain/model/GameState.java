package fr.LaurentFE.pacManCloneAgain.model;

public class GameState {

  public GameMap gameMap;

  public GameState() {
    gameMap = new GameMap(GameConfig.DEFAULT_MAP_TILE_HEIGHT,
        GameConfig.DEFAULT_MAP_TILE_WIDTH);
  }
}
