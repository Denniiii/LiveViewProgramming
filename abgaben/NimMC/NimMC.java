// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t
//
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

Clerk.markdown(STR."""
# NimMC

_Dennis Diener, Technische Hochschule Mittelhessen_

## Vorab
Da ich in der Woche Krank war und nicht an der Vorlesung und dem Praktikum teilgenommen habe, habe ich bei einem KKommilitonen um Hilfe gebeten und wir haben uns gemeinsam
dran gesetzt und er hat mir viel geholf und erklärt, viel davon verstehen tue ich nicht leider. Aber ich wollte mich noch einige male mit ihm zusammensetzen, damit wir an seinem
und meinem Code arbeiten können und ich hoffentlich auholen kann.
Deshalb wird der Code wahrscheinlich an vielen stellen gleich aussehen und gleiche Fehler haben und ich werde versuchen den Code darauf weiter aufzubauen und zu verbessern.

### Methode 1
possibleMoves()-Methode. Diese soll alle möglichen Züge in einer eingegeben Spielsituation durchgehen und wiedergeben.

```java
\{Text.cutOut("./NimMC.java", "// all Moves")}   
```
""" );

// all Moves
public static List<Move> possibleMoves(Nim nim) { //Muss noch bearbeitet werden
    List<Move> possibleMoves = new ArrayList<>();
    for (int row = 0; row < nim.rows.length; row++) {
        for (int number = 1; number <= nim.rows[row]; number++) {
            possibleMoves.add(Move.of(row, number));
        }
    }
    return possibleMoves;
}
// all Moves

Clerk.markdown(STR."""
### Methode 2
Die zweite Methode ist eine simulation(). Dort werden alle möglichen Züge eines Spiels durchgegangen und in Win und Lose gezählt.
Danach wird in einer List MoveResults wiedergegeben, welche move, win, lose speichert für jeden einzelnen Zug.

```java
\{Text.cutOut("./NimMC.java", "// simulation")}
```
""");

// simulation
public List<MoveResults> simulation(List<Move> possibleMoves) {
    final int N = 100;
    List<MoveResults> moveResults = new ArrayList<>();

    for (Move move : possibleMoves) {
        int win = 0;
        int lose = 0;
        int moveCounter = 0;
        for (int i = 0; i < N; i++) {
            Nim simNim = Nim.of(rows);
            simNim = simNim.play(move);
            moveCounter++;

            boolean simWinner = NimGame.isWinning(simNim.rows);
            if (simWinner) {
                win++;
            } else {
                lose++;
            } moveResults.add(new MoveResults(move, win, lose));
        } return moveResults;
    }
}
// simulation

Clerk.markdown(STR."""
### Methode 3
Des weiteren wird eine Klasse MoveResults mit den Variablen move, win, lose festgelegt.

```java
\{Text.cutOut("./NimMC.java", "// Klasse")}
```
""");

// Klasse
class MoveResults {
    Move move;
    int w; //sieg
    int l; //verloren

    MoveResults(Move move, int win, int lose) {
        this.move = move;
        this.w = win;
        this.l = lose;
    }

    @Override
    public String toString() {
        return "Move: " + move + "Win | Lose: " + w + " | " + l + " /n"; 
    }
}
// Klasse

Clerk.markdown(STR."""
### Methode 4
mcMove soll alle vorherigen Methoden anwenden, um dann das Ergebnis der besten Moves ausrechnet und zurückgibt.

```java
\{Text.cutOut("./NimMC.java", "// mcMove")}
```
""");

// mcMove
public Move mcMove(){
    assert !isGameOver();

    Move mcMove;
    double a = 0;

    List<MoveResults> moveResults = simulation(possibleMoves(null));

    for (MoveResults results : moveResults) {
        double x = results.w / results.l;
        if (x < a) {
            x = a;
            results.move = mcMove;
        }
    }
    return mcMove;
}
// mcMove


//Code

class NimView extends Nim {
    Turtle turtle = new Turtle(400, 400);
    public NimView(int... rows) {
        super(rows);
    }

    private Turtle holz(Turtle t) {                                                     //Streichholz zeichnen und zum nächsten Anfang gehen
        return t.forward(20).penUp().left(90).forward(20).left(90).forward(20).left(180).penDown(); 
    } 

    private Turtle jump(Turtle t, int i) {
        return t.penUp().right(90).forward(i * 20).left(90).forward(40).penDown();
    }

    public void show() {
        turtle.reset(); //Zurücksetzen
        turtle.penUp().left(90).forward(150).left(90).forward(150).left(90).penDown();//Startposition
        for (int i = 0; i < getRows().length; i++) {
            for (int j = 0; j < getRows()[i]; j++) {
                holz(turtle);
            }
            jump(turtle, getRows()[i]); //getRows um zu schauen ob es funktioniert wenn das Spiel gespielt wird und eine neue show() zeichnen
        }
    }

}

class Move {
    final int row, number;
    static Move of(int row, int number) {
        return new Move(row, number);
    }
    private Move(int row, int number) {
        if (row < 0 || number < 1) throw new IllegalArgumentException();
        this.row = row;
        this.number = number;
    }
    public String toString() {
        return "(" + row + ", " + number + ")";
    }
}

interface NimGame {
    static boolean isWinning(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (i,j) -> i ^ j) != 0;
        // klassische Variante:
        // int res = 0;
        // for(int number : numbers) res ^= number;
        // return res != 0;
    }
    NimGame play(Move... moves);
    Move randomMove();
    Move bestMove();
    boolean isGameOver();
    String toString();
}

class MoveResults {
    Move move;
    int w; //sieg
    int l; //verloren

    MoveResults(Move move, int win, int lose) {
        this.move = move;
        this.w = win;
        this.l = lose;
    }

    @Override
    public String toString() {
        return "Move: " + move + "Win | Lose: " + w + " | " + l + " /n"; 
    }
}


class Nim implements NimGame {
    private Random r = new Random();
    int[] rows;
    public static Nim of(int... rows) {
        return new Nim(rows);
    }
    protected Nim(int... rows) {
        if (rows.length > 5 || Arrays.stream(rows).anyMatch(n -> n < 0 || n > 7)) {     //Hier soll man nur eine gewisse anzahl an Streichholz"reihen" und "Streichhölzer" benutzen dürfen
            throw new IllegalArgumentException("Ein Nim-Spiel enthält maximal fünf Reihen und jede Reihe hat maximal sieben 'Streichhölzer'.");
        }
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
        this.rows = Arrays.copyOf(rows, rows.length);
    }
    private Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }
    public Nim play(Move... moves) {
        Nim nim = this;
        for(Move m : moves) nim = nim.play(m);
        return nim;
    }
    public Move randomMove() {
        assert !isGameOver();
        int row;
        do {
            row = r.nextInt(rows.length);
        } while (rows[row] == 0);
        int number = r.nextInt(rows[row]) + 1;
        return Move.of(row, number);
    }
    public Move bestMove() {
        assert !isGameOver();
        if (!NimGame.isWinning(rows)) return randomMove();
        Move m;
        do {
            m = randomMove();
        } while(NimGame.isWinning(play(m).rows));
        return m;
    }
    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }

    public String toString() {
        String s = "";
        for(int n : rows) s += "\n" + "I ".repeat(n);
        return s;
    }

    public int[] getRows() {
        return rows;
    }

    public Move mcMove(){
        assert !isGameOver();

        Move mcMove;
        double a = 0;

        List<MoveResults> moveResults = simulation(possibleMoves(null));

        for (MoveResults results : moveResults) {
            double x = results.w / results.l;
            if (x < a) {
                x = a;
                results.move = mcMove;
            }
        }
        return mcMove;
    }


    public static List<Move> possibleMoves(Nim nim) { //Muss noch bearbeitet werden
        List<Move> possibleMoves = new ArrayList<>();
        for (int row = 0; row < nim.rows.length; row++) {
            for (int number = 1; number <= nim.rows[row]; number++) {
                possibleMoves.add(Move.of(row, number));
            }
        }
        return possibleMoves;
    }

    public List<MoveResults> simulation(List<Move> possibleMoves) {
        final int N = 100;
        List<MoveResults> moveResults = new ArrayList<>();

        for (Move move : possibleMoves) {
            int win = 0;
            int lose = 0;
            int moveCounter = 0;
            for (int i = 0; i < N; i++) {
                Nim simNim = Nim.of(rows);
                simNim = simNim.play(move);
                moveCounter++;

                boolean simWinner = NimGame.isWinning(simNim.rows);
                if (simWinner) {
                    win++;
                } else {
                    lose++;
                } moveResults.add(new MoveResults(move, win, lose));
            } return moveResults;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;                                        //Null abweheren!   - Nullfalschig
        if (other == this) return true;                                         //Bin ich es selbst?- Ich ja
        if (other.getClass() != getClass()) return false;                       //Andere Klasse?    - Klasse gleich?
        Nim that = (Nim)other;                                                  //Casting           - 
        return Arrays.equals(rows, that.rows);                                  //Vergleich         - Was definiert Gleichheit?
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }
}

//Nim nim = Nim.of(2,3,4);
//assert nim != nim.play(Move.of(1,2)) : "Return a new Nim instance";

int[] randomSetup(int... maxN) {
    Random r = new Random();
    int[] rows = new int[maxN.length];
    for(int i = 0; i < maxN.length; i++) {
        rows[i] = r.nextInt(maxN[i]) + 1;
    }
    return rows;
}

ArrayList<Move> autoplay(NimGame nim) {
    ArrayList<Move> moves = new ArrayList<>();
    while (!nim.isGameOver()) {
        Move m = nim.bestMove();
        moves.add(m);
        nim = nim.play(m);
    }
    return moves;
}

boolean simulateGame(int... maxN) {
    Nim nim = Nim.of(randomSetup(maxN));
    // System.out.println(nim);
    // System.out.println((NimGame.isWinning(nim.rows) ? "first" : "second") + " to win"); 
    ArrayList<Move> moves = autoplay(nim);
    // System.out.println(moves);
    return (NimGame.isWinning(nim.rows) && (moves.size() % 2) == 1) ||
           (!NimGame.isWinning(nim.rows) && (moves.size() % 2) == 0); 
}

assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,5));
assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,6,8));

