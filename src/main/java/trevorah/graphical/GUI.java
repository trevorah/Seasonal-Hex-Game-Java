package trevorah.graphical;

import trevorah.graphical.boardPanels.HexGroupPanel;
import trevorah.graphical.boardPanels.HexPanel;
import trevorah.gameMechanics.Runner;
import trevorah.gameMechanics.GameRunner;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import trevorah.hexBoards.Board;
import trevorah.graphical.boardPanels.HexGamePanel;
import javax.swing.JLabel;

@SuppressWarnings("serial")
class GUI extends JFrame implements ActionListener {

  private static GUI frame;
  private Thread gameThread;
  private JPanel activeBoardsPanel = null;
  private JPanel buttonPanel = null;
  private JButton startButton = new JButton("Start");
  private Runner game;
  private JPanel auxBoardsPanel;
  private JPanel playBoardPanel;
  private JPanel gameSettings;
  private JPanel settingsPanel;
  private PlayerChoicePanel redPlayerOptions;
  private PlayerChoicePanel bluePlayerOptions;
  private BoardSetupPanel boardSettings;

  private GUI() {

    gameSettings = new JPanel(new GridLayout(3, 1));
    redPlayerOptions = new PlayerChoicePanel("Red");
    bluePlayerOptions = new PlayerChoicePanel("Blue");
    boardSettings = new BoardSetupPanel();

    gameSettings.add(redPlayerOptions);
    gameSettings.add(bluePlayerOptions);
    gameSettings.add(boardSettings);

    buttonPanel = new JPanel(new GridLayout(2, 1));
    startButton.setMnemonic(KeyEvent.VK_SPACE);
    startButton.setActionCommand("start");
    startButton.setEnabled(true);
    startButton.addActionListener(this);

    buttonPanel.add(startButton);

    settingsPanel = new JPanel(new BorderLayout());
    settingsPanel.add(gameSettings, BorderLayout.CENTER);
    settingsPanel.add(buttonPanel, BorderLayout.EAST);

    this.add(settingsPanel, BorderLayout.SOUTH);
  }

  private void prepareGame() {

    int red = redPlayerOptions.getPlayerType();
    String[] redArgs = redPlayerOptions.getArgs();
    int blue = bluePlayerOptions.getPlayerType();
    String[] blueArgs = bluePlayerOptions.getArgs();
    int gameType = boardSettings.getGameType();
    int boardSize = boardSettings.getBoardSize();
    int numberOfSeasons = boardSettings.getSeasonSize();
    game = new GameRunner(boardSize, gameType, numberOfSeasons, red, redArgs, blue, blueArgs);
    gameThread = (Thread) game;
  }

  public void generateBoardPanels() {

    if (activeBoardsPanel != null)
      this.remove(activeBoardsPanel);

    activeBoardsPanel = new JPanel();
    activeBoardsPanel.setLayout(new BorderLayout());

    playBoardPanel = new JPanel();
    playBoardPanel.setLayout(new BorderLayout());

    auxBoardsPanel = new JPanel();
    auxBoardsPanel.setLayout(new GridLayout(2, 1));

    JPanel tickerPanels = new JPanel();
    tickerPanels.setLayout(new GridLayout(2, 1));

    JPanel redPanel = new JPanel();
    redPanel.add(new JLabel("Red:"));
    TurnViewer redTicker = new TurnViewer(game.getSeasonPicker(), Board.RED);

    redTicker.startAnimation();
    redPanel.add(redTicker);
    tickerPanels.add(redPanel);

    JPanel bluePanel = new JPanel();
    bluePanel.add(new JLabel("Blue:"));
    TurnViewer blueTicker = new TurnViewer(game.getSeasonPicker(), Board.BLUE);

    blueTicker.startAnimation();
    bluePanel.add(blueTicker);
    tickerPanels.add(bluePanel);

    HexPanel mainBoardPanel = new HexGamePanel(game.getBoard());
    mainBoardPanel.startAnimation();

    playBoardPanel.add(mainBoardPanel, BorderLayout.CENTER);
    playBoardPanel.add(tickerPanels, BorderLayout.SOUTH);

    activeBoardsPanel.add(playBoardPanel, BorderLayout.CENTER);

    auxBoardsPanel.add(new HexGroupPanel(game.getPlayerRed()));
    auxBoardsPanel.add(new HexGroupPanel(game.getPlayerBlue()));

    activeBoardsPanel.add(auxBoardsPanel, BorderLayout.EAST);

    this.add(activeBoardsPanel, BorderLayout.CENTER);

    frame.pack();
  }

  public void actionPerformed(ActionEvent e) {
    if ("start".equals(e.getActionCommand())) {

      if (game != null)
        game.stopGame();

      this.prepareGame();
      generateBoardPanels();
      gameThread.start();

    }
  }

  public static void main(String[] args) {

    frame = new GUI();
    frame.setTitle("Hex");
    WindowListener l = new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    };
    frame.addWindowListener(l);
    frame.pack();
    frame.setVisible(true);
  }
}
