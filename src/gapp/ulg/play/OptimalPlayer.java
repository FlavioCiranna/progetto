package gapp.ulg.play;

import gapp.ulg.game.board.*;

import java.util.Objects;

import static gapp.ulg.play.OptimalPlayerFactory.Strategy;
import static gapp.ulg.game.board.GameRuler.Situation;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto {@code OptimalPlayer} è un giocatore che gioca la strategia ottimale
 * per un gioco specifico (cioè un {@link GameRuler} che ha uno specifico nome).
 * <br>
 * Una <i>strategia</i> per un gioco <i>g</i> è una procedura capace di proporre una
 * mossa valida in una qualsiasi situazione che può presentarsi durante una qualsiasi
 * partita del gioco <i>g</i>. Una strategia è <i>ottimale</i> se facendo le mosse
 * che propone, l'esito della partita è il migliore possibile. Ad esempio per il gioco
 * (6,5,4)-game la strategia ottimale garantisce che il primo giocatore vince sempre.
 * Questo significa che se il primo giocatore segue la strategia ottimale qualunque
 * siano le mosse dell'avversario è garantito che vince. Mentre per il gioco
 * (3,3,3)-game, cioè Tris o Tic Tac Toe, la strategia ottimale garantisce solamente
 * la patta, cioè uno qualsiasi dei due giocatori che segue la strategia ottimale è
 * garantito che non perde (cioè o vince o patteggia). Se entrambi seguono la
 * strategia ottimale il gioco finisce con una patta sempre.
 * <br>
 * Per calcolare la strategia ottimale per un gioco si calcolano i valori delle
 * situazioni di gioco. Il <i>valore di una situazione</i> rappresenta l'esito
 * garantito della partita che parte da quella situazione e il giocatore di turno
 * segue fino alla fine della partita la strategia ottimale (e gli altri giocatori si
 * comportano in qualsiasi modo). Il valore della situazione iniziale è il <i>valore
 * del gioco</i>. I valori delle situazioni finali sono immediati quelli delle
 * situazioni non finali si possono calcolare ricorsivamente. Consideriamo un gioco
 * con due giocatori che indichiamo con <i>x</i> e <i>y</i>. Sia <i>S</i> una
 * situazione dove <i>x</i> è il giocatore di turno:
 * <pre>
 *               | "vince x"     se c'è una mossa da <i>S</i> che porta in
 *               |                   una situazione con valore "vince x"
 * valore di <i>S</i> = | "patta"       se non vale la condizione precedente e
 *               |                   c'è una mossa da <i>S</i> che porta in
 *               |                   una situazione con valore "patta"
 *               | "vince y"     altrimenti
 * </pre>
 * Simmetricamente se il giocatore di turno in <i>S</i> è <i>y</i>.
 * <br>
 * Se il gioco ha più di due giocatori, gli scenari sono, in generale, molto più
 * complessi e per questo ci limitiamo a considerare strategie ottimale per giochi
 * con due giocatori.
 * @param <P>  tipo del modello dei pezzi */
public class OptimalPlayer<P> implements Player<P> {
    public String name;
    private Strategy st;
    private GameRuler<P> gameRul = null;
    /** Crea un giocatore capace di giocare la strategia ottimale per uno specifico
     * gioco.
     * @param name  il nome del giocatore
     * @param st  la strategia ottimale per un gioco
     * @throws NullPointerException se {@code name} o {@code st} è null */
    public OptimalPlayer(String name, Strategy st) {
        if(name == null || st == null) { throw new NullPointerException("Il nome del gioco o la strategia non possono essere null"); }
        this.name = name;
        this.st = st;
    }

    @Override
    public String name() { return name; }

    /** Se {@code g} non è il gioco per cui conosce la strategia ottimale, lancia
     * {@link IllegalArgumentException}. */
    @Override
    public void setGame(GameRuler<P> g) { //Manca l'eccezione, da comprendere un attimo
        if(!Objects.equals(g.name(), st.gName())) { throw new IllegalArgumentException("Strategia non compatibile con il gioco corrente"); }
        gameRul = g;
    }

    @Override
    public void moved(int i, Move<P> m) { //Ancora non nota l'utilità
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public Move<P> getMove() {
        if(gameRul == null || gameRul.result() >= 0 ||
                gameRul.players().indexOf(name) + 1 != gameRul.turn()) {
            throw new IllegalStateException("Il gioco potrebbe non essere impostato, terminato o non è il turno del giocatore");
        }
        return null;
    }


    private static <P> Situation<P> toSituation(GameRuler<P> gR) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }
}
