package tools.dbconnector7.persistence.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConnectionConfig {
	private String label;
	private String libraryPath;
	private String driver;
	private String url;
	private String user;
	private String password;
}
