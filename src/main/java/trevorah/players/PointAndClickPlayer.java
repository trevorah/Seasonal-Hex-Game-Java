package trevorah.players;

import trevorah.gameMechanics.Runner;

import java.awt.Point;
import java.util.ArrayList;

import trevorah.hexBoards.Board;
import trevorah.gameMechanics.Move;

public class PointAndClickPlayer implements Player {
	private Runner game = null;
	private int colour = 0;

	public PointAndClickPlayer(Runner game, int colour) {
		this.game = game;
		this.colour = colour;
	}

  public Move getMove() {
		switch (colour) {
		case Board.RED:
			System.out.print("Red move: ");
			break;
		case Board.BLUE:
			System.out.print("Blue move: ");
			break;
		}

		while (game.getBoard().getSelected() == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Point choice = game.getBoard().getSelected();

		Move move = new Move(colour, choice.x, choice.y);

		game.getBoard().setSelected(null);
		return move;
	}

	public ArrayList<Board> getAuxBoards() {
		return new ArrayList<Board>();
	}
}
