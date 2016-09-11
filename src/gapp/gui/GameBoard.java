package gapp.gui;

import gapp.gui.Elements.EmptyCell;
import gapp.gui.Elements.GamePM;
import gapp.ulg.game.board.*;
import gapp.ulg.game.util.PlayGUI;
import gapp.ulg.game.util.PlayerGUI;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.function.Consumer;

import static gapp.ulg.game.board.Action.Kind.*;
import static gapp.ulg.game.board.Action.Kind.ADD;
import static gapp.ulg.game.board.Action.Kind.SWAP;


public class GameBoard extends GridPane implements PlayGUI.Observer{
    private static GameBoard sharedBoard = null; //Tavolo da gioco dell'istanza, uno solo per avvio di applicazione

    private static final int bCells = 30;
    public static Map<Pos, GameElements> gameMap; //Per poter effettivamente sostituire o eliminare i pezzi
    public static GameRuler gR;
    public static Pos showed;
    public static List<Pos> choosed = new ArrayList<>();
    public static boolean playerMoved = false;
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
        this.showed = null;

        gameMap.clear();
        choosed.clear();

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

                if (playerMoved && (action.kind == Action.Kind.SWAP ||
                                action.kind == Action.Kind.ADD ||
                                action.kind == Action.Kind.REMOVE)) {
                    for (Pos p: (List<Pos>)action.pos) {
                        ((GamePM) gameMap.get(p)).animatePiece(action, GamePM.Kind.END);
                    }
                    continue;
                }
                else if (playerMoved) { continue; }

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

    public static Consumer<PlayerGUI.MoveChooser> humanPlayer() {
        Consumer<PlayerGUI.MoveChooser> playerManager = (moveChooser) -> {

            GameBoard board = GameBoard.getSharedBoard();

            board.setOnMouseMoved(e -> {

                Pos p = new Pos((int)e.getX() / 30, (int)e.getY() / 30);

                if (!gR.getBoard().isPos(p) || p.equals(showed)) { return; }

                if (choosed.size() > 0 && showed == null)
                {
                    gameMap.get(p).select();
                    showed = p;
                }
                else if (choosed.size() > 0 && showed != null)
                {
                    gameMap.get(showed).unselect();
                    gameMap.get(p).select();
                    showed = p;
                }
                else if (showed == null)
                {
                    showMove(gameMap.get(p), moveChooser);
                    showed = p;
                }
                else if (showed != null)
                {
                    unshowMove(gameMap.get(showed));
                    showMove(gameMap.get(p), moveChooser);
                    showed = p;
                }
            });

            board.setOnMouseClicked(e -> {

                if (showed == null)
                {
                    return;
                }

                if (e.getButton() == MouseButton.PRIMARY)
                {
                    select(gameMap.get(showed), moveChooser);
                }
                else if (e.getButton() == MouseButton.SECONDARY)
                {
                    unselect(moveChooser);
                }
            });

            board.setOnMouseExited(e -> {

                if (choosed.size() > 0 && showed != null)
                {
                    gameMap.get(showed).unselect();
                    showed = null;
                }
                else if (choosed.size() == 0 && showed != null)
                {
                    unshowMove(gameMap.get(showed));
                    showed = null;
                }
            });
        };
        return playerManager;
    }

    public static void select(GameElements pos, PlayerGUI.MoveChooser moveChooser)
    {
        if (gR.result() != - 1)
        {
            return;
        }
        List<Move> moves = moveChooser.quasiSelected();

        if (moves == null || moves.size() == 0)
        {
            return;
        }

        for (Move move: moves)
        {
            Action action = (Action)move.actions.get(0);
            Move toDo = null;

            if (action.kind == Action.Kind.ADD || action.kind == SWAP || action.kind == Action.Kind.REMOVE)
            {
                toDo = moveChooser.doSelection(action.piece);
            }
            else if (action.kind == Action.Kind.JUMP && choosed.size() > 0)
            {
                toDo = moveChooser.jumpSelection(pos.getPos());

                if (toDo != null)
                {
                    ((GamePM) gameMap.get(action.pos.get(0))).animatePiece(action, GamePM.Kind.DO);
                    ((GamePM)gameMap.get(action.pos.get(0))).updatePos(action);
                }
            }
            else if (action.kind == JUMP && choosed.size() == 0)
            {
                choosed.add(pos.getPos());
                pos.choose();
                return;
            }
            else if (action.kind == Action.Kind.MOVE && choosed.size() > 0)
            {
                toDo = moveChooser.moveSelection(action.dir, action.steps);

                if (toDo != null)
                {
                    ((GamePM)gameMap.get(action.pos.get(0))).animatePiece(action, GamePM.Kind.DO);
                    ((GamePM)gameMap.get(action.pos.get(0))).updatePos(action);
                }
            }
            else if (action.kind == MOVE && choosed.size() == 0)
            {
                choosed.add(pos.getPos());
                pos.choose();
                return;
            }

            if (moveChooser.isFinal())
            {
                getSharedBoard().setOnMouseMoved(null);
                getSharedBoard().setOnMouseClicked(null);
                getSharedBoard().setOnMouseExited(null);
                playerMoved = true;
                moveChooser.move();
                synchronized (moveChooser)
                {
                    moveChooser.notify();
                }
                return;
            }
            else if (toDo != null)
            {
                for (Pos p: choosed)
                {
                    gameMap.get(p).unchoose();
                    gameMap.get(p).unselect();
                }
                choosed.clear();
                choosed.add(pos.getPos());
                pos.choose();

                showed = pos.getPos();
                showMove(pos, moveChooser);
                return;
            }
        }
    }

    public static void unselect(PlayerGUI.MoveChooser moveChooser)
    {
        if (gR.result() != - 1 || choosed.size() == 0)
        {
            return;
        }
        Move move = moveChooser.back();

        gameMap.get(choosed.get(choosed.size() - 1)).unchoose();
        choosed.remove(choosed.get(choosed.size() - 1));

        if (move == null)
        {
            return;
        }

        for (Action a: (List<Action>)move.actions)
        {
            if (a.kind == REMOVE || a.kind == SWAP || a.kind == ADD)
            {
                for (Pos p: (List<Pos>)a.pos)
                {
                    ((GamePM)gameMap.get(p)).animatePiece(a, GamePM.Kind.DO);
                }
            }
            else if (a.kind == MOVE)
            {
                for (Pos p: (List<Pos>)a.pos)
                {
                    ((GamePM) gameMap.get(p)).animatePiece(a, GamePM.Kind.DO);
                    ((GamePM) gameMap.get(p)).updatePos(a);
                }
            }
            else if (a.kind == JUMP)
            {
                ((GamePM) gameMap.get(((List<Pos>)a.pos).get(0))).animatePiece(a, GamePM.Kind.DO);
                ((GamePM) gameMap.get(((List<Pos>)a.pos).get(0))).updatePos(a);
            }
        }
    }

    public static void showMove(GameElements pos, PlayerGUI.MoveChooser moveChooser)
    {
        if (gR.result() != - 1)
        {
            return;
        }
        pos.select();

        List<Move> moves = moveChooser.select(pos.getPos());

        for (Move m: moves)
        {
            if (((List<Action>)m.actions).get(0).kind == JUMP || ((List<Action>)m.actions).get(0).kind == MOVE)
            {
                ((GamePM) gameMap.get(pos.getPos())).animatePiece(((List<Action>)m.actions).get(0), GamePM.Kind.SHOW);
                return;
            }

            for (Action a: (List<Action>)m.actions)
            {
                if (a.kind == Action.Kind.ADD)
                {
                    GamePM piece = new GamePM((PieceModel)a.piece, ((List<Pos>)a.pos).get(0));

                    gameMap.put(piece.getPos(), piece);

                    getSharedBoard().getChildren().add(piece);

                    piece.animatePiece(a, GamePM.Kind.SHOW);
                }
                else if (a.kind == SWAP || a.kind == Action.Kind.REMOVE)
                {
                    for (Pos p: (List<Pos>)a.pos)
                    {
                        ((GamePM) gameMap.get(p)).animatePiece(a, GamePM.Kind.SHOW);
                    }
                }
            }
        }
    }

    public static void unshowMove(GameElements pos)
    {
        if (gR.result() != - 1)
        {
            return;
        }
        pos.unselect();

        for (Move m: (Set<Move>)gR.validMoves(pos.getPos()))
        {
            if (((List<Action>)m.actions).get(0).kind == JUMP || ((List<Action>)m.actions).get(0).kind == MOVE)
            {
                ((GamePM) gameMap.get(pos.getPos())).animatePiece(((List<Action>)m.actions).get(0), GamePM.Kind.UNSHOW);
                return;
            }

            for (Action a: (List<Action>)m.actions)
            {
                if (a.kind == REMOVE || a.kind == SWAP || a.kind == ADD)
                {
                    for (Pos p: (List<Pos>)a.pos)
                    {
                        ((GamePM) gameMap.get(p)).animatePiece(a, GamePM.Kind.UNSHOW);
                    }
                }
            }
        }
    }
}
