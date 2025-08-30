package fr.LaurentFE.pacManCloneAgain.view;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import fr.LaurentFE.pacManCloneAgain.model.map.Position;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import fr.LaurentFE.pacManCloneAgain.model.map.TileType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

  private final GameState gameState;
  private final GameKeyHandler gameKeyHandler;

  public GamePanel(final GameState gameState) {
    this.gameState = gameState;
    setPreferredSize(new Dimension(gameState.gameMap.getMapWidthTile() * GameConfig.TILE_SIZE,
        gameState.gameMap.getMapHeightTile() * GameConfig.TILE_SIZE));
    setBackground(Color.BLACK);
    setDoubleBuffered(true);
    gameKeyHandler = new GameKeyHandler();
    addKeyListener(gameKeyHandler);
    setFocusable(true);
  }

  public void paintComponent(final Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    drawMap(g2d);
    drawPacMan(g2d);
    drawGhosts(g2d);
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

  private void drawPacMan(final Graphics2D g2d) {
    final int mouthStartAngle;
    g2d.setColor(Color.YELLOW);
    if (gameState.pacMan.getOrientation() == Orientation.UP) {
      mouthStartAngle = 90;
    } else if (gameState.pacMan.getOrientation() == Orientation.RIGHT) {
      mouthStartAngle = 0;
    } else if (gameState.pacMan.getOrientation() == Orientation.DOWN) {
      mouthStartAngle = -90;
    } else {
      mouthStartAngle = 180;
    }
    g2d.fillArc(gameState.pacMan.getPosition().x,
        gameState.pacMan.getPosition().y,
        GameConfig.TILE_SIZE,
        GameConfig.TILE_SIZE,
        mouthStartAngle + gameState.pacMan.getCurrentMouthAngle() / 2,
        360 - gameState.pacMan.getCurrentMouthAngle());
  }

  private void drawGhosts(final Graphics2D g2d) {
    for (Ghost ghost : gameState.ghosts) {
      drawGhost(g2d, ghost);
    }
  }

  private void drawGhost(final Graphics2D g2d, final Ghost ghost) {
    g2d.setColor(ghost.getColor());
    drawGhostBody(g2d, ghost);
    drawGhostSkirt(g2d, ghost);
    drawGhostEyes(g2d, ghost);
  }

  private void drawGhostBody(final Graphics2D g2d, final Ghost ghost) {
    g2d.fillArc(ghost.getPosition().x + GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + GameConfig.TILE_SIZE / 16,
        GameConfig.TILE_SIZE - GameConfig.TILE_SIZE / 8,
        3 * GameConfig.TILE_SIZE / 4,
        0,
        180);
    g2d.fillRect(ghost.getPosition().x + GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + 3 * GameConfig.TILE_SIZE / 8,
        14 * GameConfig.TILE_SIZE / 16,
        7 * GameConfig.TILE_SIZE / 16);
  }

  private void drawGhostSkirt(final Graphics2D g2d, final Ghost ghost) {
    final int[] x1 = new int[]{ghost.getPosition().x + GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + 3 * GameConfig.TILE_SIZE / 16};
    final int[] x2 = new int[]{ghost.getPosition().x + 4 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + 6 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + 6 * GameConfig.TILE_SIZE / 16};
    final int[] x3 = new int[]{ghost.getPosition().x + 10 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + 10 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + 12 * GameConfig.TILE_SIZE / 16};
    final int[] x4 = new int[]{ghost.getPosition().x + 13 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + 15 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().x + 15 * GameConfig.TILE_SIZE / 16};
    final int[] y1 = new int[]{ghost.getPosition().y + 13 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + 15 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + 13 * GameConfig.TILE_SIZE / 16};
    final int[] y2 = new int[]{ghost.getPosition().y + 13 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + 13 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + 15 * GameConfig.TILE_SIZE / 16};
    g2d.fillPolygon(x1, y1, 3);
    g2d.fillPolygon(x2, y2, 3);
    g2d.fillRect(x2[2], y1[0], GameConfig.TILE_SIZE / 16, 2 * GameConfig.TILE_SIZE / 16);
    g2d.fillRect(x2[2] + 3 * GameConfig.TILE_SIZE / 16, y1[0], GameConfig.TILE_SIZE / 16,
        2 * GameConfig.TILE_SIZE / 16);
    g2d.fillPolygon(x3, y1, 3);
    g2d.fillPolygon(x4, y2, 3);
  }

  private void drawGhostEyes(final Graphics2D g2d, final Ghost ghost) {
    g2d.setColor(Color.WHITE);
    final int eyeHeight = 6 * GameConfig.TILE_SIZE / 16;
    final int eyeWidth = 4 * GameConfig.TILE_SIZE / 16;
    final int pupilSize = 2 * GameConfig.TILE_SIZE / 16;
    final Position leftEyePosition = new Position(
        ghost.getPosition().x + 4 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + 4 * GameConfig.TILE_SIZE / 16);
    final Position rightEyePosition = new Position(GameConfig.TILE_SIZE / 16 + eyeWidth, 0).add(
        leftEyePosition);
    final Position leftPupilPosition = new Position(
        ghost.getPosition().x + 6 * GameConfig.TILE_SIZE / 16,
        ghost.getPosition().y + 6 * GameConfig.TILE_SIZE / 16);
    final Position rightPupilPosition = new Position(GameConfig.TILE_SIZE / 16 + eyeWidth, 0).add(
        leftPupilPosition);
    final Position[] orientationOffsets = getEyeAndPupilOrientationOffset(ghost.getOrientation());

    leftEyePosition.add(orientationOffsets[0]);
    leftPupilPosition.add(orientationOffsets[1]);
    rightEyePosition.add(orientationOffsets[0]);
    rightPupilPosition.add(orientationOffsets[1]);

    g2d.fillArc(leftEyePosition.x,
        leftEyePosition.y,
        eyeWidth, eyeHeight, 0, 360);
    g2d.fillArc(rightEyePosition.x,
        rightEyePosition.y,
        eyeWidth, eyeHeight, 0, 360);
    g2d.setColor(Color.BLUE);
    g2d.fillArc(leftPupilPosition.x,
        leftPupilPosition.y,
        pupilSize, pupilSize, 0, 360);
    g2d.fillArc(rightPupilPosition.x,
        rightPupilPosition.y,
        pupilSize, pupilSize, 0, 360);
  }

  private Position[] getEyeAndPupilOrientationOffset(final Orientation orientation) {
    final Position eyeOrientationOffset;
    final Position pupilOrientationOffset;
    if (orientation == Orientation.UP) {
      eyeOrientationOffset = new Position(-GameConfig.TILE_SIZE / 16, -GameConfig.TILE_SIZE / 8);
      pupilOrientationOffset = new Position(-GameConfig.TILE_SIZE / 8, -GameConfig.TILE_SIZE / 4);
    } else if (orientation == Orientation.LEFT) {
      eyeOrientationOffset = new Position(-GameConfig.TILE_SIZE / 8, 0);
      pupilOrientationOffset = new Position(-GameConfig.TILE_SIZE / 4, 0);
    } else if (orientation == Orientation.DOWN) {
      eyeOrientationOffset = new Position(-GameConfig.TILE_SIZE / 16, GameConfig.TILE_SIZE / 16);
      pupilOrientationOffset = new Position(-2 * GameConfig.TILE_SIZE / 16,
          3 * GameConfig.TILE_SIZE / 16);
    } else {
      eyeOrientationOffset = new Position(0, 0);
      pupilOrientationOffset = new Position(0, 0);
    }
    return new Position[]{eyeOrientationOffset, pupilOrientationOffset};
  }

  public Orientation getNextOrientation() {
    return gameKeyHandler.getNextOrientation();
  }
}
