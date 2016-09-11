package gapp.gui;

import gapp.gui.Elements.EmptyCell;
import gapp.gui.Elements.GamePM;
import gapp.ulg.game.board.*;
import gapp.ulg.game.util.PlayGUI;
import gapp.ulg.game.util.PlayerGUI;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.function.Consumer;

public class GameBoard extends GridPane implements PlayGUI.Observer{
    private static GameBoard sharedBoard = null; //Tavolo da gioco dell'istanza, uno solo per avvio di applicazione

    private static final int bCells = 30;
    public static Map<Pos, GameElements> gameMap; //Per poter effettivamente sostituire o eliminare i pezzi
    public static GameRuler gR;
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
        this.gameMap = new HashMap<>();

        gameMap.clear();

        setMaxHeight(gR.getBoard().height() * bCells); setMaxWidth(gR.getBoard().height() * bCells);
        setStyle("-fx-background-color: #CCCCCC");

        if(gR.getBoard().system() == Board.System.OCTAGONAL) {
            for(Pos p : (List<Pos>)gR.mechanics().positions) {
                EmptyCell ec = new EmptyCell(p);
                setConstraints(ec, p.getB(), p.getT());
                getChildren().add(ec);
                gameMap.put(p, ec); //Aggiorna la mappa dei pezzi
            }
        }

        if(!gR.mechanics().start.newMap().isEmpty()) { //Se esistono delle posizioni di start le aggiunge
            for(Pos p : (Set<Pos>)gR.getBoard().get()) {
                GamePM pm = new GamePM((PieceModel) gR.getBoard().get(p), p);
                setConstraints(pm, p.getB(), p.getT());
                getChildren().add(pm);
                gameMap.put(p, pm);
            }
        }

    }

    @Override
    public void moved(int i, Move m) { //Completabile solo dopo aver aggiunto delle animazioni plausibili
        if(m == null) { throw new NullPointerException("La mossa non può essere null"); }
        if(!gR.isPlaying(i) || !gR.isValid(m)) { throw new IllegalArgumentException("Il giocatore o la mossa non sono validi"); }

        gR.move(m);

        if(m.getKind() == Move.Kind.ACTION) { //Se si tratta di una mossa che modifica la board
            for(Action action : (List<Action>)m.getActions()) { //Per ogni azione
                if(action.getKind() == Action.Kind.ADD) { //Se si tratta semplicemente di aggiungere pedine
                    GamePM pm = new GamePM((PieceModel) action.getPiece(), (Pos) action.getPos().get(0));
                    Pos p = (Pos) action.getPos().get(0);
                    setConstraints(pm, p.getB(), p.getT());
                    getChildren().add(pm);
                    gameMap.put(p, pm);
                    pm.animatePiece(action, GamePM.Kind.DO);
                }

                if(action.getKind() == Action.Kind.SWAP) {
                    for(Pos p : (List<Pos>) action.getPos()) {
                        ((GamePM) gameMap.get(p)).animatePiece(action, GamePM.Kind.DO);
                    }
                }

            }
        }
    }

    @Override
    public void limitBreak(int i, String msg) {
        if(msg == null) { throw new NullPointerException("Nessun messaggio di violazione inserito"); }
        if(!gR.isPlaying(i)) { throw new IllegalArgumentException("Giocatore non valido"); }

        vMsg = msg;
        gR.move(new Move(Move.Kind.RESIGN)); //Resa automatica
    }

    @Override
    public void interrupted(String msg) { //Inserire finestra di messaggio
        vMsg = msg;
    }

    public static Consumer<PlayerGUI.MoveChooser> humanPlayer() { return null; } //Non funzionante
}
