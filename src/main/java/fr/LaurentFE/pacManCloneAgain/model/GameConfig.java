package fr.LaurentFE.pacManCloneAgain.model;

import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import java.awt.event.KeyEvent;

public class GameConfig {

  public static final int DEFAULT_UP_KEY = KeyEvent.VK_Z;
  public static final int DEFAULT_RIGHT_KEY = KeyEvent.VK_D;
  public static final int DEFAULT_DOWN_KEY = KeyEvent.VK_S;
  public static final int DEFAULT_LEFT_KEY = KeyEvent.VK_Q;
  public static final int TILE_SIZE = 32;
  public static final int DEFAULT_MAP_TILE_HEIGHT = 36;
  public static final int DEFAULT_MAP_TILE_WIDTH = 28;
  public static final String DEFAULT_MAP_PATH = "./src/main/resources/level0";
  public static final String DEFAULT_PELLET_MAP_PATH = "./src/main/resources/level0_pellets";
  public static final double FPS = 60;
  public static final double DRAW_INTERVAL_NANOSEC = 1_000_000_000L / FPS;
  public static final int DEFAULT_MOVE_SPEED = TILE_SIZE / 8;
  public static final int DEFAULT_LIVES = 3;
  public static final Position DEFAULT_PACMAN_POSITION = new TileIndex(13, 26).toPosition();
  public static final Orientation DEFAULT_ORIENTATION = Orientation.RIGHT;
  public static final Position DEFAULT_BLINKY_POSITION = new TileIndex(9, 14).toPosition();
  public static final Position DEFAULT_INKY_POSITION = new TileIndex(12, 14).toPosition();
  public static final Position DEFAULT_CLYDE_POSITION = new TileIndex(15, 14).toPosition();
  public static final Position DEFAULT_PINKY_POSITION = new TileIndex(18, 14).toPosition();
  public static final long CHASE_NANO_TIME_DURATION = 20_000_000_000L;
  public static final long SCATTER_NANO_TIME_DURATION = 3_000_000_000L;
  public static final long FRIGHTENED_NANO_TIME_DURATION = 5_000_000_000L;
  public static final TileIndex DEFAULT_BLINKY_SCATTER_TILE_INDEX = new TileIndex(0, 0);
  public static final TileIndex DEFAULT_PINKY_SCATTER_TILE_INDEX = new TileIndex(
      DEFAULT_MAP_TILE_WIDTH - 1, 0);
  public static final TileIndex DEFAULT_INKY_SCATTER_TILE_INDEX = new TileIndex(
      DEFAULT_MAP_TILE_WIDTH - 1, DEFAULT_MAP_TILE_HEIGHT - 1);
  public static final TileIndex DEFAULT_CLYDE_SCATTER_TILE_INDEX = new TileIndex(0,
      DEFAULT_MAP_TILE_HEIGHT - 1);
  public static final int DEFAULT_GHOST_SCORE = 100;
  public static final int DEFAULT_PELLET_SIZE = TILE_SIZE / 8;
  public static final int DEFAULT_PELLET_OFFSET = (TILE_SIZE - DEFAULT_PELLET_SIZE) / 2;
  public static final int DEFAULT_POWER_PELLET_SIZE = TILE_SIZE / 2;
  public static final int DEFAULT_POWER_PELLET_OFFSET = (TILE_SIZE - DEFAULT_POWER_PELLET_SIZE) / 2;
}
