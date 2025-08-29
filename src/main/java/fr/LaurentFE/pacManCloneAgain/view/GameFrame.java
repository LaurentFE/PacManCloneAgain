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

  public GameFrame(final GameState gameState) {
    super("Pac-Man clone");
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

  public Orientation getNextOrientation() {
    return mainDisplay.getNextOrientation();
  }
}
