package fr.LaurentFE.pacManCloneAgain;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.view.GameFrame;

public class Application {

  public static void main(String[] args) {
    GameState gs = new GameState();
    gs.gameMap.loadMap(GameConfig.DEFAULT_MAP_PATH);
    GameFrame gf;
    if(gs.gameMap.isUsable()) {
      gs.startGame();
      gf = new GameFrame(gs);
    }
  }
}