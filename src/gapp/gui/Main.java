package gapp.gui;

import gapp.ulg.game.util.Utils;
import gapp.ulg.games.GameFactories;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
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

    private Parent choice() {
        ListView<String> games = new ListView<>(); //Lista di giochi Disponibile
        ObservableList<String> list = FXCollections.observableArrayList(GameFactories.availableBoardFactories());
        games.setItems(list);

        ComboBox<Long> time = new ComboBox<>(); //Lista di tempi limiti disponibile
        List<Long> times = new ArrayList<>(Utils.mapTime().values()); Collections.sort(times);
        time.getItems().addAll(times);
        time.setValue((long) -1);

        HBox vb = new HBox(games, time); vb.setAlignment(Pos.CENTER_LEFT);
        return vb;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(welcome(), 800, 600)); //Pagina di benvenuto

        PauseTransition delay = new PauseTransition(Duration.seconds(4)); //Passaggio a pagina impostazione game
        delay.setOnFinished( event -> primaryStage.setScene(new Scene(choice(), 800, 600)) );
        delay.play();



        primaryStage.show();
    }

}
