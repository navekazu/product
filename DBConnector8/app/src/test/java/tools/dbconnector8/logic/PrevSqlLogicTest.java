package tools.dbconnector8.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrevSqlLogicTest {
	@Test
	public void executeのテスト() throws Exception {
		PrevSqlLogic logic = new PrevSqlLogic();

		assertEquals(0, (int)logic.execute(stringBuilder(
				"select *",
				"from user_mst",
				"where user_id=2"
				)));

		assertEquals(10, (int)logic.execute(stringBuilder(
				"select *",
				"",
				"from user_mst",
				"where user_id=2"
				)));

		assertEquals(24, (int)logic.execute(stringBuilder(
				"select *",
				"from user_mst",
				"",
				"where user_id=2"
				)));
	}

	private String stringBuilder(String ... strings) {
		return String.join("\n", strings);
	}
}
