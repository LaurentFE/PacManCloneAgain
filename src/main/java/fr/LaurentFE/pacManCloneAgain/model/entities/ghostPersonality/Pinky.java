package fr.LaurentFE.pacManCloneAgain.model.entities.ghostPersonality;

import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;

public class Pinky implements GhostPersonality {

  private final GameState gameState;

  public Pinky(final GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public Orientation getNextMovementOrientation() {
    final TileIndex targetTile = gameState.pacMan.getPosition().toTileIndex();
    switch (gameState.pacMan.getOrientation()) {
      case UP -> targetTile.y -= 4;
      case LEFT -> targetTile.x -= 4;
      case DOWN -> targetTile.y +=4;
      case RIGHT -> targetTile.x +=4;
    }
    final Ghost pinky = gameState.getPinky();
    if (pinky != null) {
      final TileIndex nextMoveTile = pinky.getNextMoveTile(targetTile);

      return pinky.getOrientationToGoToTile(nextMoveTile);
    } else {
      throw new RuntimeException(
          "Pinky.getNextMovementOrientation() couldn't find a Pinky instance through GameState.getPinky()");
    }
  }
}
