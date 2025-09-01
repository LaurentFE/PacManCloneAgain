package fr.LaurentFE.pacManCloneAgain.model.entities;

import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import fr.LaurentFE.pacManCloneAgain.model.map.TileType;

public class PacMan extends MovingEntity {

  private final int maxMouthAngle;
  private int currentMouthAngle;
  private int mouthAngleIncrement;
  private boolean isAlive;
  private int lives;
  private boolean deathAnimationFinished;

  public PacMan(final Position startingPosition, final Orientation startingOrientation,
      final int moveSpeed, final GameMap gameMap, final int lives) {
    super(startingPosition, startingOrientation, moveSpeed, gameMap);
    maxMouthAngle = 90;
    currentMouthAngle = maxMouthAngle;
    mouthAngleIncrement = -5;
    isAlive = true;
    this.lives = lives;
    deathAnimationFinished = false;
  }

  public int getCurrentMouthAngle() {
    return currentMouthAngle;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public int getLives() {
    return lives;
  }

  public boolean isDeathAnimationFinished() {
    return deathAnimationFinished;
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
    if (isAlive) {
      if (nextOrientation != null && !tryToChangeDirection(nextOrientation)) {
        updatePosition();
      }
    } else {
      if (!animateDeath()) {
        return;
      }
      lives--;
      deathAnimationFinished = true;
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

  public void kill() {
    isAlive = false;
    deathAnimationFinished = false;
  }

  private boolean animateDeath() {
    orientation = Orientation.UP;
    if (mouthAngleIncrement < 0) {
      mouthAngleIncrement *= -1;
    }
    currentMouthAngle += mouthAngleIncrement;
    return currentMouthAngle >= 360;
  }

  protected void updatePosition() {
    move();
    if (!checkForWallCollisions()) {
      animateMouth();
    }
  }
}
