package tools.dbconnector7.ui;

import lombok.Builder;
import lombok.Getter;
import tools.dbconnector7.NoticeInterface;

@Getter
@Builder
public class MenuNoticeEntity {
	private NoticeInterface fileExit;

	private NoticeInterface databaseConnect;
    private NoticeInterface databaseDisconnect;
    private NoticeInterface databaseOpenQueryField;
    private NoticeInterface databaseExecuteQuery;
    private NoticeInterface databaseCancelQuery;
    private NoticeInterface databaseQueryScript;
    private NoticeInterface databaseCommit;
    private NoticeInterface databaseRollback;

}
