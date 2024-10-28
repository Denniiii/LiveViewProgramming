// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t
//
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

Clerk.markdown(STR.
"""
# Nim Turtle View

_Dennis Diener, Technische Hochschule Mittelhessen_

## Implementierung der `equals` und `hashCode` Methoden

### 1. Hashcode Methode

Mit der `hashcode()` Methode wird der Hashcode aus dem Inhalt der einzelnen Reihen berechnet.
> Dadurch entsteht selbst bei unterschiedlicher Anordnung der Reihen der gleiche Hashcode.

```java	
\{Text.cutOut("./Nim.java", "// hashCode")}
```

Dabei wird das Array kopiert und mit einem Quicksort Algorithmus aufsteigend sortiert, danach wird pro Reihe der Wert mit einer Zehnerpotenz multipliziert und aufsummiert, das aufsummierte Ergebnis ist dann der Hashcode.

### 2. Equals Methode

In der Methode werden 5 Abfragen getätigt:
Frage | Umsetzung
------|----------
Ist das Objekt `null`? | `if (other == null) return false;`
Vergleichen wir das gleiche Objekt? | `if (other == this) return true;`
Sind es gleiche Klassen? | `if (other.getClass() != getClass()) return false;`
Casting | `Nim that = (Nim)other;`
Haben die Objekte den selben Inhalt? | `return this.hashCode() == other.hashCode();`

Mit diesen Fragen kann man in der JShell Objekte mit dem Befehl: `.equals(**Objekt**)` aufrufbar.

---

## Implementierung von View 

Die Klasse NimView erweitert Nim und erstellt zuerst eine Turtle-Leinwand, wenn eine Nim-Spiel gestartet wird, danach kann man mit `.show()` den aktuellen Spielstand anzeigen lassen.

        
""");

// hashCode
@Override
boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < sortedRows.length - 1; i++) {                   //Sortiere aufsteigend bsp. 1 2 3 4 (QuickSort)
                if (sortedRows[i] > sortedRows[i + 1]) {
                    int temp = sortedRows[i];
                    sortedRows[i] = sortedRows[i + 1];
                    sortedRows[i + 1] = temp;
                    swapped = true;
                }
            }
        } while (swapped);
        int hash = 0;                                                           //Hashwert intialisieren
        for (int i = 0; i < sortedRows.length; i++) {                           //Berechne den Hashwert
            hash += sortedRows[i] * Math.pow(10, i);                            //Nutze die Zehnerpotenz um die Reihenfolge zu speichern
        }
        return hash;
// hashCode


/*-------------------------------------------------------------------------------------------------------------------------------*/


// Eigentlicher Code 

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
        turtle.reset();
        turtle.penUp().left(90).forward(150).left(90).forward(150).left(90).penDown();//Startposition
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows[i]; j++) {
                holz(turtle);
            }
            jump(turtle, rows[i]);
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

    public static List<Move> possibleMoves(Nim nim) { //Muss noch bearbeitet werden
        List<Move> possibleMoves = new ArrayList<>();
        for (int row = 0; row < nim.rows.length; row++) {
            for (int number = 1; number <= nim.rows[row]; number++) {
                possibleMoves.add(Move.of(row, number));
            }
        }
        return possibleMoves;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;                                        //Null abweheren!   - Nullfalschig
        if (other == this) return true;                                         //Bin ich es selbst?- Ich ja
        if (other.getClass() != getClass()) return false;                       //Andere Klasse?    - Klasse gleich?
        Nim that = (Nim)other;                                                  //Casting           - 
        return this.hashCode() == other.hashCode();                             //Vergleichen
    }

    @Override
    public int hashCode() {
        int sortedRows[] = new int[rows.length];                                //Kopiere das Array
        for (int i = 0; i < rows.length; i++) {                                 
            sortedRows[i] = rows[i];
        }
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < sortedRows.length - 1; i++) {                       //Sortiere aufsteigend bsp. 1 2 3 4 (QuickSort)
                if (sortedRows[i] > sortedRows[i + 1]) {
                    int temp = sortedRows[i];
                    sortedRows[i] = sortedRows[i + 1];
                    sortedRows[i + 1] = temp;
                    swapped = true;
                }
            }
        } while (swapped);
        int hash = 0;                                                           //Hashwert intialisieren
        for (int i = 0; i < sortedRows.length; i++) {                           //Berechne den Hashwert
            hash += sortedRows[i] * Math.pow(10, i);                            //Nutze die Zehnerpotenz um die Reihenfolge zu speichern
        }
        return hash;
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

/* // Beispielhaftes Spiel über JShell
jshell> Nim n = Nim.of(2,3,4)
n ==>
I I
I I I
I I I I
jshell> n = n.play(n.bestMove())
n ==>
I I
I I I
I
jshell> n = n.play(Move.of(2,1))
n ==>
I I
I I I
jshell> n = n.play(n.bestMove())
n ==>
I I
I I
jshell> n = n.play(Move.of(1,1))
n ==>
I I
I
jshell> n = n.play(n.bestMove())
n ==>
I
I
jshell> n = n.play(Move.of(1,1))
n ==>
I
jshell> n = n.play(n.bestMove())
n ==>
jshell> n.isGameOver()
$25 ==> true
*/