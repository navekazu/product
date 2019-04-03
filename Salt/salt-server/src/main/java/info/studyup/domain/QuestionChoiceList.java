package info.studyup.domain;
import com.gs.fw.finder.Operation;
import java.util.*;
public class QuestionChoiceList extends QuestionChoiceListAbstract
{
	public QuestionChoiceList()
	{
		super();
	}

	public QuestionChoiceList(int initialSize)
	{
		super(initialSize);
	}

	public QuestionChoiceList(Collection c)
	{
		super(c);
	}

	public QuestionChoiceList(Operation operation)
	{
		super(operation);
	}
}
