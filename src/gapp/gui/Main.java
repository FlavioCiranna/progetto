package gapp.gui;

import gapp.ulg.game.GameFactory;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
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

import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private GameFactory<GameRuler<PieceModel<PieceModel.Species>>> gF;
    private long timeVal = (long) -1; //Valore di default
    private int sizeVal; //Dimensione Othello
    private int m,n,k; //Dimensione mnk-game

    private Button nextScene = new Button("Next");

    private Parent welcome() { //Messaggio di inizio, scompare dopo tot tempo
        Text message = new Text("-MESSAGGIO DI BENVENUTO DA DECIDERE-");
        message.setStyle("-fx-font: 24 arial;");
        VBox vb = new VBox(message); vb.setAlignment(Pos.CENTER);
        return vb;
    }

    private BorderPane choice() {
        Text t3 = new Text("Board Size:");
        VBox sizeBox = new VBox(t3);
        ListView<String> games = new ListView<>(); //Lista di giochi Disponibile
        ObservableList<String> list = FXCollections.observableArrayList(GameFactories.availableBoardFactories());
        games.setItems(list);

        ComboBox<String> time = new ComboBox<>(); //Lista di tempi limiti disponibile, dipende dal gioco selezionato
        games.setOnMouseClicked( event -> { //Ogni click sulla lista giochi cambia la lista tempi disponibile
            time.getItems().clear(); //Ripulisce la lista o si sommerebbe all'infinito
            gF = GameFactories.getBoardFactory(games.getSelectionModel().getSelectedItem());
            List<String> timeStr = (List<String>) GameFactories.getBoardFactory(games.getSelectionModel().getSelectedItem()).params().get(0).values();
            time.getItems().addAll(timeStr);
            time.setValue("No limit");

            if(gF.name().equals("Othello")) { //Nel caso si gioca ad Othello
                TextField s1 = new TextField("8");
                TextField s2 = new TextField("8"); s2.setDisable(true);
                s1.setMaxWidth(30); s2.setMaxWidth(30);
                s1.setOnAction(e -> s2.setText(s1.getText()));
                HBox hSize = new HBox(s1, new Text("x"), s2); hSize.setSpacing(5);
                sizeBox.getChildren().clear(); sizeBox.getChildren().addAll(t3, hSize);
            }

            if(gF.name().equals("m,n,k-game")) { //Nel caso si gioca ad mnk-game
                TextField m = new TextField("3"), n = new TextField("3"), k = new TextField("3");
                m.setMaxWidth(30); n.setMaxWidth(30); k.setMaxWidth(30);
                HBox hSize = new HBox(m, n, k); hSize.setSpacing(5);
                sizeBox.getChildren().clear(); sizeBox.getChildren().addAll(t3, hSize);
            }

        });

        time.setOnMouseClicked( event -> timeVal = Utils.mapTime().get(time.getSelectionModel().getSelectedItem())); //Imposto il tempo dalla selezione

        Text t1 = new Text("Select Game:"), t2 = new Text("Select time limit:");
        TextField tf =  new TextField("Select game"); tf.setDisable(true);
        VBox left = new VBox(t1, games), right = new VBox(t2, time, sizeBox);
        left.setSpacing(10); right.setSpacing(10);
        HBox items = new HBox(left, right); items.setAlignment(Pos.CENTER); items.setSpacing(30);

        BorderPane bp = new BorderPane(items);
        bp.setBottom(nextScene); BorderPane.setAlignment(nextScene, Pos.CENTER_RIGHT); BorderPane.setMargin(nextScene, new Insets(0,12,12,0));
        BorderPane.setMargin(items, new Insets(12,12,12,12));
        return bp;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tabletop Games");
        primaryStage.setScene(new Scene(welcome(), 800, 600)); //Pagina di benvenuto

        PauseTransition delay = new PauseTransition(Duration.seconds(1)); //Passaggio a pagina impostazione game (AUMENTARE A 4!)
        delay.setOnFinished( event -> primaryStage.setScene(new Scene(choice())) );
        delay.play();



        primaryStage.show();
    }

}
