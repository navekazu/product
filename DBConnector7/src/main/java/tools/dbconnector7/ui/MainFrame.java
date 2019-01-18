package tools.dbconnector7.ui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tools.dbconnector7.NoticeInterface;

public class MainFrame extends JFrame {
	public MainMenu menu;

	public MainFrame() {
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout());

        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info: infos) {
            JButton btn = new JButton(info.getName());
            final String className = info.getClassName();
            final JFrame mainFrame = this;

            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        UIManager.setLookAndFeel(className);
                        SwingUtilities.updateComponentTreeUI(mainFrame);
                    } catch(ClassNotFoundException exception) {
                        exception.printStackTrace();
                    } catch(InstantiationException exception) {
                        exception.printStackTrace();
                    } catch(IllegalAccessException exception) {
                        exception.printStackTrace();
                    } catch(UnsupportedLookAndFeelException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            contentPane.add(btn);
        }

        setContentPane(contentPane);

        final JFrame mainFrame = this;
        menu = new MainMenu(new NoticeInterface() {
            @Override
            public void notice() {
                SwingUtilities.updateComponentTreeUI(mainFrame);
            }

        }, new NoticeInterface() {
            @Override
            public void notice() {
            }

        }, new NoticeInterface() {
            @Override
            public void notice() {
            }

        } );

        this.setJMenuBar(menu);

        pack();
    }
}
