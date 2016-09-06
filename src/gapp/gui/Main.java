package gapp.gui;

import gapp.ulg.game.GameFactory;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.util.PlayGUI;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.GameFactories;
import gapp.ulg.play.PlayerFactories;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

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
/*
    private BorderPane choice() { //Sostituire dimensioni con ComboBox? Mostrare Messaggio di errore?
        Text t3 = new Text("Board Size:");
        TextField temp = new TextField("Please Select Game"); temp.setMaxWidth(Double.MAX_VALUE); temp.setDisable(true);
        VBox sizeBox = new VBox(t3, temp);
        ListView<String> games = new ListView<>(); //Lista di giochi Disponibile
        ObservableList<String> list = FXCollections.observableArrayList(GameFactories.availableBoardFactories());
        games.setItems(list);

        ComboBox<String> time = new ComboBox<>(); //Lista di tempi limiti disponibile, dipende dal gioco selezionato
        time.setValue("Please Select Game"); time.setMaxWidth(Double.MAX_VALUE);
        games.setOnMouseClicked( event -> { //Ogni click sulla lista giochi cambia la lista tempi disponibile
            time.getItems().clear(); //Ripulisce la lista o si sommerebbe all'infinito
            gF = GameFactories.getBoardFactory(games.getSelectionModel().getSelectedItem());
            List<String> timeStr = (List<String>) GameFactories.getBoardFactory(games.getSelectionModel().getSelectedItem()).params().get(0).values();
            time.getItems().addAll(timeStr);
            time.setValue("No limit");

            if(gF.name().equals("Othello")) { //Nel caso si gioca ad Othello
                sizeVal = 8; //Imposta valore di default
                TextField s1 = new TextField("8");
                TextField s2 = new TextField("8"); s2.setDisable(true);
                s1.setMaxWidth(30); s2.setMaxWidth(30);
                s1.setOnAction(e -> s2.setText(s1.getText()));
                HBox hSize = new HBox(5, s1, new Text("x"), s2);
                sizeBox.getChildren().clear(); sizeBox.getChildren().addAll(t3, hSize);
                nextScene.setOnAction(e -> {
                    sizeVal = Integer.valueOf(s1.getText());
                    thestage.setScene(new Scene(playerSelect()));
                });
            }

            if(gF.name().equals("m,n,k-game")) { //Nel caso si gioca ad mnk-game
                m = n = k = 3; //Imposta i valori di default
                TextField m = new TextField("3"), n = new TextField("3"), k = new TextField("3");
                m.setMaxWidth(30); n.setMaxWidth(30); k.setMaxWidth(30);
                HBox hSize = new HBox(5, m, n, k);
                sizeBox.getChildren().clear(); sizeBox.getChildren().addAll(t3, hSize);
                nextScene.setOnAction(e -> {
                    this.m = Integer.valueOf(m.getText()); this.n = Integer.valueOf(n.getText()); this.k = Integer.valueOf(k.getText());
                    thestage.setScene(new Scene(playerSelect()));
                });
            }

        });

        time.setOnMouseClicked( event -> timeVal = Utils.mapTime().get(time.getSelectionModel().getSelectedItem())); //Imposto il tempo dalla selezione

        Text t1 = new Text("Select Game:"), t2 = new Text("Select time limit:");
        VBox left = new VBox(10, t1, games), right = new VBox(10, t2, time, sizeBox);
        HBox items = new HBox(left, right); items.setAlignment(Pos.CENTER); items.setSpacing(30);

        BorderPane bp = new BorderPane(items);
        bp.setBottom(nextScene); BorderPane.setAlignment(nextScene, Pos.CENTER_RIGHT); BorderPane.setMargin(nextScene, new Insets(0,12,12,0));
        BorderPane.setMargin(items, new Insets(12,12,12,12));
        return bp;
    }

    private Parent playerSelect() {
        HBox pNames = new HBox(10);
        for(int n = 0; n < gF.maxPlayers(); n++) { //Crea tanti campi quanti il numero di player
            ComboBox<String> pKind = new ComboBox<>(); pKind.setMaxWidth(Double.MAX_VALUE);
            pKind.getItems().addAll(PlayerFactories.availableBoardFactories()); pKind.setValue(PlayerFactories.availableBoardFactories()[1]);
            pKind.getItems().add("Player");
            VBox t = new VBox(10, new Text("Player "+(n+1)+" Name:"), new TextField(), pKind);
            pNames.getChildren().addAll(t);
        }

        BorderPane bp = new BorderPane(pNames); BorderPane.setMargin(pNames, new Insets(12,12,12,12));

        return bp;
    }
*/
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
