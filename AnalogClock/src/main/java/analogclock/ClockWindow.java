package analogclock;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClockWindow extends JFrame {

	public ClockWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		var contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());

		var label = new JLabel("aaaaa");
		contentPane.add(label);

		pack();
		contentPane.setVisible(true);

	}
}
