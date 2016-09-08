package gapp.gui.Elements;

import gapp.gui.GameElement;
import gapp.ulg.game.board.Pos;

public class EmptyCell extends GameElement { //Cella Bianco con contorno nero
    public EmptyCell(Pos p) { super("file:Resources/emptyCell.png", p); }
}
