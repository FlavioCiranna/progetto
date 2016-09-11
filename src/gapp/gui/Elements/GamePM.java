package gapp.gui.Elements;

import gapp.gui.GameElements;
import gapp.ulg.game.board.Action;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

public class GamePM extends GameElements {
    public enum Kind { DO, SHOW, UNSHOW }
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
            this.totalAnimTime = 33.3;
        }

        public void animate(Action a, Kind k) {
            this.a = a;
            this.kind = k;
            this.animTime = totalAnimTime;
        }

        @Override
        public void handle(long now) {
            if(a != null && kind != null && animTime >= 0) {
                double percentage = (99.9 / totalAnimTime) * (totalAnimTime - animTime);
                if(kind == Kind.DO) {
                    if(a.getKind() == Action.Kind.ADD) { //ADD + DO
                        pm.setOpacity(0.01 * percentage);
                    }
                    if(a.getKind() == Action.Kind.SWAP) { //SWAP + DO
                        pm.setScaleX((- 0.02 * percentage) + 1);
                        if(animTime <= totalAnimTime/2) {
                            PieceModel pieceModel = (PieceModel)a.piece;
                            pm.setImage(new Image("file:Resources/"+pieceModel.getSpecies()+"-"+pieceModel.getColor()+".png"));
                        }
                    }
                }

                if(kind == Kind.SHOW) {}

                animTime -= 1;
            }
        }


    }
}