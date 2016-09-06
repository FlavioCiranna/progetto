package gapp.gui;

import gapp.ulg.game.util.PlayGUI;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private static GameSpecs gSpecs = new GameSpecs();
    public static PlayGUI playGUI = new PlayGUI(gSpecs, 10000);
    public static Stage thestage;

    private Parent welcome() { //Messaggio di inizio, scompare dopo tot tempo
        Text message = new Text("-MESSAGGIO DI BENVENUTO DA DECIDERE-");
        message.setStyle("-fx-font: 24 arial;");
        VBox vb = new VBox(message); vb.setAlignment(Pos.CENTER);
        return vb;
    }

    @Override
    public void start(Stage primaryStage) {
        thestage = primaryStage;
        primaryStage.setTitle("Tabletop Games");
        primaryStage.setScene(new Scene(welcome(), 800, 600)); //Pagina di benvenuto

        PauseTransition delay = new PauseTransition(Duration.seconds(1)); //Passaggio a pagina impostazione game (AUMENTARE A 4!)
        delay.setOnFinished( event -> SettingUp.setUPmenu() );
        delay.play();

        primaryStage.show();
    }

}
