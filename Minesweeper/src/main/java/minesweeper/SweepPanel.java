package minesweeper;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class SweepPanel extends JPanel {
	private SquareButton squareButton;
	private ResultPanel resultPanel;
	private MainWindow mainWindow;
	private CardLayout layout;
	private boolean opened;

	public SweepPanel(MainWindow mainWindow, boolean bomber, int hintValue) {
		this.squareButton = new SquareButton(this);
		this.resultPanel = new ResultPanel(this, bomber, hintValue);
		this.mainWindow = mainWindow;
		this.opened = false;
		initContents();
	}

	private void initContents() {
		layout = new CardLayout();
		setLayout(layout);

		add(squareButton);
		add(resultPanel);
		
		// setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		// setBorder(new LineBorder(Color.GRAY));
		setBorder(new MatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
	}
	
	public void resetGame(boolean bomber, int hintValue) {
		squareButton.resetGame();
		resultPanel.resetGame(bomber, hintValue);
		layout.first(this);
		this.opened = false;
	}

	public ResultPanel getResultPanel() {
		return resultPanel;
	}

	public void open() {
		if (resultPanel.isBomber()) {
			resultPanel.clicked();
			mainWindow.endGame();
			return;
		}

		layout.last(this);
		opened = true;

		if (resultPanel.getHintValue() == 0) {
			mainWindow.daisyChainOpen(this);
		}
	}

	public boolean isOpened() {
		return opened;
	}
	
	public void endGame() {
		layout.last(this);
	}
	
	public void aroundOpen() {
		mainWindow.aroundOpen(this);		
	}
	
	public boolean isMarked() {
		return squareButton.isMarked();
	}
}
