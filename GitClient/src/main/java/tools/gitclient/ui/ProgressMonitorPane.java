package tools.gitclient.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

import org.eclipse.jgit.lib.BatchingProgressMonitor;

import tools.gitclient.OperationMessage;

public class ProgressMonitorPane extends BatchingProgressMonitor implements AutoCloseable {
    private OperationMessage operationMessage;
    private String title;
    private JDialog dialog;
    private JLabel label;
    private boolean cancelled = false;

    public ProgressMonitorPane(String title, OperationMessage operationMessage) {
        this.title = title;
        this.operationMessage = operationMessage;
        createDialog();
    }

    private void createDialog() {
        dialog = new JDialog(operationMessage.getMainFrame(), title, false);
        dialog.setLayout(new BorderLayout());

        label = new JLabel("                         ");
        dialog.add(label, BorderLayout.CENTER);

        dialog.pack();
        dialog.setVisible(true);
    }

    public void cancel() {
        cancelled = true;
    }

    @Override
    protected void onUpdate(String taskName, int workCurr) {
        if (taskName.equals(label.getText())) {
            return;
        }

        label.setText(taskName);
        dialog.pack();
        dialog.repaint();
    }

    @Override
    protected void onEndTask(String taskName, int workCurr) {
    }

    @Override
    protected void onUpdate(String taskName, int workCurr, int workTotal, int percentDone) {
        if (taskName.equals(label.getText())) {
            return;
        }

        label.setText(taskName);
        dialog.pack();
        dialog.repaint();
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
