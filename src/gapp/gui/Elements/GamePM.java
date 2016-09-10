package gapp.gui.Elements;

import gapp.ulg.game.board.Action;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

public class GamePM extends ImageView {
    public enum Kind { SHOW, DO, UNSHOW }
    private PieceModel pm;
    private Animator animator;
    private Pos pos;

    public GamePM(PieceModel pm, Pos p) {
        super("file:Resources/"+pm.getSpecies()+"-"+pm.getColor()+".png");
        this.pm = pm;
        this.pos = p;
        animator = new Animator(this);
        animator.start();
    }

    public PieceModel getPm() { return pm; } //Verificare se mai usato
    public Pos getPos() { return pos; }
    public void animatePiece(Action.Kind ak, Kind k) { animator.animate(ak, k); }

    private class Animator extends AnimationTimer {
        private GamePM pm;
        private Action.Kind aKind;
        private Kind kind;
        private final double totalAnimTime;
        private double animTime;

        public Animator(GamePM pm) {
            this.pm = pm;
            this.totalAnimTime = 33.3;
        }

        public void animate(Action.Kind ak, Kind k) {
            this.aKind = ak;
            this.kind = k;
            this.animTime = totalAnimTime;
        }

        @Override
        public void handle(long now) {
            if(aKind != null && kind != null && animTime >= 0) {
                double percentage = (100 / totalAnimTime) * (totalAnimTime - animTime);
                if(kind == Kind.DO) {
                    if(aKind == Action.Kind.ADD) { //ADD + DO
                        pm.setOpacity(0.01 * percentage);
                    }
                    if(aKind == Action.Kind.SWAP) { //SWAP + DO

                    }
                }

                if(kind == Kind.SHOW) {}

                animTime -= 1;
            }
        }


    }
}
