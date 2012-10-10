package trevorah.hexBoards;

import java.util.ArrayList;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.UpperSPDPackMatrix;

public class AdjMatrix {

  public static final int NO_LINK = 0;
  public static final int LINK = 1;
  private Matrix data;
  private int size;

  public AdjMatrix(int size) {
    //remember to add the border nodes as well
    this.size = size;
    this.data = new UpperSPDPackMatrix(size);
  }

  public AdjMatrix(Matrix adj) {
    this.data = adj;
    this.size = data.numColumns();
  }

  @Override
  public AdjMatrix clone() {
    return new AdjMatrix(data.copy());
  }

  public Matrix getData() {
    return this.data;
  }

  public AdjMatrix mult(AdjMatrix m2) {
    // both are assumed to be the same size
    if (m2.size() == this.size()) {
      Matrix retMatrix = new UpperSPDPackMatrix(size);
      data.mult(m2.getData(), retMatrix);
      return new AdjMatrix(retMatrix);
    } else
      return null;
  }

  public void write(int a, int b, int value) {
    if (a == b)
      value = LINK;
    internalWrite(a, b, value);
  }

  public int read(int x, int y) {
    return (int) data.get(x, y);
  }

  public void wipeNode(int nodeId) {
    for (int i = 0; i < size; i++)
      if (i != nodeId)
        internalWrite(i, nodeId, NO_LINK);
  }

  private void internalWrite(int x, int y, int value) {
    data.set(x, y, value);
    data.set(y, x, value);
  }

  public int size() {
    return data.numColumns();
  }

  public void print() {
    for (int y = 0; y < data.numRows(); y++) {
      for (int x = 0; x < data.numColumns(); x++)
        System.out.print((int) (data.get(x, y)) + " ");
      System.out.print("\n");
    }
  }

  public void bypassAndRemoveNode(int node) {
    ArrayList<Integer> neighbours = new ArrayList<Integer>();
    /*
     * populate neighbour list
     */
    for (int i = 0; i < size(); i++)
      if (read(node, i) == LINK)
        neighbours.add(i);
    /*
     * create links between all neighbours
     */
    for (int neighbour : neighbours)
      for (int newNeighbour : neighbours)
        write(neighbour, newNeighbour, LINK);
    /*
     * remove links to and from original node
     */
    wipeNode(node);
  }
}
