package trevorah.graphical;

import trevorah.gameMechanics.Runner;
import trevorah.gameMechanics.SeasonMechanics;
import trevorah.hexBoards.Board;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class BoardSetupPanel extends JPanel {

  private final JLabel sizeLabel;
  private final SpinnerNumberModel boardSizeConstraints;
  private final JSpinner boardSizeSpinner;
  private final JLabel seasonLabel;
  private final SpinnerNumberModel seasonConstraints;
  private final JSpinner seasonCountSpinner;
  private final JLabel turnStyleLabel;
  private final JComboBox turnStyleList;

  public BoardSetupPanel() {
    super();

    this.sizeLabel = new JLabel("Board size:");
    this.boardSizeConstraints = new SpinnerNumberModel(
            Board.DEFAULT_BOARD_SIZE,
            Board.MIN_SUPPORTED_BOARD_SIZE,
            Board.MAX_SUPPORTED_BOARD_SIZE,
            1);
    this.boardSizeSpinner = new JSpinner(boardSizeConstraints);

    this.seasonLabel = new JLabel("with season count:");
    this.seasonConstraints = new SpinnerNumberModel(
            SeasonMechanics.DEFAULT_SEASON_SIZE,
            SeasonMechanics.MIN_SUPPORTED_SEASON_SIZE,
            SeasonMechanics.MAX_SUPPORTED_SEASON_SIZE,
            1);
    this.seasonCountSpinner = new JSpinner(seasonConstraints);

    this.turnStyleLabel = new JLabel("turn style:");
    this.turnStyleList = new JComboBox(Runner.GAME_LIST);

    setup();
  }

  private void setup() {
    this.add(sizeLabel);
    this.add(boardSizeSpinner);
    this.add(seasonLabel);
    this.add(seasonCountSpinner);
    this.add(turnStyleLabel);
    this.add(turnStyleList);
  }

  public int getGameType() {
    return Runner.GAME_CODES[turnStyleList.getSelectedIndex()];
  }

  public int getBoardSize() {
    return (Integer) boardSizeSpinner.getValue();
  }

  public int getSeasonSize() {
    return (Integer) seasonCountSpinner.getValue();
  }
}
