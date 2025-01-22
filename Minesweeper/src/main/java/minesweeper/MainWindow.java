package minesweeper;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
	private int rows = 30;
	private int cols = 30;
	private int order = 30;
	
	private boolean bombMap[][];
	private SweepPanel sweepPanel[][];
	private JButton resetRestartButton;
	
	public MainWindow() {
		initContents();
	}
	
	public void initContents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        resetRestartButton = new JButton(" Reset ");
        resetRestartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reserGame();
			}
        });
        controlPanel.add(resetRestartButton);

        Container contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(rows, cols));

        createBombMap();

        sweepPanel = new SweepPanel[cols][rows];
        for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sweepPanel[j][i] = new SweepPanel(this, bombMap[j][i], getHint(j, i));
	        	contentPane.add(sweepPanel[j][i]);
			}
		}
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(contentPane, BorderLayout.CENTER);

        setContentPane(panel);
        pack();
	}

	private void createBombMap() {
		Random random = new Random();
		bombMap = new boolean[cols][rows];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// 四隅には置かない
				if ((i == 0 && j == 0) ||
					(i == 0 && j+1 == cols) ||
					(i+1 == rows && j == 0) ||
					(i+1 == rows && j+1 == cols)) {
					bombMap[j][i] = false;
					continue;
				}
				bombMap[j][i] = random.nextInt(100) < order;
			}
		}
	}

	private void reserGame() {
		createBombMap();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sweepPanel[j][i].resetGame(bombMap[j][i], getHint(j, i));
			}
		}
	}

	public void endGame() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sweepPanel[j][i].endGame();
			}
		}
	}
	
	public void daisyChainOpen(SweepPanel targetSweepPanel) {
		int hint = targetSweepPanel.getResultPanel().getHintValue();
		if (targetSweepPanel.getResultPanel().getHintValue() != 0) {
			return;
		}

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (this.sweepPanel[col][row] != targetSweepPanel) {
					continue;
				}

				// 左上
				if (isNotRightEnd(col) && isNotTopEnd(row)) {
					daisyChainOpen(col-1, row-1);
				}

				// 上
				if (isNotTopEnd(row)) {
					daisyChainOpen(col, row-1);
				}

				// 右上
				if (isNotLeftEnd(col) && isNotTopEnd(row)) {
					daisyChainOpen(col+1, row-1);
				}

				// 左
				if (isNotRightEnd(col)) {
					daisyChainOpen(col-1, row);
				}

				// 右
				if (isNotLeftEnd(col)) {
					daisyChainOpen(col+1, row);
				}

				// 左下
				if (isNotRightEnd(col) && isNotBottomEnd(row)) {
					daisyChainOpen(col-1, row+1);
				}

				// 下
				if (isNotBottomEnd(row)) {
					daisyChainOpen(col, row+1);
				}

				// 右下
				if (isNotLeftEnd(col) && isNotBottomEnd(row)) {
					daisyChainOpen(col+1, row+1);
				}
			}
		}
	}

	private void daisyChainOpen(int col, int row) {
		int hint = this.sweepPanel[col][row].getResultPanel().getHintValue();
		boolean opened = this.sweepPanel[col][row].isOpened();
		boolean bomber = this.sweepPanel[col][row].getResultPanel().isBomber();
		if (!bomber && !opened) {
			this.sweepPanel[col][row].open();
		}
	}
	
	public void aroundOpen(SweepPanel targetSweepPanel) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (this.sweepPanel[col][row] != targetSweepPanel) {
					continue;
				}

				/////////////////////////////////////////////////////////////
				// 周りに開いていなくてマークがついていないマスがあった開けられない
/*
				// 左上
				if (isNotRightEnd(col) && isNotTopEnd(row)) {
					if (!this.sweepPanel[col-1][row-1].isOpened() &&
							!this.sweepPanel[col-1][row-1].isMarked()) {
						this.sweepPanel[col-1][row-1].open();
					}
				}
*/				
				
				/////////////////////////////////////////////////////////////
				// 開ける
				
				// 左上
				if (isNotRightEnd(col) && isNotTopEnd(row)) {
					if (!this.sweepPanel[col-1][row-1].isOpened() &&
							!this.sweepPanel[col-1][row-1].isMarked()) {
						this.sweepPanel[col-1][row-1].open();
					}
				}

				// 上
				if (isNotTopEnd(row)) {
					if (!this.sweepPanel[col][row-1].isOpened() &&
							!this.sweepPanel[col][row-1].isMarked()) {
						this.sweepPanel[col][row-1].open();
					}
				}

				// 右上
				if (isNotLeftEnd(col) && isNotTopEnd(row)) {
					if (!this.sweepPanel[col+1][row-1].isOpened() &&
							!this.sweepPanel[col+1][row-1].isMarked()) {
						this.sweepPanel[col+1][row-1].open();
					}
				}

				// 左
				if (isNotRightEnd(col)) {
					if (!this.sweepPanel[col-1][row].isOpened() &&
							!this.sweepPanel[col-1][row].isMarked()) {
						this.sweepPanel[col-1][row].open();
					}
				}

				// 右
				if (isNotLeftEnd(col)) {
					if (!this.sweepPanel[col+1][row].isOpened() &&
							!this.sweepPanel[col+1][row].isMarked()) {
						this.sweepPanel[col+1][row].open();
					}
				}

				// 左下
				if (isNotRightEnd(col) && isNotBottomEnd(row)) {
					if (!this.sweepPanel[col-1][row+1].isOpened() &&
							!this.sweepPanel[col-1][row+1].isMarked()) {
						this.sweepPanel[col-1][row+1].open();
					}
				}

				// 下
				if (isNotBottomEnd(row)) {
					if (!this.sweepPanel[col][row+1].isOpened() &&
							!this.sweepPanel[col][row+1].isMarked()) {
						this.sweepPanel[col][row+1].open();
					}
				}

				// 右下
				if (isNotLeftEnd(col) && isNotBottomEnd(row)) {
					if (!this.sweepPanel[col+1][row+1].isOpened() &&
							!this.sweepPanel[col+1][row+1].isMarked()) {
						this.sweepPanel[col+1][row+1].open();
					}
				}
			}
		}
	}

	private int getHint(int col, int row) {
		if (bombMap[col][row]) {
			return 0;
		}

		int result = 0;

		// 左上
		if (isNotRightEnd(col) && isNotTopEnd(row)) {
			result += (bombMap[col-1][row-1]? 1: 0);
		}

		// 上
		if (isNotTopEnd(row)) {
			result += (bombMap[col][row-1]? 1: 0);
		}

		// 右上
		if (isNotLeftEnd(col) && isNotTopEnd(row)) {
			result += (bombMap[col+1][row-1]? 1: 0);
		}

		// 左
		if (isNotRightEnd(col)) {
			result += (bombMap[col-1][row]? 1: 0);
		}

		// 右
		if (isNotLeftEnd(col)) {
			result += (bombMap[col+1][row]? 1: 0);
		}

		// 左下
		if (isNotRightEnd(col) && isNotBottomEnd(row)) {
			result += (bombMap[col-1][row+1]? 1: 0);
		}

		// 下
		if (isNotBottomEnd(row)) {
			result += (bombMap[col][row+1]? 1: 0);
		}

		// 右下
		if (isNotLeftEnd(col) && isNotBottomEnd(row)) {
			result += (bombMap[col+1][row+1]? 1: 0);
		}
		
		return result;
	}

	private boolean isNotRightEnd(int col) {
		return !(col == 0);
	}
	private boolean isNotLeftEnd(int col) {
		return !(col+1 == cols);
	}
	private boolean isNotTopEnd(int row) {
		return !(row == 0);
	}
	private boolean isNotBottomEnd(int row) {
		return !(row+1 == rows);
	}
}