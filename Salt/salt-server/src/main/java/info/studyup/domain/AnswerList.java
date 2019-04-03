package info.studyup.domain;
import com.gs.fw.finder.Operation;
import java.util.*;
public class AnswerList extends AnswerListAbstract
{
	public AnswerList()
	{
		super();
	}

	public AnswerList(int initialSize)
	{
		super(initialSize);
	}

	public AnswerList(Collection c)
	{
		super(c);
	}

	public AnswerList(Operation operation)
	{
		super(operation);
	}
}
