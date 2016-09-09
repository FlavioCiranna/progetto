package gapp.gui.Elements;

import gapp.gui.GameElement;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

public class GamePM extends GameElement {
    private PieceModel pm;

    public GamePM(PieceModel pm, Pos p) {
        super("file:Resources/"+pm.getSpecies()+"-"+pm.getColor()+".png", p);
        this.pm = pm;
    }

    public PieceModel getPm() { return pm; } //Verificare se mai usato
}
