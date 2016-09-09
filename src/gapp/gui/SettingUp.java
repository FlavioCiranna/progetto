package gapp.gui;

import gapp.ulg.games.GameFactories;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class SettingUp {

    public static void setUPmenu(){ //Problema nel cambio di gioco con i parametri, correzione futura
        BorderPane setMenu = new BorderPane(); //Layout principale
        Map<String,ComboBox> paramsMap = new HashMap<>(); //Aggiornamento dei combobox nel caso abbiano caratteristiche interlacciate

        //Pannello centrale
        Text gamesTitle = new Text("Select Game:");
        ListView<String> games = new ListView<>();
        games.getItems().addAll(GameFactories.availableBoardFactories());
        VBox centerPane = new VBox(10, gamesTitle, games);
        BorderPane.setMargin(centerPane, new Insets(0,12,0,0));

        //Pannello destro
        VBox opts = new VBox(10);
        games.setOnMouseClicked( event -> {
            opts.getChildren().clear(); //Svuota il VBox
            opts.getChildren().add(new Text("Opzioni:"));
            Main.playGUI.setGameFactory(games.getSelectionModel().getSelectedItem());

            String[] params = Main.playGUI.getGameFactoryParams();
            for(String p : params) {
                Label pName = new Label(p); pName.setMinWidth(40);
                Button info = new Button("INFO");
                ComboBox cb = new ComboBox(); cb.getItems().addAll(Main.playGUI.getGameFactoryParamValues(p));
                cb.setPrefWidth(80);

                info.setOnAction(e -> {
                    Alert inf = new Alert(Alert.AlertType.INFORMATION);
                    inf.setTitle("Info"); inf.setHeaderText(null); inf.setContentText(Main.playGUI.getGameFactoryParamPrompt(p));
                    inf.showAndWait();
                });

                cb.setOnAction(e -> {
                    Main.playGUI.setGameFactoryParamValue(p, cb.getValue());

                    for (Map.Entry<String,ComboBox> entry: paramsMap.entrySet()) {
                        String par = entry.getKey();
                        ComboBox cbox = entry.getValue();

                        if (cbox != e.getSource()) {
                            try{ cbox.getItems().setAll(Main.playGUI.getGameFactoryParamValues(par)); }
                            catch (Exception ignore) {}
                        }
                    }
                });

                paramsMap.put(p,cb);
                HBox pLine = new HBox(10, pName, info, cb);
                opts.getChildren().add(pLine);
            }

            setMenu.setRight(opts); BorderPane.setMargin(opts, new Insets(0,12,0,0));
        });

        //Pannello inferiore
        Button next = new Button("Next"); next.setPrefWidth(70);
        next.setOnAction( e -> {
            if(games.getSelectionModel().getSelectedIndex() < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning"); alert.setHeaderText(null); alert.setContentText("Please, select a game.");
                alert.showAndWait();
            }
            else { PlayerSetUP.pSetUPMenu(); }
        });
        Button exit = new Button("Exit"); exit.setPrefWidth(70); exit.setOnAction( e -> Main.thestage.close());
        HBox buttons = new HBox(5, next, exit);
        setMenu.setBottom(buttons); buttons.setMaxWidth(120);
        BorderPane.setAlignment(buttons, Pos.CENTER_RIGHT); buttons.setPadding(new Insets(0,12,0,0));

        setMenu.setCenter(centerPane); centerPane.setMaxWidth(150); BorderPane.setAlignment(centerPane, Pos.CENTER_LEFT);
        setMenu.setRight(opts);
        setMenu.setPadding(new Insets(12,0,12,12));
        Main.thestage.setScene(new Scene(setMenu,400,400));
    }

}
