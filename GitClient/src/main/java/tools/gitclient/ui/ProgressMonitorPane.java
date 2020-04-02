package tools.gitclient.ui;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.jgit.lib.BatchingProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;

import tools.gitclient.OperationMessage;

//public class ProgressMonitorPane implements ProgressMonitor {
public class ProgressMonitorPane extends BatchingProgressMonitor {
    private OperationMessage operationMessage;
    private String title;
    private javax.swing.ProgressMonitor progressMonitor;
    private boolean cancelled = false;
    private PrintWriter pw;

    public ProgressMonitorPane(String title, OperationMessage operationMessage) {
        this.title = title;
        this.operationMessage = operationMessage;
        progressMonitor = new javax.swing.ProgressMonitor(
                operationMessage.getMainFrame(), title, "", 0, 100);
        progressMonitor.setMillisToPopup(0);
        TextProgressMonitor tpm ;

        try {
            pw = new PrintWriter(new FileWriter("ProgressMonitorPane.txt"));
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
/*
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
*/
    public void cancel() {
        cancelled = true;
        if (progressMonitor!=null) {
            progressMonitor.close();
        }
    }

    @Override
    protected void onUpdate(String taskName, int workCurr) {
        // TODO 自動生成されたメソッド・スタブ
        pw.println("onUpdate(\""+taskName+"\", "+workCurr+");");
    }

    @Override
    protected void onEndTask(String taskName, int workCurr) {
        // TODO 自動生成されたメソッド・スタブ
        pw.println("onEndTask(\""+taskName+"\", "+workCurr+");");
    }

    @Override
    protected void onUpdate(String taskName, int workCurr, int workTotal, int percentDone) {
        // TODO 自動生成されたメソッド・スタブ
        pw.println("onUpdate(\""+taskName+"\", "+workCurr+", "+workTotal+", "+percentDone+");");
    }

    @Override
    protected void onEndTask(String taskName, int workCurr, int workTotal, int percentDone) {
        // TODO 自動生成されたメソッド・スタブ
        pw.println("onEndTask(\""+taskName+"\", "+workCurr+", "+workTotal+", "+percentDone+");");
    }
}
