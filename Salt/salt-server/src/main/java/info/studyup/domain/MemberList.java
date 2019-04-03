package info.studyup.domain;
import com.gs.fw.finder.Operation;
import java.util.*;
public class MemberList extends MemberListAbstract
{
	public MemberList()
	{
		super();
	}

	public MemberList(int initialSize)
	{
		super(initialSize);
	}

	public MemberList(Collection c)
	{
		super(c);
	}

	public MemberList(Operation operation)
	{
		super(operation);
	}
}
