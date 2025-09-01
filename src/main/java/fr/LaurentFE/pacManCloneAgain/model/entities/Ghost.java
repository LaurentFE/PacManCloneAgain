package fr.LaurentFE.pacManCloneAgain.model.entities;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality.GhostPersonality;
import fr.LaurentFE.pacManCloneAgain.model.map.GameMap;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import fr.LaurentFE.pacManCloneAgain.model.map.TileType;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Ghost extends MovingEntity {

  private final Color color;
  private final GhostPersonality chasePersonality;
  private GhostState state;
  private long chaseNanoTimeStart;
  private long scatterNanoTimeStart;
  private long frightenedNanoTimeStart;
  private TileIndex lastCrossroadTile;
  private final TileIndex scatterTargetTile;
  private final TileIndex eatenTargetTile;

  public Ghost(final Position startingPosition, final Orientation startingOrientation,
      final Color color, final GhostPersonality ghostPersonality, final GameMap gameMap,
      final TileIndex scatterTargetTile, final int moveSpeed) {
    super(startingPosition, startingOrientation, moveSpeed, gameMap);
    this.color = color;
    this.chasePersonality = ghostPersonality;
    state = GhostState.CHASE;
    chaseNanoTimeStart = System.nanoTime();
    scatterNanoTimeStart = 0;
    frightenedNanoTimeStart = 0;
    lastCrossroadTile = new TileIndex(0, 0);
    this.scatterTargetTile = scatterTargetTile;
    eatenTargetTile = gameMap.getGhostHouse();
  }

  public Color getColor() {
    return color;
  }

  public GhostPersonality getChasePersonality() {
    return chasePersonality;
  }

  public GhostState getState() {
    return state;
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
        && gameMap.getTile(getPosition().toTileIndex()) == TileType.GHOSTHOUSE) {
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
    final Orientation nextOrientation = getNextMovementOrientation();
    if (nextOrientation == orientation) {
      return false;
    }

    if (canGetIntoPath(nextOrientation)) {
      if (isCurrentTileACrossroad()) {
        lastCrossroadTile = getPosition().toTileIndex();
      }
      orientation = nextOrientation;
      updatePosition();
      return true;
    }
    return false;
  }

  private boolean isCurrentTileACrossroad() {
    final TileIndex currentTile = getPosition().toTileIndex();
    final TileIndex tileAbovePosition = currentTile.getTileAbove();
    final TileIndex tileOnLeftPosition = currentTile.getTileOnLeft();
    final TileIndex tileBelowPosition = currentTile.getTileBelow();
    final TileIndex tileOnRightPosition = currentTile.getTileOnRight();

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
    final TileIndex tileInFront = getPosition().toTileIndex()
        .add(directionModifier);

    return !canGoThroughTile(tileInFront);
  }

  private boolean alreadyDecidedAtThisCrossroad() {
    final TileIndex currentTile = getPosition().toTileIndex();
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
    final TileIndex currentTile = getPosition().toTileIndex();
    return switch (orientation) {
      case DOWN -> currentTile.getTileAbove();
      case RIGHT -> currentTile.getTileOnLeft();
      case UP -> currentTile.getTileBelow();
      case LEFT -> currentTile.getTileOnRight();
    };
  }

  public Orientation getNextEatenMovementOrientation() {
    final TileIndex currentTile = getPosition().toTileIndex();

    if (currentTile.x != eatenTargetTile.x || currentTile.y != eatenTargetTile.y) {
      return getOrientationToGoToTile(getNextMoveTile(eatenTargetTile));
    }
    return Orientation.DOWN;
  }

  public TileIndex getNextMoveTile(final TileIndex targetTile) {
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
    final TileIndex currentTile = getPosition().toTileIndex();
    final TileIndex tileAbovePosition = currentTile.getTileAbove();
    final TileIndex tileOnLeftPosition = currentTile.getTileOnLeft();
    final TileIndex tileBelowPosition = currentTile.getTileBelow();
    final TileIndex tileOnRightPosition = currentTile.getTileOnRight();

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
    final TileIndex currentTile = getPosition().toTileIndex();
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
