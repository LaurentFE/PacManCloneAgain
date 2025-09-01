package fr.LaurentFE.pacManCloneAgain;

import fr.LaurentFE.pacManCloneAgain.controller.GameLogic;
import fr.LaurentFE.pacManCloneAgain.model.GameState;

public class Application {

  public static void main(final String[] args) {
    final GameState gs = new GameState();
    final GameLogic gl = new GameLogic(gs);
    gl.startGame();
  }
}
