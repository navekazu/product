package tools.dbconnector7.logic;

public class StringConvertor implements DataConvertor {
	@Override
	public String convert() {
		String data = "データベースから取り出した値";
		return "\"" + data + "\"";
	}
}
