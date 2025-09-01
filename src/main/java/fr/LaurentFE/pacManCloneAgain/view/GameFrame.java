package fr.LaurentFE.pacManCloneAgain.view;

import fr.LaurentFE.pacManCloneAgain.model.GameState;
import fr.LaurentFE.pacManCloneAgain.model.entities.Orientation;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class GameFrame extends JFrame {

  private final GamePanel mainDisplay;
  private final GameState gameState;

  public GameFrame(final GameState gameState) {
    super("Pac-Man clone");
    this.gameState = gameState;
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setResizable(false);
    addWindowClosingListener();
    mainDisplay = new GamePanel(gameState);
    add(mainDisplay, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void addWindowClosingListener() {
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        confirmClose();
      }
    });
  }

  private void confirmClose() {
    int exitValue = JOptionPane.showConfirmDialog(
        null,
        "Are you sure you want to exit ?",
        "Exit",
        JOptionPane.YES_NO_OPTION);
    if (exitValue == JOptionPane.YES_OPTION) {
      dispose();
    }
  }

  public void closeOnVictory() {
    JOptionPane.showMessageDialog(
        null,
        """
            Congratulations !
            You have eaten all the pellets on the map.
            Victory is yours !
            You got a score of :\s""" + gameState.score,
        "VICTORY",
        JOptionPane.INFORMATION_MESSAGE);
    dispose();
  }

  public void closeOnDeath() {
    JOptionPane.showMessageDialog(
        null,
        """
            Oh no !
            This is Game Over.
            You got a score of :\s""" + gameState.score,
        "GAME OVER",
        JOptionPane.INFORMATION_MESSAGE);
    dispose();
  }

  public void resetUserInput() {
    mainDisplay.resetUserInput();
  }

  public Orientation getNextOrientation() {
    return mainDisplay.getNextOrientation();
  }
}
