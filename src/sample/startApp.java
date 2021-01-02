package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class startApp extends Application{

    private Stage window;
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        GameApp app = GameApp.getInstance();
        app.mainMenu(window);
    }
}
