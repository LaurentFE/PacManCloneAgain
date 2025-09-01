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
    if (gameState.startGame()) {
      gameFrame = new GameFrame(gameState);
      startGameThread();
    } else {
      throw new RuntimeException("Tried GameLogic.startGame() when game is already running");
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
    for (Ghost ghost : gameState.ghosts) {
      ghost.update();
    }
    if (gameState.pellets.isEmpty()) {
      gameFrame.closeOnVictory();
      if (gameState.stopGame()) {
        stopGameThread();
      } else {
        throw new RuntimeException("Tried GameLogic.stopGameThread() when game is not running");
      }
    }
    checkCollisionWithPellets();
    gameFrame.repaint();
  }
}
