package fr.LaurentFE.pacManCloneAgain.model.entities;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality.GhostPersonality;
import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import fr.LaurentFE.pacManCloneAgain.model.map.TileType;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Ghost {

  private final Color color;
  private Orientation orientation;
  private final Rectangle hitBox;
  private final GhostPersonality chasePersonality;
  private final GameMap gameMap;
  private GhostState state;
  private long chaseNanoTimeStart;
  private long scatterNanoTimeStart;
  private long frightenedNanoTimeStart;
  private TileIndex lastCrossroadTile;
  private final TileIndex scatterTargetTile;
  private final TileIndex eatenTargetTile;
  private final int moveSpeed;

  public Ghost(final Position startingPosition, final Orientation startingOrientation,
      final Color color, final GhostPersonality ghostPersonality, final GameMap gameMap,
      final TileIndex scatterTargetTile, final int moveSpeed) {
    hitBox = new Rectangle(startingPosition.x, startingPosition.y, GameConfig.TILE_SIZE,
        GameConfig.TILE_SIZE);
    orientation = startingOrientation;
    this.color = color;
    this.chasePersonality = ghostPersonality;
    this.gameMap = gameMap;
    state = GhostState.CHASE;
    chaseNanoTimeStart = System.nanoTime();
    scatterNanoTimeStart = 0;
    frightenedNanoTimeStart = 0;
    lastCrossroadTile = new TileIndex(0, 0);
    this.scatterTargetTile = scatterTargetTile;
    eatenTargetTile = gameMap.getGhostHouse();
    this.moveSpeed = moveSpeed;
  }

  public Color getColor() {
    return color;
  }

  public Position getPosition() {
    return new Position(hitBox.x, hitBox.y);
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public GhostPersonality getChasePersonality() {
    return chasePersonality;
  }

  public void update() {
    updateState();
    if (!mustChangeDirection()) {
      updatePosition();
    }
  }

  private void updateState() {
    final long currentTime = System.nanoTime();
    if (state == GhostState.CHASE
        && currentTime - chaseNanoTimeStart >= GameConfig.CHASE_NANO_TIME_DURATION) {
      setState(GhostState.SCATTER);
      scatterNanoTimeStart = currentTime;
    } else if (state == GhostState.SCATTER
        && currentTime - scatterNanoTimeStart >= GameConfig.SCATTER_NANO_TIME_DURATION) {
      setState(GhostState.CHASE);
      chaseNanoTimeStart = currentTime;
    } else if (state == GhostState.FRIGHTENED
        && currentTime - frightenedNanoTimeStart >= GameConfig.FRIGHTENED_NANO_TIME_DURATION) {
      setState(GhostState.CHASE);
      chaseNanoTimeStart = currentTime;
    } else if (state == GhostState.EATEN
        && gameMap.getTile(new Position(hitBox.x, hitBox.y).toTileIndex()) == TileType.GHOSTHOUSE) {
      setState(GhostState.CHASE);
      chaseNanoTimeStart = currentTime;
      orientation = Orientation.UP;
      lastCrossroadTile = new TileIndex(0, 0);
    }
  }

  public void setState(final GhostState ghostState) {
    if (ghostState == GhostState.FRIGHTENED && canBeFrightened()
        || ghostState == GhostState.EATEN && canBeEaten()
        || ghostState == GhostState.CHASE && canBeRevived()
        || ghostState == GhostState.CHASE && canChase()
        || ghostState == GhostState.SCATTER && canScatter()) {
      if (ghostState == GhostState.FRIGHTENED) {
        frightenedNanoTimeStart = System.nanoTime();
      } else if (ghostState == GhostState.SCATTER) {
        scatterNanoTimeStart = System.nanoTime();
      } else if (ghostState == GhostState.CHASE) {
        chaseNanoTimeStart = System.nanoTime();
      }
      state = ghostState;
    }
  }

  private boolean canBeFrightened() {
    return state != GhostState.EATEN;
  }

  private boolean canBeEaten() {
    return state == GhostState.FRIGHTENED;
  }

  private boolean canBeRevived() {
    return state == GhostState.EATEN;
  }

  private boolean canChase() {
    return state == GhostState.SCATTER || state == GhostState.FRIGHTENED;
  }

  private boolean canScatter() {
    return state == GhostState.CHASE;
  }

  private boolean mustChangeDirection() {
    Orientation nextOrientation = getNextMovementOrientation();
    if (nextOrientation == orientation) {
      return false;
    }

    if (canGetIntoPath(nextOrientation)) {
      if (isCurrentTileACrossroad()) {
        lastCrossroadTile = new Position(hitBox.x, hitBox.y).toTileIndex();
      }
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
    final TileIndex directionModifier = switch (nextOrientation) {
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

  private boolean isCurrentTileACrossroad() {
    final TileIndex tileAbovePosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileAbove();
    final TileIndex tileOnLeftPosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileOnLeft();
    final TileIndex tileBelowPosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileBelow();
    final TileIndex tileOnRightPosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileOnRight();

    int possibleDirections = 0;
    if (canGoThroughTile(tileAbovePosition)) {
      possibleDirections++;
    }
    if (canGoThroughTile(tileOnLeftPosition)) {
      possibleDirections++;
    }
    if (canGoThroughTile(tileBelowPosition)) {
      possibleDirections++;
    }
    if (canGoThroughTile(tileOnRightPosition)) {
      possibleDirections++;
    }

    return possibleDirections > 2;
  }

  protected void updatePosition() {
    move();
    checkForWallCollisions();
  }

  public void move() {
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

  private void checkForWallCollisions() {
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
    } else if (!canGoThroughTile(upperRightTile)) {
      bumpOutOfCollision(upperRightTile);
    } else if (!canGoThroughTile(lowerLeftTile)) {
      bumpOutOfCollision(lowerLeftTile);
    } else if (!canGoThroughTile(lowerRightTile)) {
      bumpOutOfCollision(lowerRightTile);
    }
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

  private Orientation getNextMovementOrientation() {
    if (!isFacingAWall()
        && alreadyDecidedAtThisCrossroad()) {
      return orientation;
    }

    return switch (state) {
      case CHASE -> chasePersonality.getNextMovementOrientation();
      case SCATTER -> getNextScatterMovementOrientation();
      case FRIGHTENED -> getNextFrightenedMovementOrientation();
      case EATEN -> getNextEatenMovementOrientation();
    };
  }

  private boolean isFacingAWall() {
    final TileIndex directionModifier = switch (orientation) {
      case UP -> new TileIndex(0, -1);
      case LEFT -> new TileIndex(-1, 0);
      case DOWN -> new TileIndex(0, 1);
      case RIGHT -> new TileIndex(1, 0);
    };
    final TileIndex tileInFront = new Position(hitBox.x, hitBox.y).toTileIndex()
        .add(directionModifier);

    return !canGoThroughTile(tileInFront);
  }

  private boolean alreadyDecidedAtThisCrossroad() {
    final TileIndex currentTile = new Position(hitBox.x, hitBox.y).toTileIndex();
    return lastCrossroadTile.x == currentTile.x
        && lastCrossroadTile.y == currentTile.y;
  }

  public Orientation getNextScatterMovementOrientation() {
    final TileIndex nextMoveTile = getNextMoveTile(scatterTargetTile);
    return getOrientationToGoToTile(nextMoveTile);
  }

  public Orientation getNextFrightenedMovementOrientation() {
    final ArrayList<TileIndex> consideredMoveTiles = getConsideredMoveTiles();
    if (consideredMoveTiles.size() > 1) {
      consideredMoveTiles.add(getBehindTile());
      final Random random = new Random();
      return getOrientationToGoToTile(
          consideredMoveTiles.get(random.nextInt(consideredMoveTiles.size())));
    }
    if (consideredMoveTiles.isEmpty()) {
      return orientation;
    }
    return getOrientationToGoToTile(consideredMoveTiles.getFirst());
  }

  public TileIndex getBehindTile() {
    final TileIndex currentTile = new Position(hitBox.x, hitBox.y).toTileIndex();
    return switch (orientation) {
      case DOWN -> currentTile.getTileAbove();
      case RIGHT -> currentTile.getTileOnLeft();
      case UP -> currentTile.getTileBelow();
      case LEFT -> currentTile.getTileOnRight();
    };
  }

  public Orientation getNextEatenMovementOrientation() {
    final TileIndex currentTile = new Position(hitBox.x, hitBox.y).toTileIndex();

    if (currentTile.x != eatenTargetTile.x || currentTile.y != eatenTargetTile.y) {
      return getOrientationToGoToTile(getNextMoveTile(eatenTargetTile));
    }
    return Orientation.DOWN;
  }

  public TileIndex getNextMoveTile(TileIndex targetTile) {
    TileIndex finalTile = new TileIndex(0, 0);
    int squaredDist = Integer.MAX_VALUE;
    for (TileIndex consideredTile : getConsideredMoveTiles()) {
      final int relativeXDist = targetTile.x - consideredTile.x;
      final int relativeYDist = targetTile.y - consideredTile.y;
      final int squaredDistanceToTarget =
          relativeXDist * relativeXDist + relativeYDist * relativeYDist;
      if (squaredDistanceToTarget < squaredDist) {
        squaredDist = squaredDistanceToTarget;
        finalTile = consideredTile;
      }
    }
    return finalTile;
  }

  public ArrayList<TileIndex> getConsideredMoveTiles() {
    final TileIndex tileAbovePosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileAbove();
    final TileIndex tileOnLeftPosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileOnLeft();
    final TileIndex tileBelowPosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileBelow();
    final TileIndex tileOnRightPosition = new Position(hitBox.x, hitBox.y).toTileIndex()
        .getTileOnRight();

    final ArrayList<TileIndex> consideredMoveTiles = new ArrayList<>();
    if (orientation != Orientation.DOWN && canGoThroughTile(tileAbovePosition)) {
      consideredMoveTiles.add(tileAbovePosition);
    }
    if (orientation != Orientation.RIGHT && canGoThroughTile(tileOnLeftPosition)) {
      consideredMoveTiles.add(tileOnLeftPosition);
    }
    if (orientation != Orientation.UP && canGoThroughTile(tileBelowPosition)) {
      consideredMoveTiles.add(tileBelowPosition);
    }
    if (orientation != Orientation.LEFT && canGoThroughTile(tileOnRightPosition)) {
      consideredMoveTiles.add(tileOnRightPosition);
    }

    return consideredMoveTiles;
  }

  public boolean canGoThroughTile(final TileIndex tileIndex) {
    return gameMap.getTile(tileIndex) == TileType.PATH
        || gameMap.getTile(tileIndex) == TileType.GHOSTHOUSE
        || (gameMap.getTile(tileIndex) == TileType.DOOR
        && state == GhostState.EATEN)
        || (gameMap.getTile(tileIndex) == TileType.DOOR
        && state == GhostState.CHASE
        && orientation != Orientation.DOWN);
  }

  public Orientation getOrientationToGoToTile(final TileIndex tile) {
    final TileIndex currentTile = new Position(hitBox.x, hitBox.y).toTileIndex();
    if (tile.x == currentTile.x && tile.y < currentTile.y) {
      return Orientation.UP;
    } else if (tile.x < currentTile.x && tile.y == currentTile.y) {
      return Orientation.LEFT;
    } else if (tile.x == currentTile.x && tile.y > currentTile.y) {
      return Orientation.DOWN;
    } else if (tile.x > currentTile.x && tile.y == currentTile.y) {
      return Orientation.RIGHT;
    } else {
      return orientation;
    }
  }
}
