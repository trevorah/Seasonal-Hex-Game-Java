package trevorah.graphical.boardPanels;

import trevorah.players.Player;
import trevorah.hexBoards.ScoreBoard;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class HexGroupPanel extends JTabbedPane {

  Player player;

  public HexGroupPanel(Player player) {
    super();
    this.player = player;
    setup();
  }

  private void setup() {

    for (int i = 0; i < player.getAuxBoards().size(); i++) {
      HexPanel panel;
      if (player.getAuxBoards().get(i) instanceof ScoreBoard)
        panel = new HeatMap((ScoreBoard) player.getAuxBoards().get(i));
      else
        panel = new HexDisplayOnlyPanel(player.getAuxBoards().get(i));
      this.addTab(panel.getName(), panel);
      panel.startAnimation();
    }
  }
}
