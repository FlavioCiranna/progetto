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

        List<VBox> playersInfo = new ArrayList<>(); //Non Funziona, non si aggiorna il punteggio
        List<Player> pL = new ArrayList<>();
        for(Player p : (List<Player>) Main.playGUI.getpL()) {
            VBox pVBox = new VBox(10);
            Label name = new Label(p.name());
            Label kind = new Label("Kind: " + ((PlayerFactory) Main.playGUI.getpF().get(Main.playGUI.getpL().indexOf(p))).name());
            Label score = new Label();
            try {
                score.setText("Score: " + gR.score(((List<Player>) Main.playGUI.getpL()).indexOf(p)+1));
            } catch (UnsupportedOperationException exc) { score.setText("Score: ---"); }
            pVBox.getChildren().addAll(name, kind, score);
            pVBox.setStyle("-fx-background-color: greenyellow");
            pVBox.setPadding(new Insets(12,12,12,12));
            playersInfo.add(pVBox);
        }

        VBox vBoxL = new VBox(10); VBox vBoxR = new VBox(10);
        vBoxL.setAlignment(Pos.CENTER); vBoxR.setAlignment(Pos.CENTER);
        gamePane.setLeft(vBoxL); gamePane.setRight(vBoxR);
        for(int i = 0; i < playersInfo.size(); i++) {
            if(i < playersInfo.size()/2) { vBoxL.getChildren().add(playersInfo.get(i)); }
            else { vBoxR.getChildren().add(playersInfo.get(i)); }
        }

        Main.thestage.setScene(new Scene(gamePane, 800, 600));
    }
}
