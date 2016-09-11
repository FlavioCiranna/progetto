package gapp.gui;

import gapp.ulg.game.PlayerFactory;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GameScreen {
    private static GameRuler gR;

    public static void showGame() {

        try { Thread.currentThread().join(1000); }
        catch (InterruptedException ignore) { }

        BorderPane gamePane = new BorderPane();
        gamePane.setPadding(new Insets(12,12,12,12));

        GameBoard board = GameBoard.getSharedBoard();
        //StackPane cntrWindow = new ... //Per inserire eventuali messaggi in overlay
        gamePane.setCenter(board); //Sostituire con lo StackPane più tardi

        gR = GameBoard.gR;

        Button exit = new Button("Exit"); /*exit.setPrefWidth(70);*/ exit.setOnAction(e -> Main.thestage.close());
        Button next = new Button("Next"); next.setOnAction(e -> Main.playGUI.execTurn());
        HBox buttons = new HBox(10, next, exit);
        gamePane.setBottom(buttons); BorderPane.setAlignment(buttons, Pos.BOTTOM_RIGHT);

        Main.thestage.setScene(new Scene(gamePane, 800, 600));
    }
}
