package gapp.gui;

import gapp.ulg.games.GameFactories;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SettingUp {

    public static void setUPmenu(){
        BorderPane setMenu = new BorderPane(); //Layout principale

        //Pannello centrale
        Text gamesTitle = new Text("Select Game:");
        ListView<String> games = new ListView<>();
        games.getItems().addAll(GameFactories.availableBoardFactories());
        VBox centerPane = new VBox(5, gamesTitle, games);

        //Pannello destro
        games.setOnMouseClicked( event -> {
            Main.playGUI.setGameFactory(games.getSelectionModel().getSelectedItem());

            VBox opts = new VBox(5);
            String[] params = Main.playGUI.getGameFactoryParams();
            for(String p : params) {
                Text pName = new Text(p);
                Button info = new Button("INFO");
                ComboBox cb = new ComboBox();cb.getItems().addAll(Main.playGUI.getGameFactoryParamValues(p));

                HBox pLine = new HBox(10, pName, info, cb);
                opts.getChildren().add(pLine);
            }
            setMenu.setRight(opts);
        });

        setMenu.setCenter(centerPane);
        setMenu.setPadding(new Insets(12,12,12,12));
        Main.thestage.setScene(new Scene(setMenu));
    }

}
