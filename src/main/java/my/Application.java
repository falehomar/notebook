package my;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application extends javafx.application.Application {
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            EXECUTOR_SERVICE.shutdown();
            try {
                while (!EXECUTOR_SERVICE.awaitTermination(1, TimeUnit.SECONDS));
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        var fxmlLoader = new FXMLLoader(getClass().getResource("/my/Notebook.fxml"));
        var pane = new AnchorPane();
        fxmlLoader.setRoot(pane);
        fxmlLoader.load();

        var scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("mini:"+newValue);
        });
        primaryStage.onCloseRequestProperty().setValue(System.out::println);

        Dimension2D mainWindowSize = getMainWindowSize();
        primaryStage.setWidth(0.75*mainWindowSize.getWidth());
        primaryStage.setHeight(0.75*mainWindowSize.getHeight());

        primaryStage.show();



    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        launch(args);
        //Micronaut.run(Application.class, args);
    }

    static Dimension2D getMainWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        double width = screenSize.width / 2.0;
        double height = 3 * screenSize.height / 4.0;

        return new Dimension2D(width, height);
    }

}
