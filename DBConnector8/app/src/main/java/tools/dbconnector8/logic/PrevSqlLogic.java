package tools.dbconnector8.logic;

public class PrevSqlLogic extends LogicBase<String, Integer>  {

	
	@Override
	public Integer execute(String i) throws Exception {
		
		if (i.length() == 0) {
			return 0;
		}

		// 最後の1文字は無視する
		int prev = i.length() - 2;
		int index = 0;

		for (index = i.length() - 2; index >= 0; index--) {
			if (i.charAt(index) == '\n') {
				String inter = i.substring(index, prev)
					.replace(" ", "")
					.replace("　", "")
					.replace("\t", "")
					.replace("\r", "")
					.replace("\n", "");

				if (inter.length() == 0) {
					prev ++;	// 空行の後ろの改行の1文字後（行頭）にキャレットを
					break;
				}
				
				prev = index;
			}
		}
		
		
		return (index == -1)? 0: prev;
	}

}
