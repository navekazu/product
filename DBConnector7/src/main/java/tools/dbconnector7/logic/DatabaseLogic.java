package tools.dbconnector7.logic;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import tools.dbconnector7.AppHandle;
import tools.dbconnector7.persistence.Config;
import tools.dbconnector7.persistence.PersistenceManager;

public class DatabaseLogic {
	private final AppHandle appHandle;
	private DefaultTreeModel databaseStructureModel;

	public DatabaseLogic(AppHandle appHandle) {
		this.appHandle = appHandle;
	}

	public TreeModel getDatabaseStructureModel() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
        databaseStructureModel = new DefaultTreeModel(rootNode);

        readConnectionConfig();

        return databaseStructureModel;
	}

	public void readConnectionConfig() {
		PersistenceManager pm = this.appHandle.getPersistenceManager();

		try {
			DefaultMutableTreeNode rootNode = getDatabaseStructureRootNode();
			rootNode.removeAllChildren();

			Config config = pm.getConfig();
			config.getConnections().stream()
			.forEach(c -> {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(c.getLabel());
				ImageIcon closeIcon = new ImageIcon("./icon/db-disconnected.png");
				rootNode.add(node);
			});

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private DefaultMutableTreeNode getDatabaseStructureRootNode() {
		return (DefaultMutableTreeNode)databaseStructureModel.getRoot();
	}
}
