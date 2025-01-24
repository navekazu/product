package tools.dbconnector8.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import tools.dbconnector8.persistence.Config;
import tools.dbconnector8.persistence.PersistenceManager;
import tools.dbconnector8.persistence.config.UiConfig;

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

		if (!changeLocation(horizontalSplit)) {
			pack();
		}

        addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				persistWindowSize();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				persistWindowSize();
			}
		});

		horizontalSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                int newLocation = (int) evt.getNewValue();
                persistDividerSize(newLocation);
            }
        });
        
	}

	private void persistWindowSize() {
		PersistenceManager pm = new PersistenceManager();
		try {
			Config config = pm.getConfig();
			UiConfig uiConfig = config.getUiConfig("MainFrame");
			Point point = getLocation();
			Dimension dimension = getSize();
			uiConfig.setRectangle(SimpleRectangle.builder()
					.x(point.x)
					.y(point.y)
					.width(dimension.width)
					.height(dimension.height)
					.build());
			
			pm.writeConfig();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void persistDividerSize(int newLocation) {
		PersistenceManager pm = new PersistenceManager();
		try {
			Config config = pm.getConfig();
			UiConfig uiConfig = config.getUiConfig("MainFrameHorizontalSplit");
			uiConfig.setRectangle(SimpleRectangle.builder()
					.x(0)
					.y(0)
					.width(newLocation)
					.height(0)
					.build());

			pm.writeConfig();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private boolean changeLocation(JSplitPane horizontalSplit) {
		PersistenceManager pm = new PersistenceManager();
		boolean isChanged = false;

		try {
			Config config = pm.getConfig();
			UiConfig uiConfig;
			SimpleRectangle rectangle;

			uiConfig = config.getUiConfig("MainFrame");
			rectangle = uiConfig.getRectangle();
			if (Objects.nonNull(rectangle)) {
				setLocation(rectangle.getX(), rectangle.getY());
				setSize(rectangle.getWidth(), rectangle.getHeight());
				isChanged = true;
			}

			uiConfig = config.getUiConfig("MainFrameHorizontalSplit");
			rectangle = uiConfig.getRectangle();
			if (Objects.nonNull(rectangle)) {
				horizontalSplit.setDividerLocation(rectangle.getWidth());
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		return isChanged;
	}
}
