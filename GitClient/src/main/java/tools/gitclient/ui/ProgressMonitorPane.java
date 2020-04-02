package tools.gitclient.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

import org.eclipse.jgit.lib.BatchingProgressMonitor;

import tools.gitclient.OperationMessage;

public class ProgressMonitorPane extends BatchingProgressMonitor implements AutoCloseable {
    private OperationMessage operationMessage;
    private String title;
    private Dialog dialog;
    private boolean cancelled = false;

    public ProgressMonitorPane(String title, OperationMessage operationMessage) {
        this.title = title;
        this.operationMessage = operationMessage;
        createDialog();
    }

    private void createDialog() {
        dialog = new Dialog(title, operationMessage);
        Thread thread = new Thread(dialog);
        thread.start();
    }

    public void cancel() {
        cancelled = true;
    }

    @Override
    protected void onUpdate(String taskName, int workCurr) {
        dialog.setText(taskName);
    }

    @Override
    protected void onEndTask(String taskName, int workCurr) {
    }

    @Override
    protected void onUpdate(String taskName, int workCurr, int workTotal, int percentDone) {
        dialog.setText(taskName);
    }

    @Override
    protected void onEndTask(String taskName, int workCurr, int workTotal, int percentDone) {

    }

    @Override
    public void close() throws Exception {
        dialog.close();
    }

    private static class Dialog implements Runnable {
        private OperationMessage operationMessage;
        private String title;
        private JDialog dialog;
        private JLabel label;

        public Dialog(String title, OperationMessage operationMessage) {
            this.title = title;
            this.operationMessage = operationMessage;
        }

        @Override
        public void run() {
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

        public void setText(String text) {
            if (text.equals(label.getText())) {
                return;
            }
            label.setText(text);
            dialog.pack();
            dialog.repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        }

        public void close() {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }
}

