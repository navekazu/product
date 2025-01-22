package tools.dbconnector8.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class MainFrame extends JFrame implements UiBase {

	public MainFrame() {
		initContents();
	}

	@Override
	public void initContents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("DBConnector 8");

       
        MainMenu menu = new MainMenu();
        setJMenuBar(menu);

        Container contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        DatabaseView dv = new DatabaseView();
        DatabaseOperation dop = new DatabaseOperation();

        JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(dv), dop);
        
        contentPane.add(horizontalSplit, BorderLayout.CENTER);
        
        setContentPane(contentPane);
        
        pack();
	}

}
