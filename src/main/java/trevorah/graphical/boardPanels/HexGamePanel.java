package trevorah.graphical.boardPanels;

import trevorah.hexBoards.Board;
import trevorah.hexBoards.GameBoard;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;

@SuppressWarnings("serial")
public class HexGamePanel extends HexPanel{


  public HexGamePanel(GameBoard board){
    super(board);
    this.addMouseListener(new HexSelector());
    this.setPreferredSize(new Dimension(400, 200));
  }





  @Override
  	public Color getFillColour(int x, int y) {
     int value = board.get(x, y);
		Color returnColour = Color.WHITE;
		switch (value) {
		case Board.RED:
			returnColour = (Color.RED);
			break;
		case Board.BLUE:
			returnColour = (Color.BLUE);
			break;
		case Board.BLANK:
      if(((GameBoard)board).getNumberOfSeasons()>1)
        returnColour = ((GameBoard)board).getSeasonColour(x, y);
      else
        returnColour = Color.WHITE;
			break;
		default:
			returnColour = (Color.WHITE);
		}
		return returnColour;
	}


  @Override
	public Color getSeasonColour(int value) {
		Color returnColour = Color.WHITE;
		switch (value) {
		case 0:
			returnColour = (Color.GREEN.brighter());
			break;
		case 1:
			returnColour = (Color.YELLOW.brighter());
			break;
		case 2:
			returnColour = (Color.magenta.brighter());
			break;
		case 3:
			returnColour = (Color.PINK);
			break;
		default:
			returnColour = (Color.WHITE);
		}
		return returnColour;
	}

public void click(Point p) {
		chosenXY = p;
		row: for (int y = 0; y < size; y++) {
		column:	for (int x = 0; x < size; x++) {
        Polygon poly = calcHexPoly(x, y);
        if(poly.contains(p)){
        ((GameBoard)board).setSelected(x,y);
        break row;
        }
			}
		}
	}

}
