package gapp.gui;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

public class GameScreen {
    public static void showGame() {


        Main.thestage.setScene(new Scene(new TextField("Building"), 800, 600)); //Sostituire TextField con l'intera nuova schermata
    }
}
