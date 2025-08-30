package fr.LaurentFE.pacManCloneAgain;

import fr.LaurentFE.pacManCloneAgain.controller.GameLogic;
import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.GameState;

public class Application {

  public static void main(final String[] args) {
    final GameState gs = new GameState();
    gs.gameMap.loadMap(GameConfig.DEFAULT_MAP_PATH);
    if (gs.gameMap.isUsable()) {
      final GameLogic gl = new GameLogic(gs);
      gl.startGame();
    }
  }
}