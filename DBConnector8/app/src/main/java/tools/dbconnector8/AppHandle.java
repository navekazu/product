package tools.dbconnector8;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import tools.dbconnector8.model.ConnectionModel;
import tools.dbconnector8.ui.DatabaseView;
import tools.dbconnector8.ui.MainFrame;
import tools.dbconnector8.ui.QueryExecutor;
import tools.dbconnector8.ui.ResultView;
import tools.dbconnector8.ui.StatusBar;

@Data
public class AppHandle {
	private static AppHandle appHandle = null;

	private AppHandle() {
	}

	public static AppHandle getAppHandle() {
		if (appHandle == null) {
			appHandle = new AppHandle();
		}

		return appHandle;
	}

	public void addConnectionModel(ConnectionModel model) {
		connectionModels.add(model);
		currentConnectionModel = model;
		databaseView.addDatabase(model);
	}
	
	private MainFrame mainFrame;
	private DatabaseView databaseView;
	private QueryExecutor queryExecutor;
	private ResultView resultView;
	private StatusBar statusBar;
	private App app;
	private List<ConnectionModel> connectionModels = new ArrayList<>();
	private ConnectionModel currentConnectionModel;

}
