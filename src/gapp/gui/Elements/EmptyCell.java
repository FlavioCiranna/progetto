package gapp.gui.Elements;

import gapp.gui.GameElements;
import gapp.ulg.game.board.Pos;
import javafx.scene.image.ImageView;

public class EmptyCell extends GameElements { //Cella Bianco con contorno nero
    private Pos pos;

    public EmptyCell(Pos p) {
        super("file:Resources/emptyCell.png", p);
        this.pos = p;
    }
}
