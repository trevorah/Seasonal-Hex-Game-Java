package trevorah.hexBoards;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ScoreBoardTest {

    private ScoreBoard board;

    @Before
    public void setUp() {
        board = new ScoreBoard(3);
    }

    @Test
    public void initialValuesAreZero() {
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                assertEquals(0, board.get(x, y));
    }

    @Test
    public void setAndGet() {
        board.set(1, 2, 42);
        assertEquals(42, board.get(1, 2));
    }

    @Test
    public void raiseScore() {
        board.raiseScore(0, 0);
        assertEquals(1, board.get(0, 0));
        board.raiseScore(0, 0);
        assertEquals(2, board.get(0, 0));
        board.raiseScore(0, 0);
        assertEquals(3, board.get(0, 0));
    }

    @Test
    public void highestScore() {
        board.set(0, 0, 5);
        board.set(1, 1, 10);
        board.set(2, 2, 3);
        assertEquals(10, board.getHighestScore());
    }

    @Test
    public void lowestScore() {
        board.set(0, 0, 5);
        board.set(1, 1, 10);
        board.set(2, 2, 3);
        assertEquals(0, board.getLowestScore());
    }

    @Test
    public void highestAndLowestWhenAllSame() {
        assertEquals(0, board.getHighestScore());
        assertEquals(0, board.getLowestScore());
    }

    @Test
    public void wipeAllResetsToZero() {
        board.set(0, 0, 5);
        board.set(1, 1, 10);
        board.raiseScore(2, 2);
        board.wipeAll();
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                assertEquals(0, board.get(x, y));
    }

    @Test
    public void nameIsScore() {
        assertEquals("Score", board.getName());
    }

    @Test
    public void sizeIsCorrect() {
        assertEquals(3, board.getSize());
    }

    @Test
    public void highestAndLowestAfterWipe() {
        board.set(0, 0, 99);
        board.wipeAll();
        assertEquals(0, board.getHighestScore());
        assertEquals(0, board.getLowestScore());
    }
}
