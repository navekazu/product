package tools.dbconnector8.logic;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import tools.dbconnector8.model.ConnectionModel;

public class ReadDbMetaDataLogic extends LogicBase<ConnectionModel, ConnectionModel> {

	@Override
	public ConnectionModel execute(ConnectionModel i) throws SQLException {
		Connection connection = i.getConnection();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet resultSet;
		String catalog = connection.getCatalog();
		String schema = connection.getSchema();

		// テーブル情報取得
		resultSet = meta.getTables(catalog, schema, null, null);
		while (resultSet.next()) {
			String tableType = resultSet.getString("TABLE_TYPE");
			if (!i.getTables().containsKey(tableType)) {
				i.getTables().put(tableType, new ArrayList<>());
			}
			i.getTables().get(tableType).add(resultSet.getString("TABLE_NAME"));
		}
		resultSet.close();
		
		// ファンクション情報取得
		resultSet = meta.getFunctions(catalog, schema, null);
		while (resultSet.next()) {
			i.getFunctions().add(resultSet.getString("FUNCTION_NAME"));
		}
		resultSet.close();

		return i;
	}

}
