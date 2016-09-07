package gapp.gui;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

/**
 * Created by maxmo on 07/09/2016.
 */
public class GameScreen {
    public static void showGame() {
        Main.thestage.setScene(new Scene(new TextField("Building")));
    }
}
