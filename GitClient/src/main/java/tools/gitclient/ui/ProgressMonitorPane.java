package tools.gitclient.ui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.jgit.lib.BatchingProgressMonitor;

import tools.gitclient.OperationMessage;

public class ProgressMonitorPane extends BatchingProgressMonitor implements AutoCloseable {
    private OperationMessage operationMessage;
    private String title;
    private JOptionPane optionPane;
    private JDialog dialog;
    private boolean cancelled = false;

    public ProgressMonitorPane(String title, OperationMessage operationMessage) {
        this.title = title;
        this.operationMessage = operationMessage;
        this.optionPane = new JOptionPane();
        dialog = optionPane.createDialog(title);
        dialog.setModal(false);
        dialog.setVisible(true);
    }

    public void cancel() {
        cancelled = true;
    }

    @Override
    protected void onUpdate(String taskName, int workCurr) {
        optionPane.setMessage(taskName);
    }

    @Override
    protected void onEndTask(String taskName, int workCurr) {
    }

    @Override
    protected void onUpdate(String taskName, int workCurr, int workTotal, int percentDone) {
        optionPane.setMessage(taskName);
    }

    @Override
    protected void onEndTask(String taskName, int workCurr, int workTotal, int percentDone) {
    }

    @Override
    public void close() throws Exception {
        // TODO 自動生成されたメソッド・スタブ
        dialog.setVisible(false);
        dialog.dispose();
    }
}
