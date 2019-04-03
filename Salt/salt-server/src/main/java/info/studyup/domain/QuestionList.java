package info.studyup.domain;
import com.gs.fw.finder.Operation;
import java.util.*;
public class QuestionList extends QuestionListAbstract
{
	public QuestionList()
	{
		super();
	}

	public QuestionList(int initialSize)
	{
		super(initialSize);
	}

	public QuestionList(Collection c)
	{
		super(c);
	}

	public QuestionList(Operation operation)
	{
		super(operation);
	}
}
