package tools.dbconnector7.persistence.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.dbconnector7.ui.SimpleRectangle;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UiConfig {
	private int extendedState;
	private SimpleRectangle mainWindowSize;
}
