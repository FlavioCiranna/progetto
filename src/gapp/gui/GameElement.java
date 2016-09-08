package gapp.gui;

import gapp.ulg.game.board.Pos;
import javafx.scene.image.ImageView;

public class GameElement extends ImageView {

    private int x;
    private int y;
    private Pos pos;

    public GameElement(String elem, Pos p) {
        super(elem);

        this.x = p.getB();
        this.y = p.getT();
    }

    public Pos getPos() { return pos; }
}
