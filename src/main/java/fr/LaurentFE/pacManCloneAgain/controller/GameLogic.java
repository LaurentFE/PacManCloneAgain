package fr.LaurentFE.pacManCloneAgain.controller;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Ghost;
import fr.LaurentFE.pacManCloneAgain.model.entities.GhostState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Pellet;
import fr.LaurentFE.pacManCloneAgain.view.GameFrame;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class GameLogic implements Runnable {

  private Thread gameThread;
  private final GameState gameState;
  private GameFrame gameFrame;

  public GameLogic(final GameState gameState) {
    this.gameState = gameState;
  }

  public void startGame() {
    if (gameState.gameMap.isUsable()) {
      if (gameState.startGame()) {
        gameFrame = new GameFrame(gameState);
        startGameThread();
      } else {
        throw new RuntimeException("Tried GameLogic.startGame() when game is already running");
      }
    } else {
      throw new RuntimeException(
          "Tried GameLogic.startGame() when gameMap is not in a usable state.");
    }
  }

  private void startGameThread() {
    if (gameThread != null) {
      return;
    }
    gameThread = new Thread(this);
    gameThread.start();
  }

  private void stopGameThread() {
    gameThread = null;
  }

  @Override
  public void run() {
    long lastUpdateTimeNanoSec = System.nanoTime();

    while (gameThread != null) {
      if (!gameFrame.isDisplayable()) {
        if (gameState.stopGame()) {
          stopGameThread();
        } else {
          throw new RuntimeException("Tried GameLogic.stopGameThread() when game is not running");
        }
      }

      final long currentTimeNanoSec = System.nanoTime();

      if (currentTimeNanoSec >= lastUpdateTimeNanoSec + GameConfig.DRAW_INTERVAL_NANOSEC) {
        update();
        lastUpdateTimeNanoSec = currentTimeNanoSec;
      }
    }
  }

  private void checkCollisionWithGhosts() {
    for (Ghost ghost : gameState.ghosts) {
      if (ghostCollidesWithPacMan(ghost)) {
        if (ghost.getState() == GhostState.FRIGHTENED) {
          ghost.setState(GhostState.EATEN);
          gameState.eatenGhosts++;
          gameState.score +=
              GameConfig.DEFAULT_GHOST_SCORE * (int) Math.pow(2, gameState.eatenGhosts);
        } else if (ghost.getState() != GhostState.EATEN) {
          gameState.pacMan.kill();
          return;
        }
      }
    }
  }

  private boolean ghostCollidesWithPacMan(final Ghost ghost) {
    final Rectangle ghostHitBox = ghost.getHitBox();
    final Rectangle pacManHitBox = gameState.pacMan.getHitBox();
    final Rectangle collisionBox = ghostHitBox.intersection(pacManHitBox);
    if (collisionBox.height >= 0 && collisionBox.width >= 0) {
      final double collisionBoxArea = collisionBox.height * collisionBox.width;
      final double tileArea = GameConfig.TILE_SIZE * GameConfig.TILE_SIZE;
      return collisionBoxArea / tileArea >= 0.3;
    }
    return false;
  }

  private void checkCollisionWithPellets() {
    if (gameState.pellets != null) {
      final Set<Pellet> eatenPellets = new HashSet<>();
      for (Pellet pellet : gameState.pellets) {
        final Rectangle pelletHitBox = pellet.getHitBox();
        final Rectangle pacManHitBox = gameState.pacMan.getHitBox();
        if (pelletHitBox.intersection(pacManHitBox).getSize().equals(pelletHitBox.getSize())) {
          eatenPellets.add(pellet);
          gameState.score += pellet.getScore();
          if (pellet.isPowerPellet()) {
            frightenGhosts();
          }
        }
      }
      for (Pellet pellet : eatenPellets) {
        gameState.pellets.remove(pellet);
      }
    }
  }

  private void frightenGhosts() {
    gameState.eatenGhosts = 0;
    for (Ghost ghost : gameState.ghosts) {
      ghost.setState(GhostState.FRIGHTENED);
    }
  }

  private void update() {
    gameState.pacMan.update(gameFrame.getNextOrientation());
    if (gameState.pacMan.isAlive()) {
      for (Ghost ghost : gameState.ghosts) {
        ghost.update();
      }
      checkCollisionWithGhosts();
      if (gameState.pellets.isEmpty()) {
        gameFrame.closeOnVictory();
        if (gameState.stopGame()) {
          stopGameThread();
        } else {
          throw new RuntimeException("Tried GameLogic.stopGameThread() when game is not running");
        }
      }
      checkCollisionWithPellets();
    }
    if (gameState.pacMan.isDeathAnimationFinished()) {
      if (gameState.pacMan.getLives() == 0) {
        gameFrame.closeOnDeath();
        if (gameState.stopGame()) {
          stopGameThread();
        } else {
          throw new RuntimeException("Tried GameLogic.stopGameThread() when game is not running");
        }
      } else {
        gameState.resetLevel();
        gameFrame.resetUserInput();
      }
    }
    gameFrame.repaint();
  }
}
