package gapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class GameScreen {
    public static void showGame() {
        BorderPane gamePane = new BorderPane();
        gamePane.setPadding(new Insets(12,12,12,12));

        GameBoard board = GameBoard.getSharedBoard();
        gamePane.setCenter(board); BorderPane.setAlignment(board, Pos.CENTER);

        Main.thestage.setScene(new Scene(gamePane, 800, 600)); //Sostituire TextField con l'intera nuova schermata
    }
}
