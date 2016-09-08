package gapp.gui;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.util.PlayGUI;
import gapp.ulg.game.util.PlayerGUI;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;


public class GameBoard extends GridPane implements PlayGUI.Observer{
    private static GameBoard sharedBoard; //Tavolo da gioco dell'istanza, uno solo per avvio di applicazione

    private static final int bCells = 30;
    //private static Map<Pos, pezzoUI> gameMap;
    private static GameRuler gR;
    private static String vMsg;

    public GameBoard() { super(); }
    public static GameBoard getSharedBoard() {
        if(sharedBoard == null) { sharedBoard = new GameBoard(); }
        return sharedBoard;
    }

    @Override
    public void setGame(GameRuler g) { //Non riesco a far comparire il contenuto del GridPane
        if(g == null) { throw new NullPointerException("Il gioco non può essere null"); }

        this.gR = g;

        setMaxHeight(gR.getBoard().height() * bCells); setMaxWidth(gR.getBoard().height() * bCells);


    }

    @Override
    public void moved(int i, Move m) {
        if(m == null) { throw new NullPointerException("La mossa non può essere null"); }
        if(!gR.isPlaying(i) || !gR.isValid(m)) { throw new IllegalArgumentException("Il giocatore o la mossa non sono validi"); }

        gR.move(m);
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

    public static Consumer<PlayerGUI.MoveChooser> humanPlayer() {
        return null; //Temporaneo
    }
}
