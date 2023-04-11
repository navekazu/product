package tools.dbconnector7.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import tools.dbconnector7.AppHandle;
import tools.dbconnector7.persistence.config.ConnectionConfig;
import tools.dbconnector7.persistence.config.UiConfig;

public class MainFrame extends JFrame {
	private final AppHandle appHandle;

	public MainFrame(AppHandle appHandle) {
		this.appHandle = appHandle;
		this.appHandle.setMainFrame(this);
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        DatabaseStructure ds = new DatabaseStructure(appHandle.getDatabaseLogic().getDatabaseStructureModel());
        DatabaseOperation dop = new DatabaseOperation();

        JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ds, dop);
        
        contentPane.add(horizontalSplit, BorderLayout.CENTER);
        
        setContentPane(contentPane);

        MainMenu menu = new MainMenu(appHandle);
        setJMenuBar(menu);

        pack();

        
        addWindowListener(new WindowAdapter() {
        	public void windowOpened(WindowEvent e) {
        		try {
        			UiConfig uiConfig = appHandle.getPersistenceManager().getConfig().getUiConfig();

        			setExtendedState(uiConfig.getExtendedState());
        			
        			Rectangle rectangle = new Rectangle(
        					uiConfig.getMainWindowSize().getX(),
        					uiConfig.getMainWindowSize().getY(),
        					uiConfig.getMainWindowSize().getWidth(),
        					uiConfig.getMainWindowSize().getHeight());
        			setBounds(rectangle);
        		} catch(IOException ex) {
        			ex.printStackTrace();
        			
        		}
        		
        	}
        	public void windowClosing(WindowEvent e) {
        		try {
        			UiConfig uiConfig = appHandle.getPersistenceManager().getConfig().getUiConfig();

        			appHandle.getPersistenceManager().getConfig().getConnections().add(
        					ConnectionConfig.builder().label("local-EP1").libraryPath("C:\\User\\k-watanabe\\Projects\\local\\Java\\repositories\\product\\DBConnector6\\extra-lib\\mssql-jdbc-8.2.2.jre8.jar").driver("com.microsoft.sqlserver.jdbc.SQLServerDriver").url("jdbc:sqlserver://localhost:1433;databaseName=WorkingHoursFrom121229").user("WorkingHours").password("WorkingHours").build());
        			appHandle.getPersistenceManager().getConfig().getConnections().add(
        					ConnectionConfig.builder().label("local-EP2").libraryPath("C:\\User\\k-watanabe\\Projects\\local\\Java\\repositories\\product\\DBConnector6\\extra-lib\\postgresql-42.2.14.jar").driver("org.postgresql.Driver").url("jdbc:postgresql://localhost/working_hours").user("working_hours_user").password("working_hours_user!").build());

        			uiConfig.setExtendedState(getExtendedState());
        			uiConfig.setMainWindowSize(getSimpleBounds());
        			appHandle.getPersistenceManager().writeConfig();
        		} catch(IOException ex) {
        			ex.printStackTrace();
        			
        		}
        	}
        	
        });
        
        
/*
        Container contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout());

        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info: infos) {
            JButton btn = new JButton(info.getName());
            final String className = info.getClassName();
            final JFrame mainFrame = this;

            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        UIManager.setLookAndFeel(className);
                        SwingUtilities.updateComponentTreeUI(mainFrame);
                    } catch(ClassNotFoundException exception) {
                        exception.printStackTrace();
                    } catch(InstantiationException exception) {
                        exception.printStackTrace();
                    } catch(IllegalAccessException exception) {
                        exception.printStackTrace();
                    } catch(UnsupportedLookAndFeelException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            contentPane.add(btn);
        }

        setContentPane(contentPane);

        final JFrame mainFrame = this;
        menu = new MainMenu(new NoticeInterface() {
            @Override
            public void notice() {
                SwingUtilities.updateComponentTreeUI(mainFrame);
            }

        }, new NoticeInterface() {
            @Override
            public void notice() {
            }

        }, new NoticeInterface() {
            @Override
            public void notice() {
            }

        } );

        this.setJMenuBar(menu);

        pack();
*/
    }
    
    private SimpleRectangle getSimpleBounds() {
    	Rectangle rectangle = getBounds();
    	return SimpleRectangle.builder()
    			.x(rectangle.x)
    			.y(rectangle.y)
    			.height(rectangle.height)
    			.width(rectangle.width)
    			.build();
    }
}
