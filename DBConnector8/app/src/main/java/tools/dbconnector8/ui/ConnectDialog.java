package tools.dbconnector8.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.logic.ConnectionLogic;
import tools.dbconnector8.logic.ReadDbMetaDataLogic;
import tools.dbconnector8.model.ConnectionModel;
import tools.dbconnector8.persistence.Config;
import tools.dbconnector8.persistence.PersistenceManager;
import tools.dbconnector8.persistence.config.ConnectionConfig;
import tools.dbconnector8.persistence.config.UiConfig;

public class ConnectDialog extends JDialog implements UiBase {
	private DefaultTableModel tableModel;

	private JTextField labelField;
	private JTextField libraryField;
	private JTextField driverField;
	private JTextField urlField;
	private JTextField userField;
	private JPasswordField passwordField;

	public ConnectDialog() {
		initContents();
	}

	@Override
	public void initContents() {
		setTitle("Connect");

		setLayout(new BorderLayout());

		add(createConnectionsPanel(), BorderLayout.CENTER);
		add(createInputPanel(), BorderLayout.SOUTH);
	
        setModal(true);

        if (!changeLocation()) {
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
	}

	private JPanel createConnectionsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		tableModel = new DefaultTableModel();
		JTable table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // TableCellEditorを設定して編集不可にする
        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            public boolean isCellEditable(EventObject anEvent) {
                return false; // 編集不可にする
            }
        });
        
        // MouseListenerを追加してダブルクリックイベントを処理
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // ダブルクリックの判定
                if (e.getClickCount() != 2) {
                	return;
                }

                int row = table.getSelectedRow();

            	labelField.setText((String) tableModel.getValueAt(row, 0));
            	libraryField.setText((String) tableModel.getValueAt(row, 1));
            	driverField.setText((String) tableModel.getValueAt(row, 2));
            	urlField.setText((String) tableModel.getValueAt(row, 3));
            	userField.setText((String) tableModel.getValueAt(row, 4));
            	passwordField.setText((String) tableModel.getValueAt(row, 5));
            }
        });
        
		tableModel.addColumn("Label");
		tableModel.addColumn("Library");
		tableModel.addColumn("Driver");
		tableModel.addColumn("URL");
		tableModel.addColumn("User");
		tableModel.addColumn("Password");

		// パスワードを削除（モデルには残るから値の取得は可能
		TableColumn hiddenColumn = table.getColumnModel().getColumn(5);
		table.removeColumn(hiddenColumn);

		try {
			PersistenceManager pm = PersistenceManager.getPersistenceManager();
			Config config = pm.getConfig();
			List<ConnectionConfig> connections = config.getConnections();
			
			connections.stream()
				.forEach(c -> {
        			List<String> row = new ArrayList<>();

    				row.add(c.getLabel());
    				row.add(c.getLibraryPath());
    				row.add(c.getDriver());
    				row.add(c.getUrl());
    				row.add(c.getUser());
    				row.add(c.getPassword());

        			tableModel.addRow(row.toArray());
					
				});

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		JPanel buttonPanel = new JPanel();

		JButton addButton = new JButton("Add");
		JButton updateButton = new JButton("Update");
		JButton deleteButton = new JButton("Delete");
		
		buttonPanel.add(addButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PersistenceManager pm = PersistenceManager.getPersistenceManager();
				Config config;
				char[] password = passwordField.getPassword();

				try {
					config = pm.getConfig();
					List<ConnectionConfig> connections = config.getConnections();
					connections.add(ConnectionConfig.builder()
							.label(labelField.getText())
							.libraryPath(libraryField.getText())
							.driver(driverField.getText())
							.url(urlField.getText())
							.user(userField.getText())
							.password(new String(password))
							.build());

					pm.writeConfig();
					
					tableModel.addRow(new String[] {
							labelField.getText(),
							libraryField.getText(),
							driverField.getText(),
							urlField.getText(),
							userField.getText(),
							new String(password)
					});

				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		});

		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
					return;
				}

				PersistenceManager pm = PersistenceManager.getPersistenceManager();
				Config config;
				char[] password = passwordField.getPassword();

				try {
					config = pm.getConfig();
					List<ConnectionConfig> connections = config.getConnections();
					connections.add(row,
							ConnectionConfig.builder()
							.label(labelField.getText())
							.libraryPath(libraryField.getText())
							.driver(driverField.getText())
							.url(urlField.getText())
							.user(userField.getText())
							.password(new String(password))
							.build());
					connections.remove(row+1);

					pm.writeConfig();

					tableModel.setValueAt(labelField.getText(), row, 0);
					tableModel.setValueAt(libraryField.getText(), row, 1);
					tableModel.setValueAt(driverField.getText(), row, 2);
					tableModel.setValueAt(urlField.getText(), row, 3);
					tableModel.setValueAt(userField.getText(), row, 4);
					tableModel.setValueAt(new String(password), row, 5);
					
							
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
					return;
				}

				PersistenceManager pm = PersistenceManager.getPersistenceManager();
				Config config;
				char[] password = passwordField.getPassword();

				try {
					config = pm.getConfig();
					List<ConnectionConfig> connections = config.getConnections();
					connections.remove(row);

					pm.writeConfig();

					tableModel.removeRow(row);
							
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		});
		
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel createInputPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(6, 1, 3, 3));
		labelPanel.add(new JLabel("Label"));
		labelPanel.add(new JLabel("Library"));
		labelPanel.add(new JLabel("Driver"));
		labelPanel.add(new JLabel("URL"));
		labelPanel.add(new JLabel("User"));
		labelPanel.add(new JLabel("Password"));
		panel.add(labelPanel, BorderLayout.WEST);

		JPanel inputPanel = new JPanel();

		labelField = new JTextField("ローカルのSQLServer");
		libraryField = new JTextField("C:\\User\\k-watanabe\\Projects\\local\\Java\\repositories\\product\\DBConnector6\\extra-lib\\mssql-jdbc-8.2.2.jre8.jar");
		driverField = new JTextField("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		urlField = new JTextField("jdbc:sqlserver://localhost:1433;databaseName=WorkingHoursFrom121229");
		userField = new JTextField("WorkingHours");
		passwordField = new JPasswordField("WorkingHours!");

		JButton referenceButton = new JButton("...");

		JPanel libraryPanel = new JPanel();
		libraryPanel.setLayout(new BorderLayout());
		libraryPanel.add(libraryField, BorderLayout.CENTER);
		libraryPanel.add(referenceButton, BorderLayout.EAST);
		
		inputPanel.setLayout(new GridLayout(6, 1, 3, 3));
		inputPanel.add(labelField);
		inputPanel.add(libraryPanel);
		inputPanel.add(driverField);
		inputPanel.add(urlField);
		inputPanel.add(userField);
		inputPanel.add(passwordField);
		panel.add(inputPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton connectionButton = new JButton("Connect");
		JButton cancelButton = new JButton("Cancel");
		JButton testButton = new JButton("Test");
		buttonPanel.add(connectionButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(testButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		Component parent = this;

		connectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConnectionLogic logic = new ConnectionLogic();
				char[] password = passwordField.getPassword();
				ReadDbMetaDataLogic metaDataLogic = new ReadDbMetaDataLogic();

				try {
					ConnectionModel model = logic.execute(ConnectionConfig.builder()
							.label(labelField.getText())
							.libraryPath(libraryField.getText())
							.driver(driverField.getText())
							.url(urlField.getText())
							.user(userField.getText())
							.password(new String(password))
							.build());

					JOptionPane.showMessageDialog(parent, "Connection successful.", "OK", JOptionPane.INFORMATION_MESSAGE);

					metaDataLogic.execute(model);
					AppHandle.getAppHandle().addConnectionModel(model);
					dispose();

				} catch (MalformedURLException | ClassNotFoundException | InstantiationException
						| IllegalAccessException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					JOptionPane.showMessageDialog(parent, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConnectionLogic logic = new ConnectionLogic();
				char[] password = passwordField.getPassword();
				ReadDbMetaDataLogic metaDataLogic = new ReadDbMetaDataLogic();

				try {
					ConnectionModel model = logic.execute(ConnectionConfig.builder()
							.label(labelField.getText())
							.libraryPath(libraryField.getText())
							.driver(driverField.getText())
							.url(urlField.getText())
							.user(userField.getText())
							.password(new String(password))
							.build());

					JOptionPane.showMessageDialog(parent, "Connection successful.", "OK", JOptionPane.INFORMATION_MESSAGE);

					model.getConnection().close();

				} catch (MalformedURLException | ClassNotFoundException | InstantiationException
						| IllegalAccessException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					JOptionPane.showMessageDialog(parent, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return panel;
	}

	private void persistWindowSize() {
		PersistenceManager pm = PersistenceManager.getPersistenceManager();
		try {
			Config config = pm.getConfig();
			UiConfig uiConfig = config.getUiConfig("ConnectDialog");
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

	private boolean changeLocation() {
		PersistenceManager pm = PersistenceManager.getPersistenceManager();
		boolean isChanged = false;

		try {
			Config config = pm.getConfig();
			UiConfig uiConfig;
			SimpleRectangle rectangle;

			uiConfig = config.getUiConfig("ConnectDialog");
			rectangle = uiConfig.getRectangle();
			if (Objects.nonNull(rectangle)) {
				setLocation(rectangle.getX(), rectangle.getY());
				setSize(rectangle.getWidth(), rectangle.getHeight());
				isChanged = true;
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		return isChanged;
	}
}
