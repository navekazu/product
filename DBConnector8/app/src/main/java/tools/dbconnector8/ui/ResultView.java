package tools.dbconnector8.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.model.ConnectionModel;
import tools.dbconnector8.model.QueryResultColumnModel;
import tools.dbconnector8.model.QueryResultModel;

public class ResultView extends JTabbedPane implements UiBase {
	private List<JScrollPane> resultTables;
	private JTextArea messageArea;

	public ResultView() {
		AppHandle.getAppHandle().setResultView(this);
		initContents();
	}

	@Override
	public void initContents() {
		resultTables = new ArrayList<>();
		messageArea = new JTextArea();

		addTab("Message", new JScrollPane(messageArea));
	}

	public void executeQuery(String query) {
		clearResultTable();
		
		SwingWorker<QueryResultModel, List<QueryResultColumnModel>> worker = new SwingWorker<>() {
			private JTable resultTable = null;

			@Override
            protected QueryResultModel doInBackground() throws Exception {
       			// バックグラウンドで実行する処理

				ConnectionModel connectionModel = AppHandle.getAppHandle().getCurrentConnectionModel();
            	Connection connection = connectionModel.getConnection();
            	LocalDateTime executeStart = LocalDateTime.now();
            	
            	try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            	if (!preparedStatement.execute()) {
	            		// ResultSetなし
	            		return QueryResultModel.builder()
	            				.withResultSet(false)
	            				.count(preparedStatement.getUpdateCount())
	            				.executeStart(executeStart)
	            				.executeEnd(LocalDateTime.now())
	            				.build();
	            	}
	
	            	List<QueryResultColumnModel> columnModels;

	            	// ResultSetあり
	            	try (ResultSet resultSet = preparedStatement.getResultSet()) {
            			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            			// カラム情報取得
            			int columnCount = resultSetMetaData.getColumnCount();
            			columnModels = new ArrayList<>();
            			for (int i = 1; i <= columnCount; i++) {
            				columnModels.add(
            						QueryResultColumnModel.builder()
            							.value(resultSetMetaData.getColumnName(i))
            							.type(resultSetMetaData.getColumnType(i))
            							.build()
           							);
            				
            			}
            			publish(columnModels);

            			// データ取得
            			int rowCount = 0;
            			while (resultSet.next()) {
                			columnModels = new ArrayList<>();

                			for (int i = 1; i <= columnCount; i++) {
                				String value = resultSet.getString(i);
                				boolean wasNull = resultSet.wasNull();
                				columnModels.add(
                						QueryResultColumnModel.builder()
                							.value(wasNull? "(NULL)": value)
                							.type(resultSetMetaData.getColumnType(i))
                							.wasNull(wasNull)
                							.build()
               							);
                				
                			}
                			
                			publish(columnModels);
                			rowCount++;
            			}

            			return QueryResultModel.builder()
                				.withResultSet(true)
                				.count(rowCount)
                				.executeStart(executeStart)
                				.executeEnd(LocalDateTime.now())
                				.build();
	            	}

            	} catch (Exception e) {
            		messageArea.setText(e.getMessage());
	        		throw e;
	        	}
            }

            @Override
            protected void process(List<List<QueryResultColumnModel>> chunks) {
            	if (Objects.isNull(resultTable)) {
            		// 表を作ってカラムを追加する
            		// chunksの1行目を列情報として処理する
            		resultTable = createResultTable();
            		DefaultTableModel tableModel = (DefaultTableModel)resultTable.getModel();
                    
            		for (List<QueryResultColumnModel> columnModels : chunks) {
                		for (QueryResultColumnModel columnModel : columnModels) {
                    		tableModel.addColumn(columnModel.getValue());
                		}

                		chunks.remove(0);
                		break;
            		}

            		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                            QueryResultColumnModel columnModel = (QueryResultColumnModel)value;

                            if (columnModel.isWasNull()) {
                            	setHorizontalAlignment(SwingConstants.CENTER);
                            	c.setForeground(Color.BLUE);

                            } else if(columnModel.isNumberValue()) {
                            	setHorizontalAlignment(SwingConstants.RIGHT);
                            	c.setForeground(Color.BLACK);
                            
                            } else {
                            	setHorizontalAlignment(SwingConstants.LEFT);
                            	c.setForeground(Color.BLACK);
                            }
                            
                            return c;
                        }
                    };
                    for (int i = 0; i < resultTable.getColumnModel().getColumnCount(); i++) {
                    	resultTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
                    }
            	}
                
                // データ部分
        		DefaultTableModel tableModel = (DefaultTableModel)resultTable.getModel();
        		FontMetrics metrics = resultTable.getFontMetrics(resultTable.getFont());
        		int minWidth = 10; // 最小幅
        		int maxWidth = 300; // 最大幅

        		for (List<QueryResultColumnModel> columnModels : chunks) {
        			List<QueryResultColumnModel> row = new ArrayList<>();

        			for (int i = 0; i < columnModels.size(); i++ ) {
        				QueryResultColumnModel columnModel = columnModels.get(i);

        				row.add(columnModel);

        				int width = metrics.stringWidth(columnModel.getValue()) + 10; // 余白を追加
                        width = Math.max(minWidth, width);
                        TableColumn column = resultTable.getColumnModel().getColumn(i);
                        column.setPreferredWidth(Math.min(Math.max(column.getPreferredWidth(), width), maxWidth));
                    }

        			tableModel.addRow(row.toArray());
        		}
            }

            @Override
            protected void done() {
                // 処理完了後に UI を更新
				try {
	            	QueryResultModel queryResultModel = get();
	            	Duration duration = Duration.between(queryResultModel.getExecuteStart(), queryResultModel.getExecuteEnd());
	            	String message = String.format(
	            			"%,d件、%sしました。 所要時間 %02d:%02d:%02d.%03d"
	        				, queryResultModel.getCount()
	        				, queryResultModel.isWithResultSet()? "取得": "更新"
	        				, duration.toHours()
	        				, duration.toMinutes() % 60
	        				, duration.getSeconds() % 60
	        				, duration.toMillis() % 1000);

	            	AppHandle.getAppHandle().getStatusBar().setText(message);;

				} catch (InterruptedException | ExecutionException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
            }
        };

        worker.execute(); // タスクの開始
    }
	
	private void clearResultTable() {
		resultTables.stream().forEach(v -> {
			for (int i = 0; i < getTabCount(); i++) {
				if (v == getComponentAt(i)) {
					this.remove(i);
					break;
				}
			}
		});
		
		messageArea.setText("");
	}

	private JTable createResultTable() {
		
		JTable table = new JTable(new DefaultTableModel());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		
		JScrollPane scrollPane = new JScrollPane(table);
		resultTables.add(scrollPane);
		
		addTab("Result", scrollPane);
		setSelectedComponent(scrollPane);

		return table;
	}
}
