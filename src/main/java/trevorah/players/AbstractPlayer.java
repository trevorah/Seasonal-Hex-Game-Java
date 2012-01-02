package players;

import gameMechanics.Runner;
import hexBoards.Board;
import java.util.ArrayList;

public abstract class AbstractPlayer implements Player {

  protected ArrayList<Board> auxBoards = new ArrayList<Board>();
  protected Runner game;
  protected int player;
  protected int opponent;
  protected boolean pause;
  protected int size;

  public AbstractPlayer(Runner game, int colour, String[] args) {
    this.game = game;
    this.player = colour;
    if (this.player == Board.RED)
      this.opponent = Board.BLUE;
    else
      this.opponent = Board.RED;
    this.size = game.getBoard().getSize();
  }

  @Override
  public ArrayList<Board> getAuxBoards() {
    return auxBoards;
  }

  @Override
  public void setPause(boolean pause) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
