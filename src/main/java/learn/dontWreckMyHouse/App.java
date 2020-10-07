package learn.dontWreckMyHouse;

import learn.dontWreckMyHouse.ui.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class App {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        Controller controller = context.getBean(Controller.class);

        controller.run();
    }
}
