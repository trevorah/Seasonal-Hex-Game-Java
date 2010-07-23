package graphical.boardPanels;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HexSelector implements MouseListener {

  @Override
  public void mouseClicked(MouseEvent e) {
    ((HexGamePanel) e.getComponent()).click(e.getPoint());
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }
}
