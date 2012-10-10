package trevorah.players.randomTurn;

import trevorah.gameMechanics.Runner;
import trevorah.hexBoards.GameBoard;
import trevorah.hexBoards.OpenBoard;
import java.awt.Point;
import trevorah.hexBoards.Board;
import trevorah.gameMechanics.Move;
import trevorah.hexBoards.ScoreBoard;
import java.util.Random;
import trevorah.players.AbstractPlayer;
import trevorah.players.Player;

public class R_Single extends AbstractPlayer {

  private final int sampleLimit;
  private final ScoreBoard scoreBoard;
  private final GameBoard mainBoard;
  private final OpenBoard randomFillBoard;

  public R_Single(Runner game, int colour, String[] args) {
    super(game, colour, args);
    int parsedVal = 0;

    if (args != null && args.length > 0)
      parsedVal = Integer.parseInt(args[0]);

    if (parsedVal <= 0)
      parsedVal = Integer.parseInt(Player.R_DEFAULT_ARGS);

    this.sampleLimit = parsedVal / (int) (Math.pow(size, 2));
    System.out.println("sample limit set at " + sampleLimit + " per position");

    this.scoreBoard = new ScoreBoard(size);
    this.mainBoard = game.getBoard();
    this.randomFillBoard = mainBoard.openClone();
    this.randomFillBoard.setName("R_fill");

    auxBoards.add(scoreBoard);
    auxBoards.add(randomFillBoard);
  }

  public Move getMove() {

    this.scoreBoard.wipeAll();

    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        if (mainBoard.get(x, y) == Board.BLANK && mainBoard.getSeason(x, y) == game.getSeasonPicker().getCurrentSeason(player))
          for (int sampleCount = 0; sampleCount < sampleLimit; sampleCount++) {
            randomFillBoard.setBoard(mainBoard.openClone());
            randomFillBoard.set_noNewLinks(x, y, player);
            randomFillBoard.randomFill();

            if (randomFillBoard.getData().getWinningPath(player).size() > 0)
              scoreBoard.raiseScore(x, y);

          }
    Point chosen = getTopScore();
    return new Move(this.player, chosen.x, chosen.y);
  }

  protected Point getTopScore() {
    Random random = new Random();
    Point chosen = new Point();
    do {
      chosen = new Point(random.nextInt(size), random.nextInt(size));
    } while (!(mainBoard.get(chosen.x, chosen.y) == Board.BLANK && mainBoard.getSeason(chosen.x, chosen.y) == game.getSeasonPicker().getCurrentSeason(player)));


    for (int row = 0; row < size; row++)
      for (int column = 0; column < size; column++)
        if ((scoreBoard.get(column, row) > scoreBoard.get(chosen.x, chosen.y))
                && mainBoard.get(column, row) == Board.BLANK) {
          chosen.x = column;
          chosen.y = row;
        }
    return chosen;
  }
}
