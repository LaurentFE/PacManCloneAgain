package fr.LaurentFE.pacManCloneAgain.view;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import fr.LaurentFE.pacManCloneAgain.model.map.TileType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

  private final GameState gameState;

  public GamePanel(final GameState gameState) {
    this.gameState = gameState;
    setPreferredSize(new Dimension(gameState.gameMap.getMapWidthTile() * GameConfig.TILE_SIZE,
        gameState.gameMap.getMapHeightTile() * GameConfig.TILE_SIZE));
    setBackground(Color.BLACK);
    setDoubleBuffered(true);
    setFocusable(true);
  }

  public void paintComponent(final Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    drawMap(g2d);
  }

  private void drawMap(final Graphics2D g2d) {
    for (int y = 0; y < gameState.gameMap.getMapHeightTile(); y++) {
      for (int x = 0; x < gameState.gameMap.getMapWidthTile(); x++) {
        final TileType currentTile = gameState.gameMap.getTile(new TileIndex(x, y));
        if (currentTile == TileType.PATH
            || currentTile == TileType.OUTOFBOUNDS) {
          g2d.setColor(Color.BLACK);
          g2d.fillRect(x * GameConfig.TILE_SIZE,
              y * GameConfig.TILE_SIZE,
              GameConfig.TILE_SIZE,
              GameConfig.TILE_SIZE);
        } else if (currentTile == TileType.DOOR || currentTile == TileType.DECORATIVEDOOR) {
          g2d.setColor(Color.PINK);
          g2d.fillRect(x * GameConfig.TILE_SIZE,
              y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + GameConfig.TILE_SIZE / 4,
              GameConfig.TILE_SIZE,
              GameConfig.TILE_SIZE / 8);
        } else if (currentTile != TileType.UNDEFINED) {
          drawWallShape(x, y, g2d);
        }
      }
    }
  }

  private void drawWallShape(final int x, final int y, final Graphics2D g2d) {
    final TileType tileAbove = gameState.gameMap.getTile(new TileIndex(x, y - 1));
    final TileType tileOnLeft = gameState.gameMap.getTile(new TileIndex(x - 1, y));
    final TileType tileBelow = gameState.gameMap.getTile(new TileIndex(x, y + 1));

    g2d.setColor(Color.BLUE);
    switch (gameState.gameMap.getTile(new TileIndex(x, y))) {
      case DOUBLEHORIZONTALWALL -> drawDoubleHorizontalWall(
          tileAbove == TileType.PATH,
          g2d,
          x,
          y);
      case DOUBLEVERTICALWALL -> drawDoubleVerticalWall(
          tileOnLeft == TileType.PATH,
          g2d,
          x,
          y);
      case DOUBLEOUTERDOWNRIGHTCORNER -> drawDoubleOuterDownRightCorner(
          tileAbove == TileType.PATH,
          g2d,
          x,
          y);
      case DOUBLEOUTERDOWNLEFTCORNER -> drawDoubleOuterDownLeftCorner(
          tileAbove == TileType.PATH,
          g2d,
          x,
          y);
      case DOUBLEOUTERUPLEFTCORNER -> drawDoubleOuterUpLeftCorner(
          tileBelow == TileType.PATH,
          g2d,
          x,
          y);
      case DOUBLEOUTERUPRIGHTCORNER -> drawDoubleOuterUpRightCorner(
          tileBelow == TileType.PATH,
          g2d,
          x,
          y);
      case DOUBLEINNERDOWNRIGHTCORNER -> drawDoubleInnerDownRightCorner(
          tileAbove == TileType.OUTOFBOUNDS,
          g2d,
          x,
          y);
      case DOUBLEINNERDOWNLEFTCORNER -> drawDoubleInnerDownLeftCorner(
          tileAbove == TileType.OUTOFBOUNDS,
          g2d,
          x,
          y);
      case DOUBLEINNERUPLEFTCORNER -> drawDoubleInnerUpLeftCorner(
          tileAbove == TileType.OUTOFBOUNDS,
          g2d,
          x,
          y);
      case DOUBLEINNERUPRIGHTCORNER -> drawDoubleInnerUpRightCorner(
          tileAbove == TileType.OUTOFBOUNDS,
          g2d,
          x,
          y);
      case SIMPLEHORIZONTALWALL -> drawSimpleHorizontalWall(
          tileAbove == TileType.PATH,
          g2d,
          x,
          y);
      case SIMPLEVERTICALWALL -> drawSimpleVerticalWall(
          tileOnLeft == TileType.PATH,
          g2d,
          x,
          y);
      case SIMPLEOUTERDOWNRIGHTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2,
              y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2,
              GameConfig.TILE_SIZE - 2,
              GameConfig.TILE_SIZE - 2,
              180,
              -90);
      case SIMPLEOUTERDOWNLEFTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2,
              GameConfig.TILE_SIZE - 2,
              GameConfig.TILE_SIZE - 2,
              0,
              90);
      case SIMPLEOUTERUPLEFTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              GameConfig.TILE_SIZE - 2,
              GameConfig.TILE_SIZE - 2,
              0,
              -90);
      case SIMPLEOUTERUPRIGHTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2,
              y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              GameConfig.TILE_SIZE - 2,
              GameConfig.TILE_SIZE - 2,
              180,
              90);
      case SIMPLEINNERDOWNRIGHTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2,
              y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2,
              GameConfig.TILE_SIZE + 2,
              GameConfig.TILE_SIZE + 2,
              180,
              -90);
      case SIMPLEINNERDOWNLEFTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2,
              GameConfig.TILE_SIZE + 2,
              GameConfig.TILE_SIZE + 2,
              0,
              90);
      case SIMPLEINNERUPLEFTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              GameConfig.TILE_SIZE + 2,
              GameConfig.TILE_SIZE + 2,
              0,
              -90);
      case SIMPLEINNERUPRIGHTCORNER ->
          g2d.drawArc(x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2,
              y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2,
              GameConfig.TILE_SIZE + 2,
              GameConfig.TILE_SIZE + 2,
              180,
              90);
    }
  }

  private void drawDoubleHorizontalWall(final boolean pathAbove, final Graphics2D g2d, final int x,
      final int y) {
    int y1;
    int y2;
    if (pathAbove) {
      y1 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
      y2 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE - 1;
    } else {
      y1 = y * GameConfig.TILE_SIZE;
      y2 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    }
    g2d.drawLine(x * GameConfig.TILE_SIZE, y1, (x + 1) * GameConfig.TILE_SIZE, y1);
    g2d.drawLine(x * GameConfig.TILE_SIZE, y2, (x + 1) * GameConfig.TILE_SIZE, y2);
  }

  private void drawDoubleVerticalWall(final boolean pathLeft, final Graphics2D g2d, final int x,
      final int y) {
    int x1;
    int x2;
    if (pathLeft) {
      x1 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
      x2 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE - 1;
    } else {
      x1 = x * GameConfig.TILE_SIZE;
      x2 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    }
    g2d.drawLine(x1, y * GameConfig.TILE_SIZE, x1, (y + 1) * GameConfig.TILE_SIZE);
    g2d.drawLine(x2, y * GameConfig.TILE_SIZE, x2, (y + 1) * GameConfig.TILE_SIZE);
  }

  private void drawDoubleOuterDownRightCorner(final boolean pathAbove, final Graphics2D g2d,
      final int x, final int y) {
    if (pathAbove) {
      final int x1 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
      final int y1 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE,
          GameConfig.TILE_SIZE,
          90,
          90);
    } else {
      final int x1 = x * GameConfig.TILE_SIZE;
      final int x2 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
      final int y1 = y * GameConfig.TILE_SIZE;
      final int y2 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE * 2,
          GameConfig.TILE_SIZE * 2,
          90,
          90);
      g2d.drawArc(x2,
          y2,
          GameConfig.TILE_SIZE + 2,
          GameConfig.TILE_SIZE + 2,
          90,
          90);
    }
  }

  private void drawDoubleOuterDownLeftCorner(final boolean pathAbove, final Graphics2D g2d,
      final int x, final int y) {
    if (pathAbove) {
      final int x1 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2 - 2;
      final int y1 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE,
          GameConfig.TILE_SIZE,
          0,
          90);
    } else {
      final int x1 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE;
      final int x2 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2;
      final int y1 = y * GameConfig.TILE_SIZE;
      final int y2 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE * 2,
          GameConfig.TILE_SIZE * 2,
          0,
          90);
      g2d.drawArc(x2,
          y2,
          GameConfig.TILE_SIZE + 2,
          GameConfig.TILE_SIZE + 2,
          0,
          90);
    }
  }

  private void drawDoubleOuterUpLeftCorner(final boolean pathAbove, final Graphics2D g2d,
      final int x, final int y) {
    if (pathAbove) {
      final int x1 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2 - 2;
      final int y1 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2 - 2;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE,
          GameConfig.TILE_SIZE,
          0,
          -90);
    } else {
      final int x1 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE;
      final int x2 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2;
      final int y1 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE - 1;
      final int y2 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE * 2,
          GameConfig.TILE_SIZE * 2,
          0,
          -90);
      g2d.drawArc(x2,
          y2,
          GameConfig.TILE_SIZE + 2,
          GameConfig.TILE_SIZE + 2,
          0,
          -90);
    }
  }

  private void drawDoubleOuterUpRightCorner(final boolean pathBelow, final Graphics2D g2d,
      final int x, final int y) {
    if (pathBelow) {
      final int x1 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
      final int y1 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2 - 2;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE,
          GameConfig.TILE_SIZE,
          -90,
          -90);
    } else {
      final int x1 = x * GameConfig.TILE_SIZE;
      final int x2 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
      final int y1 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE - 1;
      final int y2 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2 - 1;
      g2d.drawArc(x1,
          y1,
          GameConfig.TILE_SIZE * 2,
          GameConfig.TILE_SIZE * 2,
          -90,
          -90);
      g2d.drawArc(x2,
          y2,
          GameConfig.TILE_SIZE + 2,
          GameConfig.TILE_SIZE + 2,
          -90,
          -90);
    }
  }

  private void drawDoubleInnerDownRightCorner(final boolean pathAbove, final Graphics2D g2d,
      final int x, final int y) {
    final int x1 = x * GameConfig.TILE_SIZE;
    final int x2 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    final int y1 = y * GameConfig.TILE_SIZE;
    final int y2 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    if (pathAbove) {
      g2d.drawLine(x1, y1, x1 + GameConfig.TILE_SIZE, y1);
    } else {
      g2d.drawLine(x1, y1, x1, y1 + GameConfig.TILE_SIZE);
    }
    g2d.drawArc(x2,
        y2,
        GameConfig.TILE_SIZE + 2,
        GameConfig.TILE_SIZE + 2,
        90,
        90);
  }

  private void drawDoubleInnerDownLeftCorner(final boolean outOfBoundsAbove, final Graphics2D g2d,
      final int x, final int y) {
    final int x1 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE - 1;
    final int x2 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2;
    final int y1 = y * GameConfig.TILE_SIZE;
    final int y2 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    if (outOfBoundsAbove) {
      g2d.drawLine(x1 - GameConfig.TILE_SIZE + 1, y1, x1, y1);
    } else {
      g2d.drawLine(x1, y1, x1, y1 + GameConfig.TILE_SIZE);
    }
    g2d.drawArc(x2,
        y2,
        GameConfig.TILE_SIZE + 2,
        GameConfig.TILE_SIZE + 2,
        0,
        90);
  }

  private void drawDoubleInnerUpLeftCorner(final boolean outOfBoundsAbove, final Graphics2D g2d,
      final int x, final int y) {
    final int x1 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE - 1;
    final int x2 = x * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2;
    final int y1 = y * GameConfig.TILE_SIZE;
    final int y2 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2;
    if (outOfBoundsAbove) {
      g2d.drawLine(x1, y1, x1 + GameConfig.TILE_SIZE, y1);
    } else {
      g2d.drawLine(x1, y1, x1, y1 + GameConfig.TILE_SIZE);
    }
    g2d.drawArc(x2,
        y2,
        GameConfig.TILE_SIZE + 2,
        GameConfig.TILE_SIZE + 2,
        0,
        -90);
  }

  private void drawDoubleInnerUpRightCorner(final boolean outOfBoundsAbove, final Graphics2D g2d,
      final int x, final int y) {
    final int x1 = x * GameConfig.TILE_SIZE;
    final int x2 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    final int y1 = y * GameConfig.TILE_SIZE;
    final int y2 = y * GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 2 - 1;
    if (outOfBoundsAbove) {
      g2d.drawLine(x1, y1, x1 + GameConfig.TILE_SIZE, y1);
    } else {
      g2d.drawLine(x1, y1, x1, y1 + GameConfig.TILE_SIZE);
    }
    g2d.drawArc(x2,
        y2,
        GameConfig.TILE_SIZE + 2,
        GameConfig.TILE_SIZE + 2,
        -90,
        -90);
  }

  private void drawSimpleHorizontalWall(final boolean pathAbove, final Graphics2D g2d, final int x,
      final int y) {
    int y1;
    if (pathAbove) {
      y1 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
    } else {
      y1 = y * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    }
    g2d.drawLine(x * GameConfig.TILE_SIZE, y1, (x + 1) * GameConfig.TILE_SIZE, y1);
  }

  private void drawSimpleVerticalWall(final boolean pathLeft, final Graphics2D g2d, final int x,
      final int y) {
    int x1;
    if (pathLeft) {
      x1 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 + 2;
    } else {
      x1 = x * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2 - 2;
    }
    g2d.drawLine(x1, y * GameConfig.TILE_SIZE, x1, (y + 1) * GameConfig.TILE_SIZE);
  }
}
