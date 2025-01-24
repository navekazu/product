package tools.dbconnector8.model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.dbconnector8.persistence.config.ConnectionConfig;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConnectionModel {
	private Connection connection;
	private ConnectionConfig connectionConfig;
	private Map<String, List<String>> tables;
	private List<String> functions;
}
