package tools.dbconnector8.ui;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.logic.ReadColumnLogic;
import tools.dbconnector8.model.ConnectionModel;

public class DatabaseView extends JTree implements UiBase {
	private DefaultTreeModel model;
	private DefaultMutableTreeNode rootNode;

	public DatabaseView() {
		AppHandle.getAppHandle().setDatabaseView(this);
		initContents();
	}

	@Override
	public void initContents() {
		rootNode = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(rootNode);
		setModel(model);
//		setRootVisible(false);
		
	}

	public void addDatabase(ConnectionModel model) {
		ReadColumnLogic readColumnLogic = new ReadColumnLogic(model);

		DefaultMutableTreeNode child = new DefaultMutableTreeNode(model.getConnectionConfig().getLabel());
		
		model.getTables().keySet().stream().forEach(k -> {
			DefaultMutableTreeNode tableType = new DefaultMutableTreeNode(k);
			
			model.getTables().get(k).stream().forEach(v -> {
				DefaultMutableTreeNode table = new DefaultMutableTreeNode(v);
/*
				try {
					table.add(readColumnLogic.execute(v));
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
*/
				tableType.add(table);
			});

			child.add(tableType);
		});
		
		DefaultMutableTreeNode function = new DefaultMutableTreeNode("FUNCTION");
		model.getFunctions().stream().forEach(f -> {
			function.add(new DefaultMutableTreeNode(f));
		});
		child.add(function);

		rootNode.add(child);
		this.model.nodeChanged(rootNode);
//		expandRow(0);
	}
}
