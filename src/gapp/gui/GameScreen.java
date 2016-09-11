package gapp.gui;

import gapp.ulg.game.PlayerFactory;
import gapp.ulg.game.board.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class GameScreen {
    public static void showGame() {

        try { Thread.currentThread().join(1000); }
        catch (InterruptedException ignore) { }

        BorderPane gamePane = new BorderPane();
        gamePane.setPadding(new Insets(12,12,12,12));

        GameBoard board = GameBoard.getSharedBoard();
        //StackPane cntrWindow = new ... //Per inserire eventuali messaggi in overlay
        gamePane.setCenter(board); //Sostituire con lo StackPane più tardi

        int counter = 0;
        for(Player p : ((List<Player>) Main.playGUI.pL)) {
            VBox pVbox = new VBox(10); pVbox.setStyle("-fx-background-color: greenyellow");
            Label name = new Label(p.name()); name.setStyle("-fx-font: 15 arial");
            Label kind = new Label("Kind: " + ((PlayerFactory) Main.playGUI.pF.get(counter)).name());
            pVbox.getChildren().addAll(name, kind);
            if(counter < ((List<Player>) Main.playGUI.pL).size()/2) { //Giochi che hanno più di 2 player si dividono ordinatamente
                gamePane.setLeft(pVbox);
                BorderPane.setAlignment(pVbox, Pos.CENTER);
            }
            else {
                gamePane.setRight(pVbox);
                BorderPane.setAlignment(pVbox, Pos.CENTER);
            }
            counter++;
        }

        Button exit = new Button("Exit"); exit.setOnAction(e -> Main.thestage.close());
        Button next = new Button("Next"); next.setOnAction(e -> Main.playGUI.execTurn());
        next.setPrefSize(110, 60); next.setStyle("-fx-font-size: 25; -fx-font-weight: bold");
        StackPane buttons = new StackPane(); buttons.getChildren().addAll(next, exit);
        buttons.setPrefSize(800, 100); StackPane.setAlignment(exit, Pos.BOTTOM_RIGHT);
        gamePane.setBottom(buttons);

        Main.thestage.setScene(new Scene(gamePane, 800, 600));
    }
}
