package trevorah.gameMechanics;

public class InvalidMoveException extends Exception {

  public static final int OUTSIDE_BOARD = 0;
  public static final int ALREADY_TAKEN = 1;
  private Move move;
  private int problem = -1;

  public InvalidMoveException(String exception, Move move, int problem) {
    super(exception);
    this.move = move;
    this.problem = problem;
  }

  public int getProblem() {
    return this.problem;
  }

  public Move getMove() {
    return this.move;
  }
}
