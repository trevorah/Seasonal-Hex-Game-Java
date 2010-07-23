package hexBoards;

public abstract class AbstractBoard implements Board {

  protected boolean changeOccured = true;
  protected int size;
  protected String name = "Default";

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean hasChanged() {
    return changeOccured;
  }

  @Override
  public void changeNoted() {
    this.changeOccured = false;
  }
}
