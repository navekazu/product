package tools.dbconnector8.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class DatabaseOperation extends JPanel implements UiBase {

	public DatabaseOperation() {
		initContents();
	}
	
	@Override
	public void initContents() {
        setLayout(new BorderLayout());

        QueryExecutor qe = new QueryExecutor();
        ResultView rv = new ResultView();

        JSplitPane horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(qe), new JScrollPane(rv));
        
        add(horizontalSplit, BorderLayout.CENTER);

        StatusBar sb = new StatusBar();
        add(sb, BorderLayout.SOUTH);
	}

}
