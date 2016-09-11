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

/*
Il seguente progetto è visibilmente incompleto, ho comunque deciso di procedere
con la consegna per completezza del percorso formativo del semestre passato. Il
seguente gioco sfortunatamente non è in grado di usare player umani ed è sprovvisto
dell'interfaccia per aggiungere nuovi giochi (benchè il sistema funzioni).
*/

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private static GameBoard board ;
    public static PlayGUI playGUI;
    public static Stage thestage;

    private Parent welcome() { //Messaggio di inizio, scompare dopo tot tempo
        Text message = new Text("TABLETOP GAMES");
        message.setStyle("-fx-font: 30 arial");
        VBox vb = new VBox(message); vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-background-color: forestgreen");
        return vb;
    }

    @Override
    public void start(Stage primaryStage) {
        thestage = primaryStage;
        board = GameBoard.getSharedBoard();
        playGUI = new PlayGUI(board, 10000);

        primaryStage.setTitle("Tabletop Games");
        primaryStage.setScene(new Scene(welcome(), 600, 400)); //Pagina di benvenuto

        PauseTransition delay = new PauseTransition(Duration.seconds(3)); //Passaggio a pagina impostazione game (AUMENTARE A 4!)
        delay.setOnFinished( event -> SettingUp.setUPmenu() );
        delay.play();

        primaryStage.show();
    }

}
