package tools.dbconnector8.model;

import java.sql.Types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class QueryResultColumnModel {
	private String value;
	private int type;
	private boolean wasNull;
	
	@Override
	public String toString() {
		return value;
	}

	public boolean isNumberValue() {
		switch (type) {
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.REAL:
		case Types.SMALLINT:
		case Types.TINYINT:
			return true;
		}

		return false;
	}
}
