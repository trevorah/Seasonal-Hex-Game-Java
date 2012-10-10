package trevorah.hexBoards;

import trevorah.gameMechanics.SeasonMechanics;
import java.util.Random;

public class OpenBoard extends GameBoard {

  public OpenBoard(int size, SeasonMechanics seasonPicker, BoardData data) {
    super(size, seasonPicker, data);
  }

  public void set(int x, int y, int value) {
    data.set(x, y, value);
    changeOccured = true;
  }

  public void set_noNewLinks(int x, int y, int value) {
    data.set_noNewLinks(x, y, value);
    changeOccured = true;
  }

  public void randomFill() {
    Random random = new Random();
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        if (get(x, y) == Board.BLANK)
          if (random.nextBoolean())
            set_noNewLinks(x, y, Board.RED);
          else
            set_noNewLinks(x, y, Board.BLUE);
  }

  public BoardData getData() {
    return data.clone();
  }

  public void setBoard(OpenBoard newBoard) {
    this.data = newBoard.getData();
    this.size = newBoard.getSize();
    changeOccured = true;
  }
}
