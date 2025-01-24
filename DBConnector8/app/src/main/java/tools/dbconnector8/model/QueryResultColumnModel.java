package tools.dbconnector8.model;

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
}
