package trevorah.hexBoards;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import trevorah.gameMechanics.SeasonMechanics;

public class OpenBoardTest {

    private OpenBoard openBoard;
    private static final int SIZE = 3;

    @Before
    public void setUp() {
        SeasonMechanics sm = new SeasonMechanics(1);
        BoardData data = new BoardData(SIZE, 1);
        openBoard = new OpenBoard(SIZE, sm, data);
    }

    @Test
    public void setChangesValue() {
        openBoard.set(0, 0, Board.RED);
        assertEquals(Board.RED, openBoard.get(0, 0));
    }

    @Test
    public void setNoNewLinksChangesValue() {
        openBoard.set_noNewLinks(1, 1, Board.BLUE);
        assertEquals(Board.BLUE, openBoard.get(1, 1));
    }

    @Test
    public void randomFillLeavesNoBlanks() {
        openBoard.randomFill();
        for (int x = 0; x < SIZE; x++)
            for (int y = 0; y < SIZE; y++)
                assertNotEquals(Board.BLANK, openBoard.get(x, y));
    }

    @Test
    public void randomFillPreservesExistingMoves() {
        openBoard.set(0, 0, Board.RED);
        openBoard.set(2, 2, Board.BLUE);
        openBoard.randomFill();
        assertEquals(Board.RED, openBoard.get(0, 0));
        assertEquals(Board.BLUE, openBoard.get(2, 2));
    }

    @Test
    public void randomFillUsesOnlyRedAndBlue() {
        openBoard.randomFill();
        for (int x = 0; x < SIZE; x++)
            for (int y = 0; y < SIZE; y++) {
                int val = openBoard.get(x, y);
                assertTrue("Value should be RED or BLUE",
                        val == Board.RED || val == Board.BLUE);
            }
    }

    @Test
    public void setBoardReplacesState() {
        SeasonMechanics sm = new SeasonMechanics(1);
        BoardData newData = new BoardData(SIZE, 1);
        newData.set(1, 1, Board.RED);
        OpenBoard replacement = new OpenBoard(SIZE, sm, newData);

        openBoard.setBoard(replacement);
        assertEquals(Board.RED, openBoard.get(1, 1));
    }

    @Test
    public void getDataReturnsClone() {
        openBoard.set(0, 0, Board.RED);
        BoardData data1 = openBoard.getData();
        BoardData data2 = openBoard.getData();

        // Both should have same content
        assertEquals(Board.RED, data1.get(0, 0).getValue());
        assertEquals(Board.RED, data2.get(0, 0).getValue());

        // But modifying one should not affect the other or the board
        data1.set(1, 1, Board.BLUE);
        assertEquals(Board.BLANK, data2.get(1, 1).getValue());
        assertEquals(Board.BLANK, openBoard.get(1, 1));
    }
}
