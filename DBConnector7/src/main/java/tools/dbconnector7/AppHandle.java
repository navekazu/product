package tools.dbconnector7;

import lombok.Data;
import tools.dbconnector7.logic.DatabaseLogic;
import tools.dbconnector7.persistence.PersistenceManager;
import tools.dbconnector7.ui.MainFrame;

@Data
public class AppHandle {
	private MainFrame mainFrame;

	private PersistenceManager persistenceManager;
	
	private DatabaseLogic databaseLogic;
}
