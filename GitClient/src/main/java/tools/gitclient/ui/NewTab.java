package tools.gitclient.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import tools.gitclient.OperationMessage;

public class NewTab extends Container {
    private OperationMessage operationMessage;

    public NewTab(OperationMessage operationMessage) {
        this.operationMessage = operationMessage;
        createContents();
    }

    private void createContents() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(createOpenRepoPanel());
        add(createRecentRepoPanel());
    }

    private Container createOpenRepoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Open repository"));
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        setDefaultBorder(panel);

        JButton openButton = new JButton("Open a repository");
        setDefaultBorder(openButton);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onOpenRepository();
            }
        });
        panel.add(openButton);

        JButton cloneButton = new JButton("Clone a repository");
        setDefaultBorder(cloneButton);
        cloneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onCloneRepository();
            }
        });
        panel.add(cloneButton);

        JButton startButton = new JButton("Start a local repository");
        setDefaultBorder(startButton);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onStartLocalRepository();
            }
        });
        panel.add(startButton);

        return panel;
    }

    private Container createRecentRepoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Recent repository"));
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        setDefaultBorder(panel);

        return panel;
    }

    private void setDefaultBorder(JComponent component) {
        Border border = new CompoundBorder(new EmptyBorder(3, 3, 3, 3), component.getBorder());
        component.setBorder(border);
    }

    private void onOpenRepository() {
        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File recent = operationMessage.getRecentOpenRepository();
        if (recent!=null) {
            filechooser.setCurrentDirectory(recent);
        }

        int selected = filechooser.showSaveDialog(this);
        if (selected == JFileChooser.APPROVE_OPTION){
            File file = filechooser.getSelectedFile();
            operationMessage.openRepository(file);
        }

    }

    private void onCloneRepository() {

    }

    private void onStartLocalRepository() {

    }

    private void onRecentRepository(int index) {
    }
}
