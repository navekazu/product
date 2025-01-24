package tools.dbconnector8.persistence.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.dbconnector8.ui.SimpleRectangle;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UiConfig {
	private String label;
	private SimpleRectangle rectangle;
}
