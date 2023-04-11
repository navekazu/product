package tools.dbconnector7.ui;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

public class DatabaseStructure extends JTree {
    public DatabaseStructure(TreeModel model) {
    	super(model);
    	setRootVisible(false);
    }

}
