package tools.dbconnector8.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.model.ConnectionModel;

public class AutocompleteLogic extends LogicBase<String, List<String>> {

	@Override
	public List<String> execute(String i) throws Exception {

		if (Objects.isNull(i) || Objects.equals(i, "")) {
			return new ArrayList<>();
		}
		
		ConnectionModel model = AppHandle.getAppHandle().getCurrentConnectionModel();

		// 一回、Listにまとめる
		List<String> list = new ArrayList<>();
		
		model.getTables().values().stream().forEach(v -> list.addAll(v));
		model.getColumns().values().stream().forEach(v -> list.addAll(v));

		return list.stream()
			.filter(v -> v.toUpperCase().startsWith(i.toUpperCase()))
			.filter(v -> v.length() != i.length())
			.collect(Collectors.toList());
	}

}
