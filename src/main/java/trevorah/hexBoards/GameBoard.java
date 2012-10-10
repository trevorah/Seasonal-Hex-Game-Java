package trevorah.hexBoards;

import trevorah.gameMechanics.Move;
import trevorah.gameMechanics.InvalidMoveException;
import trevorah.gameMechanics.SeasonMechanics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;

public class GameBoard extends AbstractBoard{

	private Point selected;
	protected BoardData data;
  private SeasonMechanics seasonPicker;

	public GameBoard(int size, SeasonMechanics seasonPicker) {
    this.name = "Game";
    this.seasonPicker = seasonPicker;
		int numberOfSeasons = seasonPicker.getSeasonCount();
		this.data = new BoardData(size, numberOfSeasons);
		this.size = size;
	}

	public GameBoard(int size, SeasonMechanics seasonPicker, BoardData data){
		this.size = size;
    this.seasonPicker = seasonPicker;
		this.data = data;
	}

  public Color getSeasonColour(int x, int y){
    return seasonPicker.getColourArray()[getSeason(x, y)];
  }


	public boolean makeMove(Move move) throws InvalidMoveException {

    boolean moveAccepted = false;
		int x = move.getX();
		int y = move.getY();
		int colour = move.getColour();
		if (x < 0 || x > size-1 || y < 0 ||y > size-1) {
     Toolkit.getDefaultToolkit().beep();
			throw new InvalidMoveException(
					"Coordinates outside the play area!", move,
					InvalidMoveException.OUTSIDE_BOARD);
		} else if (data.get(move.getX(), move.getY()).getValue() == Board.BLANK &&
            data.get(x, y).getSeason() == seasonPicker.getCurrentSeason(colour)) {
			data.set(move.getX(), move.getY(), colour);
      moveAccepted = true;
      changeOccured = true;
		} else {
      Toolkit.getDefaultToolkit().beep();
			throw new InvalidMoveException("That hex is not blank!", move,
					InvalidMoveException.ALREADY_TAKEN);
		}
		return moveAccepted;
	}

  public boolean checkwin(int player){
    return this.data.checkWin(player);
  }

  @Override
	public int get(int x, int y) {
		return data.get(x, y).getValue();
	}

  public int getSeason(int x, int y){
    return data.get(x, y).getSeason();
  }

  public int getNumberOfSeasons(){
    if(seasonPicker!= null)
    return seasonPicker.getSeasonCount();
    else
      return 1;
  }

	public Point getSelected() {
		return selected;
	}

	public void setSelected(Point selected) {
		this.selected = selected;
	}

	public void setSelected(int x, int y) {
		this.setSelected(new Point(x,y));
	}


	public OpenBoard openClone(){
		return new OpenBoard(size,seasonPicker, data.clone());
	}

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    changeOccured = true;
    this.name = name;
  }

}
