package trevorah.graphical.boardPanels;

import java.awt.Color;
import trevorah.hexBoards.Board;
import trevorah.hexBoards.ScoreBoard;

@SuppressWarnings("serial")
public class HeatMap extends HexPanel {

  public HeatMap(Board board) {
    super(board);
  }

  @Override
  public Color getFillColour(int x, int y) {
    int value = board.get(x, y);
    return getHeatColour(value);
  }

  private Color getHeatColour(float score) {
    Color color;

    int topScore = ((ScoreBoard) board).getHighestScore();
    int lowScore = ((ScoreBoard) board).getLowestScore();

    score -= lowScore;
    topScore -= lowScore;

    float heat = score / topScore;
    if (heat >= 1) {
      heat = 1;
      color = new Color(heat, heat, heat);
    } else
      color = new Color(heat, heat, heat);
    return color;
  }
}
