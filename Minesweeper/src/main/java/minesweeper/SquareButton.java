package minesweeper;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class SquareButton extends JButton {
	private boolean marked;
	private SweepPanel sweepPanel;
	
	public SquareButton(SweepPanel sweepPanel) {
		this.sweepPanel = sweepPanel;
		this.marked = false;
		initContents();
	}

	public void initContents() {
		setMargin(new Insets(0, 0, 0, 0));
		
		Dimension dim = new Dimension();
		dim.height = 15;
		dim.width = 15;
		setPreferredSize(dim);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				if (e.getButton() == MouseEvent.BUTTON1) {
					sweepPanel.open();
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					turnMarked();
				}
			}
		});
	}

	public void resetGame() {
		this.marked = false;
		setText("");
	}

	public void turnMarked() {
		this.marked = !this.marked;
		setText(marked? "P": "");
	}
	
	public boolean isMarked() {
		return this.marked;
	}
}
