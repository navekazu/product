package tools.salt.domain;
import com.gs.fw.finder.Operation;
import java.util.*;
public class AnswerMasterList extends AnswerMasterListAbstract
{
	public AnswerMasterList()
	{
		super();
	}

	public AnswerMasterList(int initialSize)
	{
		super(initialSize);
	}

	public AnswerMasterList(Collection c)
	{
		super(c);
	}

	public AnswerMasterList(Operation operation)
	{
		super(operation);
	}
}
