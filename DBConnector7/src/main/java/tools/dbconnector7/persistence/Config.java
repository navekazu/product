package tools.dbconnector7.persistence;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import tools.dbconnector7.persistence.config.ConnectionConfig;
import tools.dbconnector7.persistence.config.UiConfig;

@Data
public class Config {
	private List<ConnectionConfig> connections = new ArrayList<>();
	private UiConfig uiConfig = UiConfig.builder().build();
}
