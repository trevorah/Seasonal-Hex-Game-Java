package trevorah.graphical.boardPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import trevorah.hexBoards.Board;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;

@SuppressWarnings("serial")
public abstract class HexPanel extends JPanel implements Runnable {

  protected Board board = null;
  private Thread animatorThread;
  private int delay = 100;
  private double sm;
  private double lg;
  protected int size;
  protected Point chosenXY;

  public HexPanel(Board board) {
    this.board = board;
    size = this.board.getSize();

    this.setName(board.getName());
    this.setOpaque(false);
    this.setPreferredSize(new Dimension(200, 100));
  }

  public String getDescription() {
    this.setName("test");
    return this.getName();
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    double panelWidth = this.getSize().width;
    double panelHeight = this.getSize().height;
    double dim1 = panelWidth / (6 * size + 2);
    double dim2 = panelHeight / (4 + 3 * (size - 1));
    this.sm = Math.min(dim1, dim2);
    this.lg = 2 * sm;

    drawBorders(g2);
    g2.setColor(Color.BLACK);
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        /*
         * draws each hex individually - not really the best approach as
         * most lines are drawn twice. Easy to implement and works
         * though...
         */
        drawHex(g2, x, y, board.get(x, y));

  }

  private void drawHex(Graphics2D g, int column, int row, int colour) {
    Polygon polygon = calcHexPoly(column, row);
    g.setPaint(getFillColour(column, row));
    g.fill(polygon);
    g.setPaint(Color.BLACK);
    g.draw(polygon);
  }

  /*
   *
   *      x1 x2 x3
   * y1_____|_|_|___
   * y2_____|/|\|___
   * y3_____|_|_|___
   * y4_____|\|/|___
   *        | | |
   */
  protected Polygon calcHexPoly(int column, int row) {
    double xl_double = (2 * lg * column) + (lg * row) + lg;
    int xl = (int) (xl_double);
    int xm = (int) (xl_double + lg);
    int xr = (int) (xl_double + lg * 2);
    double y1_double = row * (lg + sm);
    int y1 = (int) y1_double;
    int y2 = (int) (y1_double + sm);
    int y3 = (int) (y1_double + sm + lg);
    int y4 = (int) (y1_double + sm + lg + sm);
    int[] x = {xm, xr, xr, xm, xl, xl};
    int[] y = {y1, y2, y3, y4, y3, y2};

    return new Polygon(x, y, 6);
  }

  private void drawBorders(Graphics2D g) {

    int xBottomRight = (int) (((size * 3) + 1) * lg);
    int xTopRight = (int) (2 * size * lg + (5 * sm) / 4);
    int xBottomLeft = (int) (size * lg + (3 * sm) / 4);
    int xCentre = xBottomRight / 2;

    int yBottomPoint = (int) (size * (lg + sm) + sm);
    int yCentrePoint = yBottomPoint / 2;

    int[] xLeft = {0, xCentre, xBottomLeft};
    int[] yLeft = {0, yCentrePoint, yBottomPoint};

    int[] xTop = {0, xTopRight, xCentre};
    int[] yTop = {0, 0, yCentrePoint};
    int[] xBottom = {xCentre, xBottomRight, xBottomLeft};
    int[] yBottom = {yCentrePoint, yBottomPoint, yBottomPoint};
    int[] xRight = {xTopRight, xBottomRight, xCentre};
    int[] yRight = {0, yBottomPoint, yCentrePoint};

    Polygon left = new Polygon(xLeft, yLeft, 3);
    Polygon right = new Polygon(xRight, yRight, 3);
    Polygon top = new Polygon(xTop, yTop, 3);
    Polygon bottom = new Polygon(xBottom, yBottom, 3);

    g.setPaint(Color.RED);
    g.fill(top);
    g.fill(bottom);

    g.setPaint(Color.BLUE);
    g.fill(left);
    g.fill(right);

    g.setPaint(Color.BLACK);
    g.draw(top);
    g.draw(bottom);
    g.draw(left);
    g.draw(right);
  }

  public Color getFillColour(int x, int y) {
    int value = board.get(x, y);
    Color returnColour = Color.WHITE;
    switch (value) {
      case Board.RED:
        returnColour = (Color.RED);
        break;
      case Board.BLUE:
        returnColour = (Color.BLUE);
        break;
      default:
        returnColour = (Color.WHITE);
    }
    return returnColour;
  }

  public Color getSeasonColour(int value) {
    Color returnColour = Color.WHITE;
    switch (value) {
      case 0:
        returnColour = (Color.GREEN.brighter());
        break;
      case 1:
        returnColour = (Color.YELLOW.brighter());
        break;
      case 2:
        returnColour = (Color.magenta.brighter());
        break;
      case 3:
        returnColour = (Color.PINK);
        break;
      default:
        returnColour = (Color.WHITE);
    }
    return returnColour;
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
      if (board.hasChanged()) {
        repaint();
        board.changeNoted();
      }
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
