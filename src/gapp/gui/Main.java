package gapp.gui;

import gapp.ulg.game.util.Utils;
import gapp.ulg.games.GameFactories;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    private Parent welcome() { //Messaggio di inizio, scompare dopo tot tempo
        Text message = new Text("-MESSAGGIO DI BENVENUTO DA DECIDERE-");
        message.setStyle("-fx-font: 24 arial;");
        VBox vb = new VBox(message); vb.setAlignment(Pos.CENTER);
        return vb;
    }

    private BorderPane choice() {
        ListView<String> games = new ListView<>(); //Lista di giochi Disponibile
        ObservableList<String> list = FXCollections.observableArrayList(GameFactories.availableBoardFactories());
        games.setItems(list);

        ComboBox<String> time = new ComboBox<>(); //Lista di tempi limiti disponibile
        List<String> timeStr = Arrays.asList("No limit", "1s", "2s", "3s", "5s", "10s", "20s", "30s", "1m", "2m", "5m"); //Non troppo sicuro se compatibile con ogni gioco
        time.getItems().addAll(timeStr);
        time.setValue("No limit");

        Text t1 = new Text("Select Game:"), t2 = new Text("Select time limit:");
        VBox b1 = new VBox(t1, games); VBox b2 = new VBox(t2, time); b1.setSpacing(10); b2.setSpacing(10);
        HBox items = new HBox(b1, b2); items.setAlignment(Pos.CENTER); items.setSpacing(30);

        BorderPane bp = new BorderPane(items); BorderPane.setMargin(items, new Insets(12,12,12,12));
        return bp;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(welcome(), 800, 600)); //Pagina di benvenuto

        PauseTransition delay = new PauseTransition(Duration.seconds(1)); //Passaggio a pagina impostazione game (AUMENTARE A 4!)
        delay.setOnFinished( event -> primaryStage.setScene(new Scene(choice())) );
        delay.play();



        primaryStage.show();
    }

}
