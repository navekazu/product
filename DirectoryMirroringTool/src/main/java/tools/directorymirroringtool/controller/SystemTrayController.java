package tools.directorymirroringtool.controller;

import tools.directorymirroringtool.SystemTrayEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SystemTrayController {
    private TrayIcon icon;
    public void startOnTray(SystemTrayEvent eventHandler) {

        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(getClass().getResource("/icon.png"));

            PopupMenu menu = new PopupMenu("SystemTray");

            MenuItem showWindowItem = new MenuItem("Show window");
            showWindowItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    eventHandler.showWindow();
                }
            });
            menu.add(showWindowItem);

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    eventHandler.exit();
                }
            });
            menu.add(exitItem);

            icon = new TrayIcon(image, "Directory mirroring tool", menu);
            tray.add(icon);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(icon);
    }
}
