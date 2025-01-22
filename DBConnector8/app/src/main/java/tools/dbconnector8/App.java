/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package tools.dbconnector8;

import tools.dbconnector8.ui.MainFrame;

public class App {
	private MainFrame mainFrame;

	public static void main(String[] args) {
		new App();
    }
	
	public App() {
		AppHandle.getAppHandle().setApp(this);

		mainFrame = new MainFrame();
		mainFrame.setVisible(true);	
	}
    
    public void close() {
    	mainFrame.dispose();
    }
}
