package fr.LaurentFE.pacManCloneAgain.model.entities;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import java.awt.Color;
import java.awt.Rectangle;

public class Ghost {

  private final Color color;
  private final Orientation orientation;
  private final Rectangle hitBox;

  public Ghost(final Position startingPosition, final Orientation startingOrientation,
      final Color color) {
    hitBox = new Rectangle(startingPosition.x, startingPosition.y, GameConfig.TILE_SIZE,
        GameConfig.TILE_SIZE);
    orientation = startingOrientation;
    this.color = color;
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
}
