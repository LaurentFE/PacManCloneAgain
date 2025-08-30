package fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality;

import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;

public class Clyde implements GhostPersonality {

  private final GameState gameState;

  public Clyde(final GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public Orientation getNextMovementOrientation() {

    final Ghost clyde = gameState.getClyde();
    if (clyde != null) {
      if (getSquareDistanceFromPacMan() >= 64) {
        final TileIndex targetTile = gameState.pacMan.getPosition().toTileIndex();
        final TileIndex nextMoveTile = clyde.getNextMoveTile(targetTile);

        return clyde.getOrientationToGoToTile(nextMoveTile);
      } else {
        return clyde.getNextScatterMovementOrientation();
      }
    } else {
      throw new RuntimeException(
          "Clyde.getNextMovementOrientation() couldn't find a Clyde instance through GameState.getClyde()");
    }
  }

  private int getSquareDistanceFromPacMan() {
    final TileIndex pacManTile = gameState.pacMan.getPosition().toTileIndex();
    final Ghost clyde = gameState.getClyde();
    if (clyde != null) {
      final TileIndex clydeTile = clyde.getPosition().toTileIndex();

      return (pacManTile.x - clydeTile.x) * (pacManTile.x - clydeTile.x)
          + (pacManTile.y - clydeTile.y) * (pacManTile.y - clydeTile.y);
    } else {
      throw new RuntimeException(
          "Clyde.getSquareDistanceFromPacMan() couldn't find a Clyde instance through GameState.getClyde()");
    }
  }
}
