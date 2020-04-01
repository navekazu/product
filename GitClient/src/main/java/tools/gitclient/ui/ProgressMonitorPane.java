package tools.gitclient.ui;

import org.eclipse.jgit.lib.ProgressMonitor;

import tools.gitclient.OperationMessage;

public class ProgressMonitorPane implements ProgressMonitor {
    private OperationMessage operationMessage;
    private String title;
    private javax.swing.ProgressMonitor progressMonitor;
    private boolean cancelled = false;

    public ProgressMonitorPane(String title, OperationMessage operationMessage) {
        this.title = title;
        this.operationMessage = operationMessage;
        progressMonitor = new javax.swing.ProgressMonitor(
                operationMessage.getMainFrame(), title, "", 0, 0);
    }

    @Override
    public void start(int totalTasks) {
        if (progressMonitor!=null) {
            progressMonitor.setMaximum(totalTasks);
        }
    }

    @Override
    public void beginTask(String title, int totalWork) {
        if (progressMonitor!=null) {
            progressMonitor.setNote(title);
        }
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
