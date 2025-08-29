package fr.LaurentFE.pacManCloneAgain.model;

import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;

public class GameConfig {

  public static final int TILE_SIZE = 32;
  public static final int DEFAULT_MAP_TILE_HEIGHT = 36;
  public static final int DEFAULT_MAP_TILE_WIDTH = 28;
  public static final String DEFAULT_MAP_PATH = "./src/main/resources/level0";
  public static final double FPS = 60;
  public static final double DRAW_INTERVAL_NANOSEC = 1_000_000_000L / FPS;
  public static final int DEFAULT_MOVE_SPEED = TILE_SIZE / 8;
  public static final Position DEFAULT_PACMAN_POSITION = new TileIndex(13, 26).toPosition();
  public static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
}
