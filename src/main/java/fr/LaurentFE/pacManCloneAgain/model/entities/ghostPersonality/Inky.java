package fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality;

import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;

public class Inky implements GhostPersonality {

  private final GameState gameState;

  public Inky(final GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public Orientation getNextMovementOrientation() {
    final TileIndex pacManTile = gameState.pacMan.getPosition().toTileIndex();
    switch (gameState.pacMan.getOrientation()) {
      case UP -> pacManTile.y -= 2;
      case LEFT -> pacManTile.x -= 2;
      case DOWN -> pacManTile.y += 2;
      case RIGHT -> pacManTile.x += 2;
    }
    final Ghost blinky = gameState.getBlinky();
    if (blinky != null) {
      final TileIndex blinkyTile = blinky.getPosition().toTileIndex();
      final TileIndex targetTile = new TileIndex(
          blinkyTile.x - 2 * (blinkyTile.x - pacManTile.x),
          blinkyTile.y - 2 * (blinkyTile.y - pacManTile.y));

      final Ghost inky = gameState.getInky();
      if (inky != null) {
        final TileIndex nextMoveTile = inky.getNextMoveTile(
            new TileIndex(targetTile.x, targetTile.y));

        return inky.getOrientationToGoToTile(nextMoveTile);
      } else {
        throw new RuntimeException(
            "Inky.getNextMovementOrientation() couldn't find an Inky instance through GameState.getInky()");
      }
    } else {
      throw new RuntimeException(
          "Inky.getNextMovementOrientation() couldn't find a Blinky instance through GameState.getBlinky()");
    }
  }
}
