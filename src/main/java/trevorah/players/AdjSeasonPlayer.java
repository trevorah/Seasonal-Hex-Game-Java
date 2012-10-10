package trevorah.players;

import trevorah.gameMechanics.Move;
import trevorah.gameMechanics.Runner;
import trevorah.hexBoards.AdjMatrix;
import trevorah.hexBoards.Board;
import java.awt.Point;

public class AdjSeasonPlayer extends AdjPlayer {

  public AdjSeasonPlayer(Runner game, int colour, String[] args) {
    super(game,colour,args);
  }

  @Override
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

    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        if (game.getBoard().get(x, y) == Board.BLANK && mainBoard.getSeason(x, y) == game.getSeasonPicker().getCurrentSeason(player)) {

          /*
           * player related scores
           */
          int score = 0;
          boardClone.setBoard(mainBoard.openClone());

          int future = 0;
          int season = game.getSeasonPicker().getAdvanceSeason(player, future);
          boardClone.set(x, y, player);
          AdjMatrix base = boardClone.getData().getAdjMatrix(player, season);

          season = game.getSeasonPicker().getAdvanceSeason(player, 1);

          while (!(base.read(myBorder1, myBorder2) > 0) && score < maxPathLength) {
            future++;
            season = game.getSeasonPicker().getAdvanceSeason(player, future);
            base = base.mult(boardClone.getData().getAdjMatrix(player,season));
            score++;
          }

          if(score >= maxPathLength-1)
            System.out.println("my max path length reached");

          myPathlength.set(x, y, score);
          myPathCount.set(x, y, base.read(myBorder1, myBorder2));

          /*
           * opponant related scores
           */
          score = 0;
          boardClone.setBoard(mainBoard.openClone());
          boardClone.set(x, y, player);


          future = 0;
          season = game.getSeasonPicker().getAdvanceSeason(opponent, future);
          base = boardClone.getData().getAdjMatrix(opponent, season);

          while (!(base.read(oppBorder1, oppBorder2) > 0) && score < maxPathLength) {
            future++;
            season = game.getSeasonPicker().getAdvanceSeason(opponent, future);
            base = base.mult(boardClone.getData().getAdjMatrix(opponent,season));
            score++;
          }

          if(score >= maxPathLength-1)
            System.out.println("opp max path length reached");

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
      }
    }

    Point winner = pickRandom();

    int winx = winner.x;
    int winy = winner.y;

    int winScore = comboLengths.get(winx, winy);


    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        // if empty space
        if (mainBoard.get(x, y) == Board.BLANK && mainBoard.getSeason(x, y) == game.getSeasonPicker().getCurrentSeason(player)) {
          if (beats(x,y,winx,winy)) {
             winx = x;
            winy = y;

          }
        }
      }
    }
    return new Move(player, winx, winy);
  }
}
