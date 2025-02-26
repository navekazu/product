package tools.dbconnector8.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.persistence.Config;
import tools.dbconnector8.persistence.PersistenceManager;
import tools.dbconnector8.persistence.config.UiConfig;

public class DatabaseOperation extends JPanel implements UiBase {

	public DatabaseOperation() {
		initContents();
	}
	
	@Override
	public void initContents() {
        setLayout(new BorderLayout());

        QueryExecutor qe = new QueryExecutor();
        ResultView rv = new ResultView();

        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(qe), rv);
        
        add(verticalSplit, BorderLayout.CENTER);

        StatusBar sb = new StatusBar();
        add(sb, BorderLayout.SOUTH);

        changeLocation(verticalSplit);

        verticalSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                int newLocation = (int) evt.getNewValue();
                persistDividerSize(newLocation);
            }
        });
        
        AppHandle.getAppHandle().setQueryExecutor(qe);
	}

	private void persistDividerSize(int newLocation) {
		PersistenceManager pm = new PersistenceManager();
		try {
			Config config = pm.getConfig();
			UiConfig uiConfig = config.getUiConfig("DatabaseOperationVerticalSplit");
			uiConfig.setRectangle(SimpleRectangle.builder()
					.x(0)
					.y(0)
					.width(0)
					.height(newLocation)
					.build());

			pm.writeConfig();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private boolean changeLocation(JSplitPane verticalSplit) {
		PersistenceManager pm = new PersistenceManager();
		boolean isChanged = false;

		try {
			Config config = pm.getConfig();
			UiConfig uiConfig;
			SimpleRectangle rectangle;

			uiConfig = config.getUiConfig("DatabaseOperationVerticalSplit");
			rectangle = uiConfig.getRectangle();
			if (Objects.nonNull(rectangle)) {
				verticalSplit.setDividerLocation(rectangle.getHeight());
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		return isChanged;
	}
}
