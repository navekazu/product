package tools.gitclient.ui;

import org.eclipse.jgit.lib.ProgressMonitor;

import tools.gitclient.OperationMessage;

public class ProgressMonitorPane implements ProgressMonitor {
    private OperationMessage operationMessage;
    private javax.swing.ProgressMonitor progressMonitor;
    private boolean cancelled = false;

    public ProgressMonitorPane(OperationMessage operationMessage) {
        this.operationMessage = operationMessage;
    }

    @Override
    public void start(int totalTasks) {
    }

    @Override
    public void beginTask(String title, int totalWork) {
        progressMonitor = new javax.swing.ProgressMonitor(
                operationMessage.getMainFrame(), title, "", 0, totalWork);
    }

    @Override
    public void update(int completed) {
        if (progressMonitor!=null) {
            progressMonitor.setProgress(completed);
        }
    }

    @Override
    public void endTask() {
        if (progressMonitor!=null) {
            progressMonitor.close();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
        if (progressMonitor!=null) {
            progressMonitor.close();
        }
    }
}
