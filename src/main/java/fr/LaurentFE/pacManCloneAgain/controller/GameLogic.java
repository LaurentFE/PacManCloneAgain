package fr.LaurentFE.pacManCloneAgain.controller;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.view.GameFrame;

public class GameLogic implements Runnable {

  private Thread gameThread;
  private final GameState gameState;
  private GameFrame gameFrame;

  public GameLogic(final GameState gameState) {
    this.gameState = gameState;
  }

  public void startGame() {
    if (!gameState.isRunning()) {
      gameState.startGame();
      gameFrame = new GameFrame(gameState);
      startGameThread();
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
        gameState.stopGame();
        stopGameThread();
      }

      final long currentTimeNanoSec = System.nanoTime();

      if (currentTimeNanoSec >= lastUpdateTimeNanoSec + GameConfig.DRAW_INTERVAL_NANOSEC) {
        update();
        lastUpdateTimeNanoSec = currentTimeNanoSec;
      }
    }
  }

  private void update() {
    gameState.pacMan.update(gameFrame.getNextOrientation());
    gameFrame.repaint();
  }
}
