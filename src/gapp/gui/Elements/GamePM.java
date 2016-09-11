package gapp.gui.Elements;

import gapp.gui.GameBoard;
import gapp.gui.GameElements;
import gapp.ulg.game.board.Action;
import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

import java.util.List;

public class GamePM extends GameElements {
    public enum Kind { DO, END, SHOW, UNSHOW }
    private PieceModel pm;
    private Animator animator;
    private Pos pos;

    public GamePM(PieceModel pm, Pos p) {
        super("file:Resources/"+pm.getSpecies()+"-"+pm.getColor()+".png", p);
        this.pm = pm;
        this.pos = p;
        animator = new Animator(this);
        animator.start();
    }

    public void updatePos(Action action)
    {
        Pos prevPos = getPos();

        if (action.kind == Action.Kind.JUMP)
        {
            pos = ((List<Pos>)action.pos).get(1);
        }
        else if (action.kind == Action.Kind.MOVE)
        {
            if (action.dir == Board.Dir.RIGHT ||
                    action.dir == Board.Dir.UP_R ||
                    action.dir == Board.Dir.DOWN_R)
            {
                pos = new Pos(pos.getB()+action.steps, pos.getT());
            }
            else if (action.dir == Board.Dir.LEFT ||
                    action.dir == Board.Dir.UP_L ||
                    action.dir == Board.Dir.DOWN_L)
            {
                pos = new Pos(pos.getB()-action.steps, pos.getT());
            }

            if (action.dir == Board.Dir.UP ||
                    action.dir == Board.Dir.UP_R ||
                    action.dir == Board.Dir.UP_L)
            {
                pos = new Pos(pos.getB(), pos.getT()+action.steps);
            }
            else if (action.dir == Board.Dir.DOWN ||
                    action.dir == Board.Dir.DOWN_L ||
                    action.dir == Board.Dir.DOWN_R)
            {
                pos = new Pos(pos.getB(), pos.getT()-action.steps);
            }
        }

        GameBoard.gameMap.put(getPos(), GameBoard.gameMap.get(getPos()));
        GameBoard.gameMap.put(prevPos, new EmptyCell(prevPos));
    }

    public PieceModel getPm() { return pm; } //Verificare se mai usato
    public void animatePiece(Action a, Kind k) { animator.animate(a, k); }

    private class Animator extends AnimationTimer {
        private GamePM pm;
        private Action a;
        private Kind kind;
        private final double totalAnimTime;
        private double animTime;

        public Animator(GamePM pm) {
            this.pm = pm;
            this.totalAnimTime = 20;
        }

        public void animate(Action a, Kind k) {
            this.a = a;
            this.kind = k;
            this.animTime = totalAnimTime;
        }

        @Override
        public void handle(long now) {
            if (a != null && kind != null && animTime >= 0) {
                double percentage = (100 / totalAnimTime) * (totalAnimTime - animTime);
                if (kind == Kind.DO) {
                    if (a.getKind() == Action.Kind.ADD) { //ADD + DO
                        pm.setOpacity(0.01 * percentage);
                    }
                    if (a.getKind() == Action.Kind.SWAP) { //SWAP + DO
                        double operation;
                        if (animTime <= totalAnimTime / 2) {
                            PieceModel pieceModel = (PieceModel) a.piece;
                            pm.setImage(new Image("file:Resources/" + pieceModel.getSpecies() + "-" + pieceModel.getColor() + ".png"));
                            operation = -0.01 * (1 - percentage); //Evito che i pezzi si rigirino su se stessi
                            } else { operation = (-0.01 * percentage) + 1; }
                            pm.setScaleY(operation);
                    }
                    if (a.kind == Action.Kind.REMOVE) {
                        pm.setScaleX(1 - (0.01 * percentage));
                        pm.setScaleY(1 - (0.01 * percentage));
                        pm.setOpacity(1 - (0.005 * percentage));
                    }
                }
                else if (kind == Kind.END) {
                    if (a.kind == Action.Kind.ADD) {
                    pm.setScaleX(0.005 * percentage + 0.5);
                    pm.setScaleY(0.005 * percentage + 0.5);
                    }
                    else if (a.kind == Action.Kind.REMOVE) {
                        pm.setScaleX(0.5 - (0.005 * percentage));
                        pm.setScaleY(0.5 - (0.005 * percentage));
                    }
                    else if (a.kind == Action.Kind.SWAP) {
                        pm.setScaleX(1 - (0.005 * percentage));
                        pm.setScaleX((- 0.015 * percentage) + 0.5);
                        if (pm.getScaleX() <= - 0.025 && pm.getScaleX() >= - 0.03) {
                            PieceModel pieceModel = (PieceModel)a.piece;
                            pm.setImage(new Image("file:Reources/"+pieceModel.species + "-" + pieceModel.color+".png"));
                        }
                    }
                }
                else if (kind == Kind.SHOW) {
                    if (a.kind == Action.Kind.ADD) {
                        pm.setScaleX(0.005 * percentage);
                        pm.setScaleY(0.005 * percentage);
                        pm.setOpacity(0.01 * percentage);
                    }
                    else if (a.kind == Action.Kind.SWAP) { pm.setScaleX(1 - (0.005 * percentage)); }
                    else if (a.kind == Action.Kind.REMOVE) {
                        pm.setScaleX(1 - (0.005 * percentage));
                        pm.setScaleY(1 - (0.005 * percentage));
                        pm.setOpacity(1 - (0.005 * percentage));
                    }
                    else if (a.kind == Action.Kind.MOVE || a.kind == Action.Kind.JUMP) {
                        pm.setScaleX(1 + (0.0025 * percentage));
                        pm.setScaleY(1 + (0.0025 * percentage));
                    }
                }
                else
                {
                    if (a.kind == Action.Kind.ADD)
                    {
                        pm.setScaleX(0.5 - (0.005 * percentage));
                        pm.setScaleY(0.5 - (0.005 * percentage));
                        pm.setOpacity(1 - (0.01 * percentage));
                    }
                    else if (a.kind == Action.Kind.SWAP)
                    {
                        pm.setScaleX(0.5 + (0.005 * percentage));
                    }
                    else if (a.kind == Action.Kind.REMOVE)
                    {
                        pm.setScaleX(0.5 + (0.005 * percentage));
                        pm.setScaleY(0.5 + (0.005 * percentage));
                        pm.setOpacity(0.5 + (0.005 * percentage));
                    }
                    else if (a.kind == Action.Kind.MOVE || a.kind == Action.Kind.JUMP)
                    {
                        pm.setScaleX(1.25 - (0.0025 * percentage));
                        pm.setScaleY(1.25 - (0.0025 * percentage));
                    }
                }
                animTime -= 1;
            }
        }
    }
}