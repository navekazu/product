package tools.dbconnector8.logic;

public abstract class LogicBase<I, O> {

	public LogicBase() {
	}

	public abstract O execute(I i) throws Exception ;
}
