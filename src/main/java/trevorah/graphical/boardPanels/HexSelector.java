package trevorah.graphical.boardPanels;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HexSelector implements MouseListener {

  public void mouseClicked(MouseEvent e) {
    ((HexGamePanel) e.getComponent()).click(e.getPoint());
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
  }
}
