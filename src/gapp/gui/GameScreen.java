package gapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class GameScreen {
    public static void showGame() {
        BorderPane gamePane = new BorderPane();
        gamePane.setPadding(new Insets(12,12,12,12));

        GameBoard board = GameBoard.getSharedBoard();
        gamePane.setCenter(board);

        Button exit = new Button("Exit"); exit.setPrefWidth(70); exit.setOnAction(e -> Main.thestage.close());
        gamePane.setBottom(exit); BorderPane.setAlignment(exit, Pos.BOTTOM_RIGHT);

        Main.thestage.setScene(new Scene(gamePane, 800, 600));
    }
}
