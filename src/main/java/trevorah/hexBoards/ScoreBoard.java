package trevorah.hexBoards;

public class ScoreBoard extends AbstractBoard {

  private int[][] board;
  private int lowestScore;
  private int highestScore;

  public ScoreBoard(int size) {
    this.name = "Score";
    this.size = size;
    this.board = new int[size][size];

  }

  public void set(int x, int y, int value) {
    board[x][y] = value;
    changeOccured = true;
  }

  public void raiseScore(int x, int y) {
    board[x][y] = board[x][y] + 1;
    changeOccured = true;
  }

  public void wipeAll() {
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        board[i][j] = 0;
    changeOccured = true;
  }

  @Override
  public int get(int x, int y) {
    return board[x][y];
  }

  public int getHighestScore() {
    refreshHighLow();
    return highestScore;
  }

  public int getLowestScore() {
    refreshHighLow();
    return lowestScore;
  }

  private void refreshHighLow() {
    lowestScore = board[0][0];
    highestScore = board[0][0];

    for (int[] row : board)
      for (int value : row)
        if (value > highestScore)
          highestScore = value;
        else if (value < lowestScore)
          lowestScore = value;
  }
}
