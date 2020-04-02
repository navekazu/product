package tools.gitclient.ui;

import org.eclipse.jgit.lib.BatchingProgressMonitor;

import tools.gitclient.OperationMessage;

public class ProgressMonitorPane extends BatchingProgressMonitor implements AutoCloseable {
    private OperationMessage operationMessage;
    private String title;
    private javax.swing.ProgressMonitor progressMonitor;
    private boolean cancelled = false;

    public ProgressMonitorPane(String title, OperationMessage operationMessage) {
        this.title = title;
        this.operationMessage = operationMessage;
        progressMonitor = new javax.swing.ProgressMonitor(
                operationMessage.getMainFrame(), title, "", 0, 10);
        progressMonitor.setMillisToPopup(0);
    }

    public void cancel() {
        cancelled = true;
        if (progressMonitor!=null) {
            progressMonitor.close();
        }
    }

    @Override
    protected void onUpdate(String taskName, int workCurr) {
        progressMonitor.setNote(taskName);
    }

    @Override
    protected void onEndTask(String taskName, int workCurr) {
        progressMonitor.setNote(taskName);
    }

    @Override
    protected void onUpdate(String taskName, int workCurr, int workTotal, int percentDone) {
        progressMonitor.setNote(taskName);
        progressMonitor.setMaximum(workTotal);
        progressMonitor.setProgress(workCurr);
    }

    @Override
    protected void onEndTask(String taskName, int workCurr, int workTotal, int percentDone) {
        progressMonitor.setNote(taskName);
        progressMonitor.setMaximum(workTotal);
        progressMonitor.setProgress(workCurr);
    }

    @Override
    public void close() throws Exception {
        // TODO 自動生成されたメソッド・スタブ
        progressMonitor.close();
    }
}
