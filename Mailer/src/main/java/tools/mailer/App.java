package tools.mailer;

import tools.mailer.di.container.DIContainer;

public class App {
    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        DIContainer diContainer = new DIContainer();
        diContainer.loadPlugin();
    }
}
