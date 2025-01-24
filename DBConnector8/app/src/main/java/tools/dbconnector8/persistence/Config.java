package tools.dbconnector8.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import tools.dbconnector8.persistence.config.ConnectionConfig;
import tools.dbconnector8.persistence.config.UiConfig;


@Data
public class Config {
	private List<ConnectionConfig> connections = new ArrayList<>();
	private List<UiConfig> uiConfigs = new ArrayList<>();

	public UiConfig getUiConfig(String label) {
		UiConfig uiConfig = uiConfigs
				.stream()
				.filter(c -> Objects.equals(label, c.getLabel()))
				.findFirst()
				.orElse(null);

		if (Objects.isNull(uiConfig)) {
			uiConfig = UiConfig.builder()
					.label(label)
					.build();
			uiConfigs.add(uiConfig);
		}

		return uiConfig;
	}
}
