package tools.dbconnector7.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SimpleRectangle {
	/** Rectangleの高さ。 */
	private int	height;

	/** Rectangleの幅。 */
	private int	width;

	/** Rectangleの左上隅のX座標。 */
	private int	x;

	/** Rectangleの左上隅のY座標。 */
	private int	y;
}
