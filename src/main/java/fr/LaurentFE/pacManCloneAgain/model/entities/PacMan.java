package fr.LaurentFE.pacManCloneAgain.model.entities;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import fr.LaurentFE.pacManCloneAgain.model.map.TileType;
import java.awt.Rectangle;

public class PacMan {

  private Orientation orientation;
  private final Rectangle hitBox;
  private final int moveSpeed;
  private final int maxMouthAngle;
  private int currentMouthAngle;
  private int mouthAngleIncrement;
  private final GameMap gameMap;

  public PacMan(final Position startingPosition, final Orientation startingOrientation,
      final int moveSpeed, final GameMap gameMap) {
    hitBox = new Rectangle(startingPosition.x, startingPosition.y, GameConfig.TILE_SIZE,
        GameConfig.TILE_SIZE);
    orientation = startingOrientation;
    this.moveSpeed = moveSpeed;
    maxMouthAngle = 90;
    currentMouthAngle = maxMouthAngle;
    mouthAngleIncrement = -5;
    this.gameMap = gameMap;
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public int getCurrentMouthAngle() {
    return currentMouthAngle;
  }

  public Position getPosition() {
    return new Position(hitBox.x, hitBox.y);
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

  private boolean canGetIntoPath(final Orientation nextOrientation) {
    final Rectangle pathTile = getNextPathTileForOrientation(nextOrientation);
    if (pathTile.equals(new Rectangle())) {
      return false;
    }

    if (nextOrientation == Orientation.UP
        || nextOrientation == Orientation.DOWN) {
      if (pathTile.x - hitBox.x < moveSpeed
          && pathTile.x - hitBox.x > -moveSpeed) {
        hitBox.x = pathTile.x;
        return true;
      }
    } else {
      if (pathTile.y - hitBox.y < moveSpeed
          && pathTile.y - hitBox.y > -moveSpeed) {
        hitBox.y = pathTile.y;
        return true;
      }
    }
    return false;
  }

  private Rectangle getNextPathTileForOrientation(final Orientation nextOrientation) {
    final TileIndex directionModifier = switch(nextOrientation) {
      case UP -> new TileIndex(0, -1);
      case LEFT -> new TileIndex(-1, 0);
      case DOWN -> new TileIndex(0, 1);
      case RIGHT -> new TileIndex(1, 0);
    };

    final TileIndex tileAIndex = new Position(hitBox.x, hitBox.y).toTileIndex()
        .add(directionModifier);
    final TileIndex tileBIndex = new Position(hitBox.x, hitBox.y).toTileIndex()
        .add(directionModifier);
    final TileIndex tileCIndex = new Position(hitBox.x, hitBox.y).toTileIndex()
        .add(directionModifier);

    if (nextOrientation == Orientation.UP || nextOrientation == Orientation.DOWN) {
      tileAIndex.x -= 1;
      tileCIndex.x += 1;
    } else {
      tileAIndex.y -= 1;
      tileCIndex.y += 1;
    }

    tileLoopAroundHorizontal(tileAIndex);
    tileLoopAroundHorizontal(tileBIndex);
    tileLoopAroundHorizontal(tileCIndex);

    final Position tileAPosition = tileAIndex.toPosition();
    final Position tileBPosition = tileBIndex.toPosition();
    final Position tileCPosition = tileCIndex.toPosition();

    if (canGoThroughTile(tileBIndex)) {
      return new Rectangle(
          tileBPosition.x,
          tileBPosition.y,
          GameConfig.TILE_SIZE,
          GameConfig.TILE_SIZE
      );
    } else if (canGoThroughTile(tileAIndex)) {
      return new Rectangle(
          tileAPosition.x,
          tileAPosition.y,
          GameConfig.TILE_SIZE,
          GameConfig.TILE_SIZE
      );
    } else if (canGoThroughTile(tileCIndex)) {
      return new Rectangle(
          tileCPosition.x,
          tileCPosition.y,
          GameConfig.TILE_SIZE,
          GameConfig.TILE_SIZE
      );
    } else {
      return new Rectangle();
    }
  }

  private void tileLoopAroundHorizontal(TileIndex tile) {
    if (tile.x < 0) {
      tile.x = gameMap.getMapWidthTile() - 1;
    }
    if (tile.x >= gameMap.getMapWidthTile()) {
      tile.x = 0;
    }
  }

  private boolean canGoThroughTile(final TileIndex tileIndex) {
    return gameMap.getTile(tileIndex) == TileType.PATH;
  }

  private void updatePosition() {
    move();
    if (!checkForWallCollisions()) {
      animateMouth();
    }
  }

  private void move() {
    if (orientation == Orientation.LEFT) {
      hitBox.x = hitBox.x - moveSpeed;
      if (hitBox.x < -GameConfig.TILE_SIZE) {
        hitBox.x = (gameMap.getMapWidthTile() + 1) * GameConfig.TILE_SIZE + hitBox.x;
      }
    } else if (orientation == Orientation.RIGHT) {
      hitBox.x = hitBox.x + moveSpeed;
      if (hitBox.x >= gameMap.getMapWidthTile() * GameConfig.TILE_SIZE) {
        hitBox.x = hitBox.x - (gameMap.getMapWidthTile() + 1) * GameConfig.TILE_SIZE;
      }
    } else if (orientation == Orientation.UP) {
      hitBox.y = hitBox.y - moveSpeed;
    } else if (orientation == Orientation.DOWN) {
      hitBox.y = hitBox.y + moveSpeed;
    }
  }

  private boolean checkForWallCollisions() {
    final TileIndex upperLeftTile = new Position(hitBox.x, hitBox.y).toTileIndex();
    final TileIndex upperRightTile = new Position(hitBox.x + hitBox.width - 1,
        hitBox.y).toTileIndex();
    final TileIndex lowerLeftTile = new Position(hitBox.x,
        hitBox.y + hitBox.height - 1).toTileIndex();
    final TileIndex lowerRightTile = new Position(hitBox.x + hitBox.width - 1,
        hitBox.y + hitBox.height - 1).toTileIndex();

    tileLoopAroundHorizontal(upperLeftTile);
    tileLoopAroundHorizontal(upperRightTile);
    tileLoopAroundHorizontal(lowerLeftTile);
    tileLoopAroundHorizontal(lowerRightTile);

    if (!canGoThroughTile(upperLeftTile)) {
      bumpOutOfCollision(upperLeftTile);
      return true;
    } else if (!canGoThroughTile(upperRightTile)) {
      bumpOutOfCollision(upperRightTile);
      return true;
    } else if (!canGoThroughTile(lowerLeftTile)) {
      bumpOutOfCollision(lowerLeftTile);
      return true;
    } else if (!canGoThroughTile(lowerRightTile)) {
      bumpOutOfCollision(lowerRightTile);
      return true;
    }
    return false;
  }

  private void bumpOutOfCollision(final TileIndex collisionTileIndex) {
    final Position collisionTilePosition = collisionTileIndex.toPosition();
    if (orientation == Orientation.LEFT) {
      hitBox.x = collisionTilePosition.x + GameConfig.TILE_SIZE;
    } else if (orientation == Orientation.RIGHT) {
      hitBox.x = collisionTilePosition.x - GameConfig.TILE_SIZE;
    } else if (orientation == Orientation.UP) {
      hitBox.y = collisionTilePosition.y + GameConfig.TILE_SIZE;
    } else if (orientation == Orientation.DOWN) {
      hitBox.y = collisionTilePosition.y - GameConfig.TILE_SIZE;
    }
  }
}
