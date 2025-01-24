package tools.dbconnector8.logic;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;

import tools.dbconnector8.model.ConnectionModel;

public class ReadColumnLogic extends LogicBase<String, DefaultMutableTreeNode> {

	private ConnectionModel model;
	
	public ReadColumnLogic(ConnectionModel model) {
		this.model = model;
	}
	
	@Override
	public DefaultMutableTreeNode execute(String i) throws SQLException {
		Connection connection = model.getConnection();
		DatabaseMetaData meta = connection.getMetaData();

		ResultSet columns = meta.getColumns(null, null, i, null);
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Columns");
		
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            int columnSize = columns.getInt("COLUMN_SIZE");
            boolean isNullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable;

            String label = String.format("%s (%s(%d), %s)", columnName, columnType, columnSize, isNullable? "Nullable": "Not Null");
            node.add(new DefaultMutableTreeNode(label));
        }
        
        columns.close();

		return node;
	}

}
