package trevorah.players;

import trevorah.gameMechanics.Runner;
import java.awt.Point;
import java.util.Random;
import trevorah.hexBoards.AdjMatrix;
import trevorah.hexBoards.Board;
import trevorah.hexBoards.BoardData;
import trevorah.hexBoards.GameBoard;
import trevorah.gameMechanics.Move;
import trevorah.hexBoards.OpenBoard;
import trevorah.hexBoards.ScoreBoard;

public class AdjPlayer extends AbstractPlayer {

  protected GameBoard mainBoard;
  protected OpenBoard boardClone;
  protected ScoreBoard myPathlength;
  protected ScoreBoard oppPathlength;
  protected ScoreBoard myPathCount;
  protected ScoreBoard oppPathCount;
  protected double aggro = 0.5;
  int myBorder1;
  int myBorder2;
  int oppBorder1;
  int oppBorder2;
  protected ScoreBoard comboLengths;
  protected ScoreBoard comboPathCounts;
  protected int maxPathLength;
  Random random = new Random();

  public AdjPlayer(Runner game, int colour, String[] args) {
    super(game, colour, args);
    this.maxPathLength = size * size;
    this.mainBoard = game.getBoard();
    this.boardClone = mainBoard.openClone();
    this.myPathlength = new ScoreBoard(size);
    this.oppPathlength = new ScoreBoard(size);
    this.myPathCount = new ScoreBoard(size);
    this.oppPathCount = new ScoreBoard(size);
    this.comboLengths = new ScoreBoard(size);
    this.comboPathCounts = new ScoreBoard(size);

    boardClone.setName("clone");
    myPathlength.setName("my Plength");
    oppPathlength.setName("opp Plength");
    myPathCount.setName("my Pcount");
    oppPathCount.setName("opp Pcount");
    comboLengths.setName("combo Plength");
    comboPathCounts.setName("combo Pcount");

    auxBoards.add(boardClone);
    auxBoards.add(myPathlength);
    auxBoards.add(oppPathlength);
    auxBoards.add(myPathCount);
    auxBoards.add(oppPathCount);
    auxBoards.add(comboLengths);
    auxBoards.add(comboPathCounts);

    switch (colour) {
      case Board.RED:
        myBorder1 = BoardData.RED_BORDER1_NODE;
        myBorder2 = BoardData.RED_BORDER2_NODE;
        oppBorder1 = BoardData.BLUE_BORDER1_NODE;
        oppBorder2 = BoardData.BLUE_BORDER2_NODE;
        break;
      case Board.BLUE:
        myBorder1 = BoardData.BLUE_BORDER1_NODE;
        myBorder2 = BoardData.BLUE_BORDER2_NODE;
        oppBorder1 = BoardData.RED_BORDER1_NODE;
        oppBorder2 = BoardData.RED_BORDER2_NODE;
        break;
    }
  }

  public Move getMove() {
    /*
     * wipe all scoreboards
     */
    myPathlength.wipeAll();
    oppPathlength.wipeAll();
    myPathCount.wipeAll();
    oppPathCount.wipeAll();
    comboLengths.wipeAll();
    comboPathCounts.wipeAll();

    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        if (game.getBoard().get(x, y) == Board.BLANK) {

          /*
           * player related scores
           */
          int score = 0;
          boardClone.setBoard(mainBoard.openClone());
          boardClone.set(x, y, player);
          AdjMatrix base = boardClone.getData().getAdjMatrix(player).clone();

          while (!(base.read(myBorder1, myBorder2) > 0) && score < maxPathLength) {
            base = base.mult(boardClone.getData().getAdjMatrix(player));
            score++;
          }

          myPathlength.set(x, y, score);
          myPathCount.set(x, y, base.read(myBorder1, myBorder2));

          /*
           * opponant related scores
           */
          score = 0;
          boardClone.setBoard(mainBoard.openClone());

          boardClone.set(x, y, player);
          base = boardClone.getData().getAdjMatrix(opponent).clone();

          while (!(base.read(oppBorder1, oppBorder2) > 0) && score < maxPathLength) {
            base = base.mult(boardClone.getData().getAdjMatrix(opponent));
            score++;
          }

          oppPathlength.set(x, y, score);
          oppPathCount.set(x, y, base.read(oppBorder1, oppBorder2));

          /*
           * combine scores...
           */
          score = (int) (aggro * myPathlength.get(x, y) - (1 - aggro) * oppPathlength.get(x, y));
          comboLengths.set(x, y, score);

          score = (int) (aggro * (-myPathCount.get(x, y)) + (1 - aggro) * oppPathCount.get(x, y));
          comboPathCounts.set(x, y, score);
        }


    Point winner = pickRandom();

    int winx = winner.x;
    int winy = winner.y;

    int winScore = comboLengths.get(winx, winy);


    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        // if empty space
        if (mainBoard.get(x, y) == Board.BLANK && mainBoard.getSeason(x, y) == game.getSeasonPicker().getCurrentSeason(player))
          if (beats(x, y, winx, winy)) {
            winx = x;
            winy = y;

          }

    return new Move(player, winx, winy);
  }

  protected Point pickRandom() {
    int x = 0;
    int y = 0;
    do {
      x = random.nextInt(this.size);
      y = random.nextInt(this.size);
    } while (mainBoard.get(x, y) != Board.BLANK || mainBoard.getSeason(x, y) != game.getSeasonPicker().getCurrentSeason(player));
    return new Point(x, y);
  }

  protected boolean beats(int x, int y, int winningX, int winningY) {
    int length_compettitor = comboLengths.get(x, y);
    int length_champion = comboLengths.get(winningX, winningY);
    if (length_compettitor < length_champion)
      return true;
    else if (length_compettitor == length_champion) {
      int path_competitor = comboPathCounts.get(x, y);
      int path_champion = comboPathCounts.get(winningX, winningY);
      if (path_competitor < path_champion)
        return true;
      else if (path_competitor == path_champion)
        return random.nextBoolean();

    }
    return false;

  }
}
