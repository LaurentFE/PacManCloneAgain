package fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality;

import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;

public class Blinky implements GhostPersonality {

  private final GameState gameState;

  public Blinky(final GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public Orientation getNextMovementOrientation() {
    final TileIndex targetTile = gameState.pacMan.getPosition().toTileIndex();
    final Ghost blinky = gameState.getBlinky();
    if (blinky != null) {
      final TileIndex nextMoveTile = blinky.getNextMoveTile(targetTile);

      return blinky.getOrientationToGoToTile(nextMoveTile);
    } else {
      throw new RuntimeException(
          "Blinky.getNextMovementOrientation() couldn't find a Blinky instance through GameState.getBlinky()");
    }
  }
}
