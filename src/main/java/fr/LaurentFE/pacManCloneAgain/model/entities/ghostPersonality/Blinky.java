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
    final TileIndex nextMoveTile = blinky.getNextMoveTile(targetTile);

    return blinky.getOrientationToGoToTile(nextMoveTile);
  }
}
