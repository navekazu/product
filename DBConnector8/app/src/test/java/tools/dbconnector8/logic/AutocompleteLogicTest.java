package tools.dbconnector8.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.model.ConnectionModel;

public class AutocompleteLogicTest {

	public AutocompleteLogicTest() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Test
	public void executeのテスト() throws Exception {
		AutocompleteLogic logic = new AutocompleteLogic();

		Map<String, List<String>> tables = new HashMap<>();
		
		tables.put("TABLES", Arrays.asList("abc", "abcd", "abcde"));

		ConnectionModel currentConnectionModel = ConnectionModel.builder()
				.tables(tables)
				.build();
		
		AppHandle.getAppHandle().setCurrentConnectionModel(currentConnectionModel);

		List<String> list;
		
		// ３つヒット
		list = logic.execute("a");
		assertEquals(3, list.size());

		// ３つヒット
		list = logic.execute("ab");
		assertEquals(3, list.size());

		// ３つヒット
		list = logic.execute("abc");
		assertEquals(3, list.size());

		// 2つヒット
		list = logic.execute("abcd");
		assertEquals(2, list.size());

		// 1つヒット
		list = logic.execute("abcde");
		assertEquals(1, list.size());

		// 0ヒット
		list = logic.execute("abcdef");
		assertEquals(0, list.size());

		// 0ヒット
		list = logic.execute("z");
		assertEquals(0, list.size());

	
		
		// ３つヒット
		list = logic.execute("A");
		assertEquals(3, list.size());

		// ３つヒット
		list = logic.execute("AB");
		assertEquals(3, list.size());

		// ３つヒット
		list = logic.execute("ABC");
		assertEquals(3, list.size());

		// 2つヒット
		list = logic.execute("ABCD");
		assertEquals(2, list.size());

		// 1つヒット
		list = logic.execute("ABCDE");
		assertEquals(1, list.size());

		// 0ヒット
		list = logic.execute("ABCDEF");
		assertEquals(0, list.size());

		// 0ヒット
		list = logic.execute("Z");
		assertEquals(0, list.size());
	}

}
