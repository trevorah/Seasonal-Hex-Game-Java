package trevorah.players.randomTurn;

import trevorah.hexBoards.Board;
import trevorah.gameMechanics.Runner;
import trevorah.hexBoards.OpenBoard;
import java.awt.Point;
import java.util.ArrayList;
import trevorah.hexBoards.GameBoard;
import trevorah.gameMechanics.Move;
import trevorah.hexBoards.ScoreBoard;
import java.util.Random;
import trevorah.players.AbstractPlayer;
import trevorah.players.Player;

public class R_Path extends AbstractPlayer {

  private final ScoreBoard scoreBoard;
  private final GameBoard mainBoard;
  private final OpenBoard randomFillBoard;
  private final OpenBoard paths;
  private final int sampleLimit;

  public R_Path(Runner game, int colour, String[] args) {
    super(game, colour, args);
    int parsedVal = 0;

    if (args != null && args.length > 0)
      parsedVal = Integer.parseInt(args[0]);

    if (parsedVal <= 0)
      parsedVal = Integer.parseInt(Player.R_DEFAULT_ARGS);
    this.sampleLimit = parsedVal;
    System.out.println("sample limit set at " + sampleLimit);

    this.scoreBoard = new ScoreBoard(size);
    this.mainBoard = game.getBoard();
    this.randomFillBoard = mainBoard.openClone();
    this.randomFillBoard.setName("R_fill");
    this.paths = mainBoard.openClone();
    this.paths.setName("Paths");

    auxBoards.add(scoreBoard);
    auxBoards.add(randomFillBoard);
    auxBoards.add(paths);

  }
  boolean first = true;

  public Move getMove() {
    this.scoreBoard.wipeAll();


    for (int sampleCount = 0; sampleCount < sampleLimit; sampleCount++) {
      randomFillBoard.setBoard(mainBoard.openClone());
      paths.setBoard(mainBoard.openClone());

      randomFillBoard.randomFill();

      ArrayList<Point> path = randomFillBoard.getData().getWinningPath(player);

      for (Point p : path) {
        int x = p.x;
        int y = p.y;
        paths.set(x, y, player);
        if (mainBoard.get(x, y) == Board.BLANK && mainBoard.getSeason(x, y) == game.getSeasonPicker().getCurrentSeason(player))
          scoreBoard.raiseScore(x, y);
      }
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
