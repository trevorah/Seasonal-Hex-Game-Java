package trevorah.graphical;

import trevorah.gameMechanics.SeasonMechanics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TurnViewer extends JPanel implements Runnable {

  Thread animatorThread;
  int delay = 100;
  Color[] colourArray;
  int[] seasonList;
  SeasonMechanics data;
  int player;

  public TurnViewer(SeasonMechanics data, int player) {
    this.player = player;
    this.data = data;
    this.colourArray = data.getColourArray();
    this.seasonList = data.getSubPattern(player);
    this.setPreferredSize(new Dimension(25 * seasonList.length, 25));
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    int panelWidth = this.getSize().width;
    int panelHeight = this.getSize().height;
    int boxWidth = (panelWidth / seasonList.length);

    /*
     * draw each season
     * _____________________
     * |      |      |      |
     * |  s0  |  s1  |  s2  |
     * |______|______|______|
     */

    for (int i = 0; i < seasonList.length; i++) {
      int extra = i * boxWidth;
      int[] x = {extra, extra + boxWidth, extra + boxWidth, extra};
      int[] y = {0, 0, panelHeight, panelHeight};

      GeneralPath seasonBox = new GeneralPath(Path2D.WIND_EVEN_ODD,
              x.length);
      seasonBox.moveTo(x[0], y[0]);
      for (int j = 1; j < x.length; j++)
        seasonBox.lineTo(x[j], y[j]);
      seasonBox.closePath();

      g2.setPaint(colourArray[seasonList[i]]);
      g2.fill(seasonBox);
      g2.setPaint(Color.BLACK);
      g2.draw(seasonBox);
    }

    /*
     * draw the triangle
     *  ______
     *  |    |
     *  | |> |
     *  |____|
     */

    int currentSeason = data.getCurrentPos(player);
    int extra = currentSeason * boxWidth;
    int[] x = {extra + boxWidth / 3, extra + (2 * boxWidth) / 3, extra + boxWidth / 3};
    int[] y = {panelHeight / 3, panelHeight / 2, (2 * panelHeight) / 3};

    GeneralPath playTriangle = new GeneralPath(Path2D.WIND_EVEN_ODD,
            x.length);
    playTriangle.moveTo(x[0], y[0]);
    for (int i = 1; i < x.length; i++)
      playTriangle.lineTo(x[i], y[i]);
    playTriangle.closePath();

    if (data.isThinking(player)) {
      g2.setPaint(Color.BLACK);
      g2.fill(playTriangle);
    }
    g2.setPaint(Color.BLACK);
    g2.draw(playTriangle);

  }

  public void run() {
    // Just to be nice, lower this thread's priority
    // so it can't interfere with other processing going on.
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

    // Remember the starting time.
    long startTime = System.currentTimeMillis();

    // Remember which thread we are.
    Thread currentThread = Thread.currentThread();

    // This is the animation loop.
    while (currentThread == animatorThread) {
      // Advance the animation frame.
      // Display it.
      repaint();

      // Delay depending on how far we are behind.
      try {
        startTime += delay;
        Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
      } catch (InterruptedException e) {
        break;
      }
    }

  }

  public void startAnimation() {
    // Start the animating thread.
    if (animatorThread == null)
      animatorThread = new Thread(this);
    animatorThread.start();
  }

  public void stopAnimation() {
    // Stop the animating thread.
    animatorThread = null;
  }
}
