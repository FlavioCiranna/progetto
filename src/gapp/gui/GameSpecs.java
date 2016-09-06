package gapp.gui;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.util.PlayGUI;
import javafx.scene.layout.Pane;


public class GameSpecs extends Pane implements PlayGUI.Observer{
    private GameRuler gR;
    private String vMsg;

    public GameSpecs() { super(); }

    @Override
    public void setGame(GameRuler g) {
        if(g == null) { throw new NullPointerException("Il gioco non può essere null"); }
        this.gR = g;
    }

    @Override
    public void moved(int i, Move m) {
        if(m == null) { throw new NullPointerException("La mossa non può essere null"); }
        if(!gR.isPlaying(i) || !gR.isValid(m)) { throw new IllegalArgumentException("Il giocatore o la mossa non sono validi"); }

        gR.move(m);

        //Riservato per implementazioni future sulla board
    }

    @Override
    public void limitBreak(int i, String msg) {
        if(msg == null) { throw new NullPointerException("Nessun messaggio di violazione inserito"); }
        if(!gR.isPlaying(i)) { throw new IllegalArgumentException("Giocatore non valido"); }

        vMsg = msg;
        gR.move(new Move(Move.Kind.RESIGN)); //Resa automatica
    }

    @Override
    public void interrupted(String msg) { //Indagare sull'utilizzo futuro
        vMsg = msg;
    }
}
