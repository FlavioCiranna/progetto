package gapp.ulg.game.util;

import gapp.ulg.game.board.*;

import java.util.*;
import java.util.function.Consumer;

/** Un {@code PlayerGUI} rappresenta un giocatore che sceglie le mosse tramite GUI.
 * Però un oggetto {@code PlayerGUI} né controlla né interagisce direttamente con
 * alcuna GUI. Il suo scopo è facilitare la programmazione della parte di una GUI
 * che si occupa di permettere ad un utente di scegliere mosse valide durante una
 * partita ad un qualsiasi gioco del framework. L'interazione indiretta con la GUI
 * è mediata da {@link PlayerGUI.MoveChooser}. Ogni volta che è richiesta una mossa
 * al giocatore tramite il metodo {@link PlayerGUI#getMove()}, un oggetto
 * {@link PlayerGUI.MoveChooser} è comunicato alla GUI (vedi parametro {@code master}
 * di {@link PlayerGUI#PlayerGUI(String, Consumer)}). Tale oggetto presenta le mosse
 * valide della situazione di gioco attuale sotto forma di albero navigabile che
 * facilita la scelta di una delle mosse tramite la GUI. Infatti la navigazione è
 * manovrabile tramite operazioni che sono direttamente collegabili a eventi di input,
 * come il click del mouse in una certa posizione o la pressione e il movimento del
 * mouse.
 * <br>
 * Uno dei vantaggi principali che la programmazione di una GUI ottiene dall'adozione
 * dell'approccio di {@code PlayerGUI}, e del sottostante meccanismo basato su
 * {@link PlayerGUI.MoveChooser}, è che la GUI non necessita di nessuna ulteriore
 * modifica o aggiustamento per giocare a nuovi giochi che possono venir aggiunti
 * in qualsiasi momento al framework. E i nuovi giochi non devono preoccuparsi di
 * gestire una GUI per giocare.
 * <br>
 * Un {@code PlayerGUI} non deve essere usato nel thread o i thread che gestiscono
 * la GUI. Primo perché non c'è necessità di farlo e secondo perché il metodo
 * {@link PlayerGUI#getMove()} blocca fino a che la mossa non è stata scelta.
 * @param <P>  tipo del modello dei pezzi */
public class PlayerGUI<P> implements Player<P> {
    /** Un oggetto {@code MoveChooser} ha lo scopo di facilitare la scelta tramite
     * GUI di una mossa valida in una certa situazione di gioco. A questo fine
     * rappresenta l'insieme delle mosse valide di tipo {@link Move.Kind#ACTION} di
     * una situazione di gioco con una struttura ad albero navigabile chiamata
     * <i>albero delle mosse</i>.
     * <br>
     * Un oggetto {@code MoveChooser}, mantiene un nodo corrente che all'inizio è la
     * radice dell'albero. Il nodo corrente può essere spostato a uno dei suoi nodi
     * figli con i metodi {@link MoveChooser#doSelection(Object)},
     * {@link MoveChooser#moveSelection(Board.Dir, int)} e
     * {@link MoveChooser#jumpSelection(Pos)} o all'indietro al nodo genitore con
     * {@link MoveChooser#back()}. Per facilitare la selezione del nodo figlio,
     * un {@code MoveChooser} mantiene anche un insieme di posizioni, chiamato la
     * <i>selezione corrente</i>, che permette di selezionare un sotto-insieme dei
     * nodi figli del nodo corrente, tramite il metodo
     * {@link MoveChooser#select(Pos...)}.
     * <br>
     * Ogniqualvolta il nodo corrente è finale, cioè il metodo
     * {@link MoveChooser#isFinal()} ritorna true, si può scegliere la mossa
     * corrispondente con {@link MoveChooser#move()}. In ogni momento si può scegliere
     * la resa con {@link MoveChooser#resign()} e, se è una mossa valida (lo si può
     * accertare con {@link MoveChooser#mayPass()}), si può passare il turno con
     * {@link MoveChooser#pass()}. Per maggiori informazioni si può consultare
     * <a href="http://twiki.di.uniroma1.it/pub/Metod_prog/RS2_ESAMI/progetto.html">Progetto</a>.
     * @param <P>  tipo del modello dei pezzi */
    public interface MoveChooser<P> {
        /** @return un {@link Optional} con la sotto-mossa del nodo corrente o un
         * {@link Optional} vuoto se il nodo corrente è la radice con prefisso vuoto
         * o null se l'albero è vuoto
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        Optional<Move<P>> subMove();

        /** Ritorna la lista con le sotto-mosse di tutti i nodi figli del nodo
         * corrente. Se il nodo corrente è una foglia, ritorna la lista vuota. La
         * lista ritornata è sempre creata ex novo. Se l'albero è vuoto, ritorna null.
         * @return la lista con le sotto-mosse di tutti i nodi figli del nodo
         * corrente o null
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        List<Move<P>> childrenSubMoves();

        /** Seleziona le posizioni specificate e ritorna le sotto-mosse dei nodi figli
         * del nodo corrente selezionati dalle posizioni. Se non ci sono nodi figli
         * selezionati dalle posizioni, ritorna la lista vuota. La selezione corrente
         * diventa sempre uguale alle posizioni specificate anche se non seleziona
         * alcun nodo. La lista ritornata è sempre creata ex novo. Se l'albero è
         * vuoto, ritorna null.
         * @param pp  un insieme di posizioni
         * @return le sotto-mosse dei nodi figli del nodo corrente selezionati dalle
         * posizioni o null
         * @throws NullPointerException se una delle posizioni date è null
         * @throws IllegalArgumentException se non c'è almeno una posizione o ci
         * sono posizioni duplicate o c'è qualche posizione che non è nella board
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        List<Move<P>> select(Pos... pp);

        /** Ritorna le sotto-mosse dei nodi figli del nodo corrente quasi-selezionati
         * dalla selezione corrente. Se non ci sono sotto-mosse quasi-selezionate,
         * ritorna la lista vuota. La lista ritornata è sempre creata ex novo. Se
         * l'albero è vuoto ritorna null.
         * @return le sotto-mosse dei nodi figli del nodo corrente quasi-selezionati
         * dalla selezione corrente o null
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        List<Move<P>> quasiSelected();

        /** Se l'insieme dei nodi figli del nodo corrente selezionati dalla selezione
         * corrente non è vuoto e le prime azioni delle loro sotto-mosse o sono tutte
         * {@link Action.Kind#ADD} o sono tutte {@link Action.Kind#SWAP}, allora
         * ritorna la lista dei pezzi di tali azioni. Se invece c'è un solo nodo
         * figlio selezionato e la prima azione della sua sotto-mossa è
         * {@link Action.Kind#REMOVE}, ritorna una lista con l'unico elemento null.
         * Altrimenti ritorna la lista vuota. La lista ritornata è sempre creata ex
         * novo. Se l'albero è vuoto, ritorna null.
         * @return la lista dei pezzi o che contiene solamente l'elemento null o la
         * lista vuota o null
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        List<P> selectionPieces();

        /** Annulla la selezione corrente, se presente.
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        void clearSelection();

        /** Se le prime azioni delle sotto-mosse dei nodi figli selezionati dalla
         * selezione corrente sono tutte {@link Action.Kind#ADD} o sono tutte
         * {@link Action.Kind#SWAP} e una di queste ha il pezzo {@code pm}, allora
         * sposta il nodo corrente al corrispondente nodo figlio e ritorna la relativa
         * sotto-mossa. Se invece c'è un solo nodo figlio selezionato, la prima azione
         * della sua sotto-mossa è {@link Action.Kind#REMOVE} e {@code pm} è null,
         * allora sposta il nodo corrente al corrispondente nodo figlio e ritorna la
         * relativa sotto-mossa. In entrambi i casi la selezione corrente è annullata.
         * Se nessuno dei due casi è soddisfatto, non fa nulla e ritorna null.
         * @param pm  un pezzo o null
         * @return la sotto-mossa del nuovo nodo corrente o null
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        Move<P> doSelection(P pm);

        /** Se la prima azione della sotto-mossa di uno dei nodi figli selezionati
         * dalla selezione corrente è {@link Action.Kind#JUMP} con la posizione
         * d'arrivo {@code p}, allora sposta il nodo corrente al corrispondente nodo
         * figlio, annulla la selezione corrente e ritorna la relativa sotto-mossa.
         * Altrimenti non fa nulla e ritorna null.
         * @param p  una posizione
         * @return la sotto-mossa del nuovo nodo corrente o null
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        Move<P> jumpSelection(Pos p);

        /** Se la prima azione della sotto-mossa di uno dei nodi figli selezionati
         * dalla selezione corrente è {@link Action.Kind#MOVE} con parametri
         * {@code d} e {@code ns}, allora sposta il nodo corrente al corrispondente
         * nodo figlio, annulla la selezione corrente e ritorna la relativa
         * sotto-mossa. Altrimenti non fa nulla e ritorna null.
         * @param d  direzione di movimento
         * @param ns  numero passi
         * @return la sotto-mossa del nuovo nodo corrente o null
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        Move<P> moveSelection(Board.Dir d, int ns);

        /** Sposta il nodo corrente al nodo genitore e ritorna la sotto-mossa inversa
         * della sotto-mossa del precedente nodo corrente. Per la definizione di
         * sotto-mossa inversa si veda
         * <a href="http://twiki.di.uniroma1.it/pub/Metod_prog/RS2_ESAMI/progetto.html">Progetto</a>.
         * Se il nodo corrente è la radice (o l'albero è vuoto), non fa nulla e
         * ritorna null.
         * @return la sotto-mossa inversa della sotto-mossa del precedente nodo
         * corrente o null
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        Move<P> back();

        /** @return true se il nodo corrente è finale, false altrimenti
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        boolean isFinal();

        /** Se il nodo corrente è finale, sceglie la mossa corrispondente. Dopo che
         * il metodo è stato invocato con successo, questo oggetto non è più
         * utilizzabile e tutti i suoi metodi lanciano {@link IllegalStateException}.
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto o l'albero è vuoto o il nodo corrente non è finale */
        void move();

        /** @return true se {@link Move.Kind#PASS} è una mossa valida, false
         * altrimenti.
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        boolean mayPass();

        /** Se {@link Move.Kind#PASS} è una mossa valida, sceglie la mossa
         * {@link Move.Kind#PASS}. Dopo che il metodo è stato invocato con successo,
         * questo oggetto non è più utilizzabile e tutti i suoi metodi lanciano
         * {@link IllegalStateException}.
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto o se {@link Move.Kind#PASS} non è una mossa valida */
        void pass();

        /** Sceglie la mossa {@link Move.Kind#RESIGN}. Dopo che il metodo è stato
         * invocato con successo, questo oggetto non è più utilizzabile e tutti i
         * suoi metodi lanciano {@link IllegalStateException}.
         * @throws IllegalStateException se è già stata scelta una mossa o il tempo
         * è scaduto */
        void resign();
    }

    private String name;
    private Consumer<MoveChooser<P>> master;
    private GameRuler<P> gameRul = null;

    /** Crea un {@link PlayerGUI} con il nome e il master specificati. L'oggetto
     * {@link Consumer} master ad ogni richiesta di una mossa (vedi
     * {@link PlayerGUI#getMove()}) accetta, tramite il metodo
     * {@link Consumer#accept(Object)}, un {@link MoveChooser} e lo usa per scegliere
     * una mossa valida tramite una GUI. Il metodo {@link Consumer#accept(Object)} di
     * master deve ritornare imediatamente, non deve quindi attendere che la mossa
     * sia scelta. L'oggetto master deve essere thread-safe perché il suo metodo
     * {@link Consumer#accept(Object)} sarà invocato in più thread.
     * @param name  nome del giocatore
     * @param master  il master
     * @throws NullPointerException se {@code name} o {@code master} è null */
    public PlayerGUI(String name, Consumer<MoveChooser<P>> master) {
        if(name == null || master == null) { throw new NullPointerException("Il nome del player o il suo master non possono essere null"); }
        this.name = name;
        this.master = master;
    }

    @Override
    public String name() { return name; }

    /** Il gioco può essere impostato una sola volta. In altre parole questo oggetto
     * può essere usato solamente per una partita. Nelle invocazioni succesive alla
     * prima lancia {@link IllegalArgumentException}.
     * @throws IllegalArgumentException se un gioco è stato già impostato */
    @Override
    public void setGame(GameRuler<P> g) {
        if(g == null) { throw new NullPointerException("Il gioco non può essere null"); }
        if(gameRul != null) { throw new IllegalArgumentException("Il gioco è già stato impostato"); }
        gameRul = g;
    }

    @Override
    public void moved(int i, Move<P> m) {
        if(m == null) { throw new NullPointerException("La mossa non può essere null"); }
        if(gameRul == null || gameRul.result() != -1) { throw new IllegalArgumentException("La partita non è impostata o è già terminata"); }
        if(!gameRul.isPlaying(i) || !gameRul.isValid(m)) { throw new IllegalArgumentException(); }

        gameRul.move(m);
    }

    /** Crea un {@link MoveChooser} per l'attuale situazione di gioco e lo passa
     * al {@link Consumer} {@code master} che è stato impostato nel costruttore di
     * questo oggetto. Dopo di che si mette in attesa che il {@code master} scelga
     * una mossa valida tramite l'oggetto {@link MoveChooser} che gli è stato passato.
     * Non appena la mossa è scelta, si risveglia dall'attesa e ritorna la mossa
     * scelta. Più precisamente, se il gioco non pone limiti di tempo sulle mosse,
     * questo metodo attende in uno stato {@link Thread.State#WAITING} se invece il
     * gioco pone un limite di tempo, attende in uno stato
     * {@link Thread.State#TIMED_WAITING} e se non viene scelta una mossa entro la
     * scadenza, allora esce dall'attesa, rende inutilizzabile il {@link MoveChooser}
     * e ritorna la mossa {@link Move.Kind#RESIGN}. Se il thread in cui è invocato
     * il metodo è interrotto durante l'attesa, il metodo esce immediatamente
     * dall'attesa, rende inutilizzabile il {@link MoveChooser} e ritorna null.
     * <br>
     * <b>Importante: il metodo non usa thread addizionali creati dal metodo stesso
     * e il {@link MoveChooser} creato è thread-safe perchè è usato in almeno un
     * thread differente, quello che gestisce la GUI.</b> */
    @Override
    public Move<P> getMove() {
        Chooser c = new Chooser(gameRul);
        master.accept(c);

        try {
            while (c.finalChoice == null) { Thread.currentThread().sleep(100); }
        }
        catch (InterruptedException e) { return null; }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return c.finalChoice;
    }


    private class Chooser implements MoveChooser<P> {

        private Node root;
        private GameRuler<P> gR;
        private Set<Pos> selection;
        private Move<P> finalChoice = null;

        public Chooser(GameRuler<P> gr) {

            this.gR = gr;

            selection = new HashSet<>();

            root = createTree(new Node(null), gr.validMoves());
        }

        private Node createTree(Node node, Set<Move<P>> moves)
        {
            Map<Move<P>, Set<Move<P>>> movesMap = new HashMap<>();

            for (Move<P> move: moves)
            {
                if (move.kind == Move.Kind.ACTION)
                {
                    if (node.prefix == null && movesMap.size() == 0)
                    {
                        node.prefix = move;
                    }
                    else if (node.prefix != null && movesMap.size() == 0)
                    {
                        if (node.prefix.actions.get(0).equals(move.actions.get(0)))
                        {
                            Move<P> newPref;
                            int count = 0;

                            for (int i = 0; i < Math.min(node.prefix.actions.size(), move.actions.size()); i++)
                            {
                                if (node.prefix.actions.get(i).equals(move.actions.get(i)))
                                {
                                    count++;
                                }
                            }
                            newPref = new Move<>(node.prefix.actions.subList(0, count));

                            movesMap.put(newPref, new HashSet<>());
                            movesMap.get(newPref).add(new Move(move.actions.subList(count, move.actions.size())));
                            movesMap.get(newPref).add(new Move(node.prefix.actions.subList(count, node.prefix.actions.size())));

                            node.prefix = newPref;
                        }
                        else
                        {
                            movesMap.put(node.prefix, new HashSet<>());
                            movesMap.get(node.prefix).add(node.prefix);
                            movesMap.put(move, new HashSet<>());
                            movesMap.get(move).add(move);
                            node.prefix = null;
                        }
                    }
                    else if (node.prefix != null && movesMap.size() > 0)
                    {
                        Move<P> newPref;
                        int count = 0;

                        for (int i = 0; i < Math.min(node.prefix.actions.size(), move.actions.size()); i++)
                        {
                            if (node.prefix.actions.get(i).equals(move.actions.get(i)))
                            {
                                count++;
                            }
                        }
                        newPref = new Move<>(node.prefix.actions.subList(0, count));

                        if (newPref.equals(node.prefix))
                        {
                            movesMap.get(node.prefix).add(new Move<>(move.actions.subList(count, move.actions.size())));
                        }
                        else
                        {
                            if (!movesMap.containsKey(newPref))
                            {
                                movesMap.put(newPref, new HashSet<>());
                            }
                            movesMap.get(newPref).add(new Move(move.actions.subList(count, move.actions.size())));
                            movesMap.get(newPref).add(new Move(node.prefix.actions.subList(count, node.prefix.actions.size())));

                            node.prefix = newPref;
                        }
                    }
                    else if (node.prefix == null && movesMap.size() > 0)
                    {
                        movesMap.put(move, new HashSet<>());
                        movesMap.get(move).add(move);
                    }
                }
            }

            if (node.parent != null && node.parent.prefix != null)
            {
                List<Action<P>> actions = new ArrayList<>();

                for (Action<P> action: node.prefix.actions)
                {
                    if (!node.parent.prefix.actions.contains(action))
                    {
                        actions.add(action);
                    }
                }
                node.subMove = new Move<>(actions);
            }
            else if (node.parent != null && node.parent.prefix == null)
            {
                node.subMove = new Move<>(node.prefix.actions);
            }

            for (Move<P> move: movesMap.keySet())
            {
                node.children.add(createTree(new Node(node), movesMap.get(move)));
            }
            return node;
        }


        @Override
        public Optional<Move<P>> subMove()
        {
            if (finalChoice != null) throw new IllegalStateException();

            if (root == null)
            {
                return null;
            }
            else if (root.subMove == null)
            {
                return Optional.empty();
            }
            else
            {
                return Optional.of(root.subMove);
            }
        }

        @Override
        public List<Move<P>> childrenSubMoves()
        {
            if (finalChoice != null) throw new IllegalStateException();

            if (root == null)
            {
                return null;
            }

            List<Move<P>> childrenSubMoves = new ArrayList<>();

            for (Node node: root.children)
            {
                if (node.subMove != null)
                {
                    childrenSubMoves.add(node.subMove);
                }
            }
            return childrenSubMoves;
        }

        @Override
        public List<Move<P>> select(Pos... pp)
        {
            if (finalChoice != null) throw new IllegalStateException();
            if (pp.length == 0) throw new IllegalArgumentException();

            clearSelection();

            for (Pos pos: pp)
            {
                Objects.requireNonNull(pos);

                if (!gR.getBoard().isPos(pos) || selection.contains(pos)) throw new IllegalArgumentException();

                selection.add(pos);
            }

            if (root == null)
            {
                return null;
            }

            List<Move<P>> selected = new ArrayList<>();

            for (Node node: root.children)
            {
                Action action = node.subMove.actions.get(0);

                if (selection.contains(action.pos.get(0)))
                {
                    selected.add(node.subMove);
                }
            }
            return selected;
        }

        @Override
        public List<Move<P>> quasiSelected()
        {
            if (finalChoice != null) throw new IllegalStateException();

            if (root == null)
            {
                return null;
            }

            List<Move<P>> subMoves = childrenSubMoves();
            List<Move<P>> quasiSelected = new ArrayList<>();

            for (Move<P> move: subMoves)
            {
                for (Pos pos: move.actions.get(0).pos)
                {
                    if (selection.contains(pos))
                    {
                        quasiSelected.add(move);
                        break;
                    }
                }
            }
            return quasiSelected;
        }

        @Override
        public List<P> selectionPieces()
        {
            if (finalChoice != null) throw new IllegalStateException();

            if (root == null)
            {
                return null;
            }

            List<P> pieces = new ArrayList<>();
            List<Move<P>> quasiSelected = quasiSelected();

            if (quasiSelected.size() == 1 && quasiSelected.get(0).actions.get(0).kind == Action.Kind.REMOVE)
            {
                pieces.add(null);
                return pieces;
            }

            Action.Kind found = null;

            for (Move<P> move: quasiSelected())
            {
                if (found == null && (move.actions.get(0).kind == Action.Kind.ADD || move.actions.get(0).kind == Action.Kind.SWAP))
                {
                    found = move.actions.get(0).kind;
                    pieces.add(move.actions.get(0).piece);
                }
                if (found == move.actions.get(0).kind)
                {
                    pieces.add(move.actions.get(0).piece);
                }
                else
                {
                    pieces.clear();
                    return pieces;
                }
            }
            return pieces;
        }

        @Override
        public void clearSelection()
        {
            if (finalChoice != null) throw new IllegalStateException();

            selection.clear();
        }

        @Override
        public Move<P> doSelection(P pm)
        {
            if (finalChoice != null) throw new IllegalStateException();

            List<Move<P>> quasiSelected = quasiSelected();
            List<P> pieces = new ArrayList<>();

            if (quasiSelected.size() == 1 && quasiSelected.get(0).actions.get(0).kind == Action.Kind.REMOVE)
            {
                for (Node node: root.children)
                {
                    if (node.subMove == quasiSelected.get(0))
                    {
                        root = node;
                        selection.clear();
                        return root.subMove;
                    }
                }
            }

            Action.Kind found = null;

            for (Move<P> move: quasiSelected())
            {
                if (found == null && (move.actions.get(0).kind == Action.Kind.ADD || move.actions.get(0).kind == Action.Kind.SWAP))
                {
                    found = move.actions.get(0).kind;
                    pieces.add(move.actions.get(0).piece);
                }
                if (found == move.actions.get(0).kind)
                {
                    pieces.add(move.actions.get(0).piece);
                }
                else
                {
                    return null;
                }
            }

            if (pieces.contains(pm))
            {
                for (Node node: root.children)
                {
                    if (node.subMove.equals(quasiSelected.get(pieces.indexOf(pm))))
                    {
                        root = node;
                        selection.clear();
                        return root.subMove;
                    }
                }
            }
            return null;
        }

        @Override
        public Move<P> jumpSelection(Pos p)
        {
            if (finalChoice != null) throw new IllegalStateException();

            List<Move<P>> quasiSelected = quasiSelected();

            for (Node node: root.children)
            {
                for (Move<P> move: quasiSelected)
                {
                    if (node.subMove.equals(move) &&
                            move.actions.get(0).kind == Action.Kind.JUMP &&
                            move.actions.get(0).pos.get(1).equals(p))
                    {
                        root = node;
                        selection.clear();
                        return root.subMove;
                    }
                }
            }
            return null;
        }

        @Override
        public Move<P> moveSelection(Board.Dir d, int ns)
        {
            if (finalChoice != null) throw new IllegalStateException();

            List<Move<P>> quasiSelected = quasiSelected();

            for (Node node: root.children)
            {
                for (Move<P> move: quasiSelected)
                {
                    if (node.subMove.equals(move) &&
                            move.actions.get(0).kind == Action.Kind.MOVE &&
                            move.actions.get(0).dir == d &&
                            move.actions.get(0).steps == ns)
                    {
                        root = node;
                        selection.clear();
                        return root.subMove;
                    }
                }
            }
            return null;
        }

        @Override
        public Move<P> back()
        {
            if (finalChoice != null) throw new IllegalStateException();

            if (root == null || root.parent == null)
            {
                return null;
            }

            List<Action<P>> actions = new ArrayList<>();

            for (Action action: root.parent.subMove.actions)
            {
                Action<P> inverseAction = null;
                if (action.kind == Action.Kind.REMOVE)
                {
                    for (Pos p: (List<Pos>)action.pos)
                    {
                        inverseAction = new Action(p, gR.getBoard().get(p));
                        actions.add(inverseAction);
                    }
                }
                else if (action.kind == Action.Kind.ADD)
                {
                    inverseAction = new Action(action.pos.get(0));
                    actions.add(inverseAction);
                }
                else if (action.kind == Action.Kind.JUMP)
                {
                    inverseAction = new Action((Pos)action.pos.get(1), (Pos)action.pos.get(0));
                    actions.add(inverseAction);
                }
                else if (action.kind == Action.Kind.MOVE)
                {
                    List<Pos> positions = new ArrayList<>();
                    for (Pos p: (List<Pos>)action.pos)
                    {
                        Pos newPos = null;
                        for (int i = 0; i < action.steps; i++)
                        {
                            newPos = gR.getBoard().adjacent(p, Utils.opposite(action.dir));
                        }
                        positions.add(newPos);
                    }
                    inverseAction = new Action(Utils.opposite(action.dir), action.steps, (Pos[])positions.toArray());
                    actions.add(inverseAction);
                }
                else
                {
                    inverseAction = new Action(gR.mechanics().pieces.get(2 - (1 + gR.mechanics().pieces.indexOf(action.piece))), (Pos[])action.pos.toArray());
                }
                actions.add(inverseAction);
            }
            return new Move<>(actions);
        }

        @Override
        public boolean isFinal()
        {
            if (finalChoice != null) throw new IllegalStateException();

            if (root == null || root.children.size() > 0)
            {
                return false;
            }

            return true;
        }

        @Override
        public void move()
        {
            if (finalChoice != null || !isFinal()) throw new IllegalStateException();

            finalChoice = root.prefix;
        }

        @Override
        public boolean mayPass()
        {
            if (finalChoice != null) throw new IllegalStateException();

            return gR.isValid(new Move<>(Move.Kind.PASS));
        }

        @Override
        public void pass()
        {
            if (finalChoice != null || !mayPass()) throw new IllegalStateException();

            finalChoice = new Move<>(Move.Kind.PASS);
        }

        @Override
        public void resign()
        {
            if (finalChoice != null) throw new IllegalStateException();

            finalChoice = new Move<>(Move.Kind.RESIGN);
        }


        private class Node
        {
            private Node parent;
            private Move<P> prefix;
            private Move<P> subMove;
            private List<Node> children;

            public Node(Node parent)
            {
                this.parent = parent;

                children = new ArrayList<>();
            }
        }
    }
}
