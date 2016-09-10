package gapp.gui.Elements;

import gapp.ulg.game.board.Pos;
import javafx.scene.image.ImageView;

public class EmptyCell extends ImageView { //Cella Bianco con contorno nero
    private Pos pos;
    public EmptyCell(Pos p) {
        super("file:Resources/emptyCell.png");
        this.pos = p;
    }
}
