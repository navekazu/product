package tools.pwm;

import tools.pwm.ui.Login;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    public App() {
        Login login = new Login();
        login.pack();
        login.setVisible(true);
        System.out.println("isLoginSuccess:"+login.isLoginSuccess());
    }

    public static void main(String[] args) {
        new App();
    }
}