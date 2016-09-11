package gapp.gui;

import gapp.ulg.game.board.GameRuler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class GameScreen {
    private static GameRuler gR;

    public static void showGame() {

        try { Thread.currentThread().join(1000); }
        catch (InterruptedException ignore) { }

        gR = GameBoard.gR;

        BorderPane gamePane = new BorderPane();
        gamePane.setPadding(new Insets(12,12,12,12));

        GameBoard board = GameBoard.getSharedBoard();
        //StackPane cntrWindow = new ... //Per inserire eventuali messaggi in overlay
        gamePane.setCenter(board); //Sostituire con lo StackPane più tardi

        Button exit = new Button("Exit"); exit.setOnAction(e -> Main.thestage.close());
        Button next = new Button("Next"); next.setOnAction(e -> Main.playGUI.execTurn());
        next.setPrefSize(110, 60); next.setStyle("-fx-font-size: 25; -fx-font-weight: bold");
        StackPane buttons = new StackPane(); buttons.getChildren().addAll(next, exit);
        buttons.setPrefSize(800, 100); StackPane.setAlignment(exit, Pos.BOTTOM_RIGHT);
        gamePane.setBottom(buttons);

        Main.thestage.setScene(new Scene(gamePane, 800, 600));
    }
}
