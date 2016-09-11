package gapp.gui.Elements;

import gapp.gui.GameElements;
import gapp.ulg.game.board.Pos;

public class EmptyCell extends GameElements { //Cella vuota con contorno nero
    private Pos pos;

    public EmptyCell(Pos p) {
        super("file:resources/emptyCell.png", p);
        this.pos = p;
    }
}
