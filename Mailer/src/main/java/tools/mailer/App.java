package tools.mailer;

import tools.mailer.di.container.DIContainer;

public class App {
    public static void main( String[] args ) {
        DIContainer diContainer = new DIContainer();
        diContainer.loadPlugin();
    }
}
