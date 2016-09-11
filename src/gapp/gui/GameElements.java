package gapp.gui;

import gapp.ulg.game.board.Pos;
import javafx.scene.image.ImageView;

public class GameElements extends ImageView {
    private Pos pos;
    public boolean selected;
    public boolean choosed;

    public GameElements(String url, Pos p) {
        super(url);
        this.pos = p;
    }

    public Pos getPos() { return pos; }
    public void select()
    {
        if (!selected)
        {
            setOpacity(getOpacity() * 0.5);
            selected = true;
        }
    }

    public void unselect()
    {
        if (selected)
        {
            setOpacity(getOpacity() * 2);
            selected = false;
        }
    }

    public void choose()
    {
        if (!choosed)
        {
            choosed = true;
        }
    }

    public void unchoose()
    {
        if (choosed)
        {
            choosed = false;
        }
    }
}
