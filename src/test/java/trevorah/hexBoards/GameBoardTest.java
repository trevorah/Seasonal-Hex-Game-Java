package trevorah.hexBoards;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import trevorah.gameMechanics.InvalidMoveException;
import trevorah.gameMechanics.Move;
import trevorah.gameMechanics.SeasonMechanics;

public class GameBoardTest {

    private GameBoard gameBoard;
    private SeasonMechanics seasonMechanics;
    private static final int SIZE = 3;

    @Before
    public void setUp() {
        // 1-season mode: all tiles season 0, all moves accepted
        seasonMechanics = new SeasonMechanics(1);
        gameBoard = new GameBoard(SIZE, seasonMechanics);
    }

    @Test
    public void validMoveReturnsTrue() throws InvalidMoveException {
        Move move = new Move(Board.RED, 0, 0);
        assertTrue(gameBoard.makeMove(move));
    }

    @Test
    public void validMoveChangesBoardState() throws InvalidMoveException {
        Move move = new Move(Board.RED, 1, 1);
        gameBoard.makeMove(move);
        assertEquals(Board.RED, gameBoard.get(1, 1));
    }

    @Test(expected = InvalidMoveException.class)
    public void negativeXThrowsOutsideBoard() throws InvalidMoveException {
        gameBoard.makeMove(new Move(Board.RED, -1, 0));
    }

    @Test(expected = InvalidMoveException.class)
    public void negativeYThrowsOutsideBoard() throws InvalidMoveException {
        gameBoard.makeMove(new Move(Board.RED, 0, -1));
    }

    @Test(expected = InvalidMoveException.class)
    public void overflowXThrowsOutsideBoard() throws InvalidMoveException {
        gameBoard.makeMove(new Move(Board.RED, SIZE, 0));
    }

    @Test(expected = InvalidMoveException.class)
    public void overflowYThrowsOutsideBoard() throws InvalidMoveException {
        gameBoard.makeMove(new Move(Board.RED, 0, SIZE));
    }

    @Test
    public void outsideBoardExceptionHasCorrectProblem() {
        try {
            gameBoard.makeMove(new Move(Board.RED, -1, 0));
            fail("Expected InvalidMoveException");
        } catch (InvalidMoveException e) {
            assertEquals(InvalidMoveException.OUTSIDE_BOARD, e.getProblem());
        }
    }

    @Test
    public void alreadyTakenThrowsException() throws InvalidMoveException {
        gameBoard.makeMove(new Move(Board.RED, 0, 0));
        try {
            gameBoard.makeMove(new Move(Board.BLUE, 0, 0));
            fail("Expected InvalidMoveException");
        } catch (InvalidMoveException e) {
            assertEquals(InvalidMoveException.ALREADY_TAKEN, e.getProblem());
        }
    }

    @Test
    public void winDetectionDelegatesToBoardData() throws InvalidMoveException {
        assertFalse(gameBoard.checkwin(Board.RED));
        // Fill column 0 for RED win (N-S)
        gameBoard.makeMove(new Move(Board.RED, 0, 0));
        gameBoard.makeMove(new Move(Board.RED, 0, 1));
        gameBoard.makeMove(new Move(Board.RED, 0, 2));
        assertTrue(gameBoard.checkwin(Board.RED));
    }

    @Test
    public void openCloneIsIndependent() throws InvalidMoveException {
        gameBoard.makeMove(new Move(Board.RED, 0, 0));
        OpenBoard clone = gameBoard.openClone();

        assertEquals(Board.RED, clone.get(0, 0));

        // Modify clone
        clone.set(1, 1, Board.BLUE);
        // Original should not be affected
        assertEquals(Board.BLANK, gameBoard.get(1, 1));
    }

    @Test
    public void nameIsGame() {
        assertEquals("Game", gameBoard.getName());
    }

    @Test
    public void sizeIsCorrect() {
        assertEquals(SIZE, gameBoard.getSize());
    }

    @Test
    public void numberOfSeasonsReturnsCorrectValue() {
        assertEquals(1, gameBoard.getNumberOfSeasons());
    }

    @Test
    public void wrongSeasonTileRejectedOnMultiSeasonBoard() {
        // 2-season board
        SeasonMechanics sm2 = new SeasonMechanics(2);
        GameBoard multiBoard = new GameBoard(5, sm2);
        int currentSeason = sm2.getCurrentSeason(Board.RED);

        // Find a tile that is NOT the current season for RED
        boolean foundWrongSeason = false;
        for (int x = 0; x < 5 && !foundWrongSeason; x++) {
            for (int y = 0; y < 5 && !foundWrongSeason; y++) {
                if (multiBoard.getSeason(x, y) != currentSeason) {
                    try {
                        multiBoard.makeMove(new Move(Board.RED, x, y));
                        fail("Expected InvalidMoveException for wrong season tile");
                    } catch (InvalidMoveException e) {
                        assertEquals(InvalidMoveException.ALREADY_TAKEN, e.getProblem());
                        foundWrongSeason = true;
                    }
                }
            }
        }
        // With 2 seasons on a 5x5 board, there should always be at least one wrong-season tile
        assertTrue("Should have found at least one wrong-season tile", foundWrongSeason);
    }

    @Test
    public void correctSeasonTileAccepted() throws InvalidMoveException {
        // 2-season board
        SeasonMechanics sm2 = new SeasonMechanics(2);
        GameBoard multiBoard = new GameBoard(5, sm2);
        int currentSeason = sm2.getCurrentSeason(Board.RED);

        // Find a tile that IS the current season for RED
        boolean foundCorrectSeason = false;
        for (int x = 0; x < 5 && !foundCorrectSeason; x++) {
            for (int y = 0; y < 5 && !foundCorrectSeason; y++) {
                if (multiBoard.getSeason(x, y) == currentSeason) {
                    assertTrue(multiBoard.makeMove(new Move(Board.RED, x, y)));
                    foundCorrectSeason = true;
                }
            }
        }
        assertTrue("Should have found at least one correct-season tile", foundCorrectSeason);
    }
}
