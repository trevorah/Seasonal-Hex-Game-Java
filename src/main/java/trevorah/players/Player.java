package trevorah.players;

import java.util.ArrayList;

import trevorah.hexBoards.Board;
import trevorah.gameMechanics.Move;

public interface Player {

	public static final int R_PATH = 0;
	public static final int R_POINT = 1;
	
	public static final int SEASON_PATH = 2;
	
	public static final int CLICK_PLAYER = 3;
	public static final int ALL_PATH = 4;

  public static final String R_DEFAULT_ARGS = "500 fills";
  public static final String CLICK_DEFAULT_ARGS = "n/a";
  public static final String ADJ_DEFAULT_ARGS = "n/a";
	

	public static final String[] playerList = { "Random Fill Path", "Random Fill Spot", "Seasonal Adjacency Player", "Human Player",
			  "Classic Adjacency Player" };
	public static final int[] playerIndex = { R_PATH, R_POINT, SEASON_PATH, CLICK_PLAYER, ALL_PATH };

  public static final String[] argsList = { R_DEFAULT_ARGS, R_DEFAULT_ARGS, ADJ_DEFAULT_ARGS, CLICK_DEFAULT_ARGS, ADJ_DEFAULT_ARGS};
	public Move getMove();

	public ArrayList<Board> getAuxBoards();
}
