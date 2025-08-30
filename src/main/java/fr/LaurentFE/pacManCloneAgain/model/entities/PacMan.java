package fr.LaurentFE.pacManCloneAgain.model.entities;

import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import fr.LaurentFE.pacManCloneAgain.model.map.TileType;

public class PacMan extends MovingEntity {

  private final int maxMouthAngle;
  private int currentMouthAngle;
  private int mouthAngleIncrement;

  public PacMan(final Position startingPosition, final Orientation startingOrientation,
      final int moveSpeed, final GameMap gameMap) {
    super(startingPosition, startingOrientation, moveSpeed, gameMap);
    maxMouthAngle = 90;
    currentMouthAngle = maxMouthAngle;
    mouthAngleIncrement = -5;
  }

  public int getCurrentMouthAngle() {
    return currentMouthAngle;
  }

  public void animateMouth() {
    currentMouthAngle += mouthAngleIncrement;
    if (currentMouthAngle >= maxMouthAngle) {
      currentMouthAngle = maxMouthAngle;
      mouthAngleIncrement *= -1;
    } else if (currentMouthAngle <= 0) {
      currentMouthAngle = 0;
      mouthAngleIncrement *= -1;
    }
  }

  public void update(final Orientation nextOrientation) {
    if (nextOrientation != null && !tryToChangeDirection(nextOrientation)) {
      updatePosition();
    }
  }

  private boolean tryToChangeDirection(final Orientation nextOrientation) {
    if (nextOrientation == orientation) {
      return false;
    }

    if (canGetIntoPath(nextOrientation)) {
      orientation = nextOrientation;
      updatePosition();
      return true;
    }
    return false;
  }

  protected boolean canGoThroughTile(final TileIndex tileIndex) {
    return gameMap.getTile(tileIndex) == TileType.PATH;
  }

  protected void updatePosition() {
    move();
    if (!checkForWallCollisions()) {
      animateMouth();
    }
  }
}
