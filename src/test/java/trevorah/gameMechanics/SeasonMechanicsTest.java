package trevorah.gameMechanics;

import static org.junit.Assert.*;
import org.junit.Test;
import trevorah.hexBoards.Board;

public class SeasonMechanicsTest {

    // --- getSubPattern tests ---

    @Test
    public void oneSeasonSubPatternRed() {
        SeasonMechanics sm = new SeasonMechanics(1);
        int[] pattern = sm.getSubPattern(Board.RED);
        assertEquals(1, pattern.length);
        assertEquals(0, pattern[0]);
    }

    @Test
    public void oneSeasonSubPatternBlue() {
        SeasonMechanics sm = new SeasonMechanics(1);
        int[] pattern = sm.getSubPattern(Board.BLUE);
        assertEquals(1, pattern.length);
        assertEquals(0, pattern[0]);
    }

    @Test
    public void twoSeasonSubPatternRed() {
        SeasonMechanics sm = new SeasonMechanics(2);
        int[] pattern = sm.getSubPattern(Board.RED);
        // Full pattern: {0, 0, 1, 1}, RED takes even indices: [0]=0, [2]=1
        assertEquals(2, pattern.length);
        assertEquals(0, pattern[0]);
        assertEquals(1, pattern[1]);
    }

    @Test
    public void twoSeasonSubPatternBlue() {
        SeasonMechanics sm = new SeasonMechanics(2);
        int[] pattern = sm.getSubPattern(Board.BLUE);
        // Full pattern: {0, 0, 1, 1}, BLUE takes odd indices: [1]=0, [3]=1
        assertEquals(2, pattern.length);
        assertEquals(0, pattern[0]);
        assertEquals(1, pattern[1]);
    }

    @Test
    public void threeSeasonSubPatternRed() {
        SeasonMechanics sm = new SeasonMechanics(3);
        int[] pattern = sm.getSubPattern(Board.RED);
        // Full pattern: {0, 2, 1, 0, 2, 1}, RED takes even indices: [0]=0, [2]=1, [4]=2
        assertEquals(3, pattern.length);
        assertEquals(0, pattern[0]);
        assertEquals(1, pattern[1]);
        assertEquals(2, pattern[2]);
    }

    @Test
    public void threeSeasonSubPatternBlue() {
        SeasonMechanics sm = new SeasonMechanics(3);
        int[] pattern = sm.getSubPattern(Board.BLUE);
        // Full pattern: {0, 2, 1, 0, 2, 1}, BLUE takes odd indices: [1]=2, [3]=0, [5]=1
        assertEquals(3, pattern.length);
        assertEquals(2, pattern[0]);
        assertEquals(0, pattern[1]);
        assertEquals(1, pattern[2]);
    }

    // --- getCurrentSeason tests ---

    @Test
    public void initialCurrentSeasonRed() {
        SeasonMechanics sm = new SeasonMechanics(2);
        // redPosition starts at 0, redSubPattern[0] = 0
        assertEquals(0, sm.getCurrentSeason(Board.RED));
    }

    @Test
    public void initialCurrentSeasonBlue() {
        SeasonMechanics sm = new SeasonMechanics(2);
        // bluePosition starts at 0, blueSubPattern[0] = 0
        assertEquals(0, sm.getCurrentSeason(Board.BLUE));
    }

    // --- increment tests ---

    @Test
    public void incrementAdvancesSeason() {
        SeasonMechanics sm = new SeasonMechanics(2);
        // RED: subPattern = [0, 1]
        assertEquals(0, sm.getCurrentSeason(Board.RED));
        sm.increment(Board.RED);
        assertEquals(1, sm.getCurrentSeason(Board.RED));
    }

    @Test
    public void incrementWrapsAround() {
        SeasonMechanics sm = new SeasonMechanics(2);
        // RED: subPattern = [0, 1], length 2
        sm.increment(Board.RED);
        sm.increment(Board.RED);
        // Should wrap back to position 0
        assertEquals(0, sm.getCurrentSeason(Board.RED));
    }

    @Test
    public void incrementDoesNotCrossContaminate() {
        SeasonMechanics sm = new SeasonMechanics(2);
        sm.increment(Board.RED);
        // BLUE should still be at position 0
        assertEquals(0, sm.getCurrentSeason(Board.BLUE));
        assertEquals(1, sm.getCurrentSeason(Board.RED));
    }

    // --- getAdvanceSeason tests ---

    @Test
    public void getAdvanceSeasonLookahead() {
        SeasonMechanics sm = new SeasonMechanics(3);
        // RED subPattern = [0, 1, 2], position starts at 0
        // advance 1: position (0+1)%3 = 1, pattern[1] = 1
        assertEquals(1, sm.getAdvanceSeason(Board.RED, 1));
        // advance 2: position (0+2)%3 = 2, pattern[2] = 2
        assertEquals(2, sm.getAdvanceSeason(Board.RED, 2));
    }

    @Test
    public void getAdvanceSeasonWraps() {
        SeasonMechanics sm = new SeasonMechanics(3);
        // RED subPattern = [0, 1, 2], position 0
        // advance 3: position (0+3)%3 = 0, pattern[0] = 0
        assertEquals(0, sm.getAdvanceSeason(Board.RED, 3));
    }

    // --- getNextSeason tests ---

    @Test
    public void getNextSeasonCycles() {
        SeasonMechanics sm = new SeasonMechanics(3);
        // RED subPattern = [0, 1, 2]
        assertEquals(1, sm.getNextSeason(0, Board.RED));
        assertEquals(2, sm.getNextSeason(1, Board.RED));
        assertEquals(0, sm.getNextSeason(2, Board.RED));
    }

    @Test
    public void getNextSeasonOneSeason() {
        SeasonMechanics sm = new SeasonMechanics(1);
        // subPattern = [0], next of 0 wraps to 0
        assertEquals(0, sm.getNextSeason(0, Board.RED));
    }

    // --- thinkingPlayer / isThinking tests ---

    @Test
    public void initiallyNotThinking() {
        SeasonMechanics sm = new SeasonMechanics(1);
        assertFalse(sm.isThinking(Board.RED));
        assertFalse(sm.isThinking(Board.BLUE));
    }

    @Test
    public void thinkingPlayerToggles() {
        SeasonMechanics sm = new SeasonMechanics(1);
        sm.thinkingPlayer(Board.RED);
        assertTrue(sm.isThinking(Board.RED));
        assertFalse(sm.isThinking(Board.BLUE));

        sm.thinkingPlayer(Board.BLUE);
        assertFalse(sm.isThinking(Board.RED));
        assertTrue(sm.isThinking(Board.BLUE));
    }

    // --- seasonCount tests ---

    @Test
    public void seasonCountReturnsCorrectValue() {
        assertEquals(1, new SeasonMechanics(1).getSeasonCount());
        assertEquals(2, new SeasonMechanics(2).getSeasonCount());
        assertEquals(3, new SeasonMechanics(3).getSeasonCount());
    }
}
