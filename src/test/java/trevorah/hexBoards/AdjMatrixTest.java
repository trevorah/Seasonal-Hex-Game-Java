package trevorah.hexBoards;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AdjMatrixTest {

    private AdjMatrix matrix;

    @Before
    public void setUp() {
        matrix = new AdjMatrix(5);
    }

    @Test
    public void initialValuesAreNoLink() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertEquals(AdjMatrix.NO_LINK, matrix.read(i, j));
    }

    @Test
    public void writeAndReadSymmetry() {
        matrix.write(1, 3, AdjMatrix.LINK);
        assertEquals(AdjMatrix.LINK, matrix.read(1, 3));
        assertEquals(AdjMatrix.LINK, matrix.read(3, 1));
    }

    @Test
    public void diagonalAlwaysLink() {
        // Writing to diagonal should always produce LINK regardless of value
        matrix.write(2, 2, AdjMatrix.NO_LINK);
        assertEquals(AdjMatrix.LINK, matrix.read(2, 2));
    }

    @Test
    public void size() {
        assertEquals(5, matrix.size());
        AdjMatrix bigger = new AdjMatrix(10);
        assertEquals(10, bigger.size());
    }

    @Test
    public void wipeNodeDisconnects() {
        matrix.write(0, 1, AdjMatrix.LINK);
        matrix.write(0, 2, AdjMatrix.LINK);
        matrix.write(1, 2, AdjMatrix.LINK);

        matrix.wipeNode(0);

        assertEquals(AdjMatrix.NO_LINK, matrix.read(0, 1));
        assertEquals(AdjMatrix.NO_LINK, matrix.read(0, 2));
        assertEquals(AdjMatrix.NO_LINK, matrix.read(1, 0));
        assertEquals(AdjMatrix.NO_LINK, matrix.read(2, 0));
        // Other links should remain
        assertEquals(AdjMatrix.LINK, matrix.read(1, 2));
        assertEquals(AdjMatrix.LINK, matrix.read(2, 1));
    }

    @Test
    public void bypassAndRemoveNodeCreatesTransitiveLinks() {
        // 0 -- 2 -- 4, bypassing 2 should link 0 to 4
        matrix.write(0, 2, AdjMatrix.LINK);
        matrix.write(2, 4, AdjMatrix.LINK);

        matrix.bypassAndRemoveNode(2);

        assertEquals(AdjMatrix.LINK, matrix.read(0, 4));
        assertEquals(AdjMatrix.LINK, matrix.read(4, 0));
        // Node 2 should be disconnected from everything else
        assertEquals(AdjMatrix.NO_LINK, matrix.read(0, 2));
        assertEquals(AdjMatrix.NO_LINK, matrix.read(2, 4));
    }

    @Test
    public void bypassChainSimulatesGame() {
        // Simulate nodes 0, 1, 2, 3, 4 in a chain: 0--1--2--3--4
        matrix.write(0, 1, AdjMatrix.LINK);
        matrix.write(1, 2, AdjMatrix.LINK);
        matrix.write(2, 3, AdjMatrix.LINK);
        matrix.write(3, 4, AdjMatrix.LINK);

        // Bypass node 1: 0 should now link to 2
        matrix.bypassAndRemoveNode(1);
        assertEquals(AdjMatrix.LINK, matrix.read(0, 2));

        // Bypass node 3: 2 should now link to 4
        matrix.bypassAndRemoveNode(3);
        assertEquals(AdjMatrix.LINK, matrix.read(2, 4));

        // Bypass node 2: 0 should now link to 4 (transitive chain complete)
        matrix.bypassAndRemoveNode(2);
        assertEquals(AdjMatrix.LINK, matrix.read(0, 4));
    }

    @Test
    public void cloneIsIndependent() {
        matrix.write(0, 1, AdjMatrix.LINK);
        AdjMatrix cloned = matrix.clone();

        assertEquals(AdjMatrix.LINK, cloned.read(0, 1));

        // Modify clone
        cloned.write(0, 1, AdjMatrix.NO_LINK);
        cloned.write(2, 3, AdjMatrix.LINK);

        // Original unchanged
        assertEquals(AdjMatrix.LINK, matrix.read(0, 1));
        assertEquals(AdjMatrix.NO_LINK, matrix.read(2, 3));
    }

    @Test
    public void multSizeMismatchReturnsNull() {
        AdjMatrix smaller = new AdjMatrix(3);
        assertNull(matrix.mult(smaller));
    }

    @Test
    public void multSameSizeReturnsResult() {
        AdjMatrix other = new AdjMatrix(5);
        AdjMatrix result = matrix.mult(other);
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    public void bypassNodeWithNoNeighboursIsNoOp() {
        // Node 3 has no neighbours - bypass should not fail
        matrix.write(0, 1, AdjMatrix.LINK);
        matrix.bypassAndRemoveNode(3);
        // Existing links unchanged
        assertEquals(AdjMatrix.LINK, matrix.read(0, 1));
    }
}
