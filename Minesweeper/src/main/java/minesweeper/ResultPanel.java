package minesweeper;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResultPanel extends JPanel {
	private SweepPanel sweepPanel;
	private boolean bomber;
	private int hintValue;
	private boolean clicked;
	private JLabel label;

	public ResultPanel(SweepPanel sweepPanel, boolean bomber, int hintValue) {
		this.sweepPanel = sweepPanel;
		this.bomber = bomber;
		this.hintValue = hintValue;
		this.clicked = false;
		
		initComponent();
	}

	private void initComponent() {
        setLayout(new GridLayout(1, 1, 0, 0));

        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        changeLabel();
		add(label);
		updateResult(label);

		addMouseListener(new MouseAdapter() {
			private boolean button3Clicked = false;

			public void mousePressed(MouseEvent e){
				if (e.getButton() == MouseEvent.BUTTON1) {
					
					if (button3Clicked) {
						sweepPanel.aroundOpen();
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					button3Clicked = true;
				}
			}

			public void mouseReleased(MouseEvent e){
				if (e.getButton() == MouseEvent.BUTTON3) {
					button3Clicked = false;
				}
			}
		});
	}
	
	private void changeLabel() {
		if (bomber) {
			label.setText("●");
		} else if(hintValue != 0) {
			label.setText(Integer.toString(hintValue));
		} else {
			label.setText("");
		}
	}

	public void resetGame(boolean bomber, int hintValue) {
		this.bomber = bomber;
		this.hintValue = hintValue;
		this.clicked = false;

		changeLabel();
		updateResult(label);
	}

	private void updateResult(JLabel label) {
		if (label != null) {
			Color color;

			switch (hintValue) {
			case 1:
				color = Color.BLUE;
				break;
			case 2:
				// color = Color.GREEN;
				color = new Color(0, 150, 0);
				break;
			case 3:
				color = Color.RED;
				break;
			case 4:
				color = Color.DARK_GRAY;
				break;
			case 5:
				// color = Color.YELLOW;
				color = new Color(150, 150, 0);
				break;
			case 6:
				color = Color.ORANGE;
				break;
			case 7:
				color = Color.MAGENTA;
				break;
			case 8:
				color = new Color(150, 0, 0);
				break;
			default:
				color = Color.BLACK;
				break;
			}

			label.setForeground(color);
		}
		
		if (bomber && clicked) {
			setBackground(Color.RED);
		} else {
			setBackground(null);
		}
	}

	public boolean isBomber() {
		return this.bomber;
	}
	public int getHintValue() {
		return this.hintValue;
	}
	public void clicked() {
		this.clicked = true;
		updateResult(null);
	}

}
