package players;

import gameMechanics.Runner;

import java.awt.Point;
import java.util.ArrayList;

import hexBoards.Board;
import gameMechanics.Move;

public class PointAndClickPlayer implements Player {
	private Runner game = null;
	private int colour = 0;

	public PointAndClickPlayer(Runner game, int colour) {
		this.game = game;
		this.colour = colour;
	}

  @Override
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

	@Override
	public ArrayList<Board> getAuxBoards() {
		return new ArrayList<Board>();
	}

	@Override
	public void setPause(boolean pause) {
	}

}
