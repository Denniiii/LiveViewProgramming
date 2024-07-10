package abgaben;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

Clerk.markdown(STR."""
# NimMC

_Dennis Diener, Technische Hochschule Mittelhessen_

##Disclaimer

Leider kann ich aus irgendeinem Grund nicht mehr eine Instanz von TM aufrufen und dadurch nicht mehr die View programmieren. T_T
""" );

public class TM {
    private Tape tape;
    private Table table;
    private String currentState;

    public TM(int tapeSize, char vbz, String startState) {
        tape = new Tape(tapeSize, vbz);
        table = new Table();
        currentState = startState;
    }

    public void addTransition(String fromState, char read, char write, Move move, String toState) {
        table.addTransition(fromState, read, write, move, toState);
    }

    void step() {
        if (currentState.equals("HALT")) {
            return;
        }
        char symbol = tape.read();
        Action action = table.getAction(currentState, symbol);
        if (action == null) {
            return;
        }
        tape.write(action.write);
        tape.move(action.move);
        currentState = action.State;
    }

    public void run() {
        while (!currentState.equals("HALT")) {
            step();
        }
    }

    Turtle turtle = new Turtle(400, 400);
}

enum Move {
    LEFT, RIGHT
}

class Tape {
    private ArrayList<Character> tape;
    private int index; //Position
    private char vbz = ' '; //Vorbelegungszeichen = vbz
    
    public Tape(int size, char vbz) {   //Konstruktor des Tapes
        tape = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tape.add(vbz);
        }
        index = 0;
        this.vbz = vbz;
    }

    //Auslesen des aktuellen Symbols
    char read() {
        return tape.get(index);
    }

    //Schreibe in die aktuelle Zelle/Position
    void write(char symbol) {
            tape.set(index, symbol);
    }

    //Bewege dich
    void move(Move dir) { 
        if (dir == Move.LEFT) {
            index--;
            if (index < 0) {
                tape.add(0, vbz);
                index = 0;
            }
        } else {
            index++;
            if (index >= tape.size()) {
                tape.add(vbz);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder bandString = new StringBuilder();
        for (int i = 0; i < tape.size(); i++) {
            if (i == index) {
                bandString.append("#");
            }
            bandString.append(tape.get(i));
            if (i == index) {
                bandString.append("#");
            }
            bandString.append(" ");
        }
        return bandString.toString();
    }
}

class Table {
    private Map<Trigger, Action> transitions;

    public Table() {
        transitions = new HashMap<>();
    }

    public void addTransition(String fromState, char read, char write, Move move, String toState) {
        transitions.put(new Trigger(fromState, read), new Action(write, move, toState));
    }

    public Action getAction(String fromState, char read) {
        return transitions.get(new Trigger(fromState, read));
    }
}

class Trigger {
    String fromState;
    char read;

    Trigger(String fromState, char read) {
        this.fromState = fromState;
        this.read = read;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;                                    //Null abweheren!   - Nullfalschig
        if (other == this) return true;                                     //Bin ich es selbst?- Ich ja
        if (other.getClass() != getClass()) return false;                   //Andere Klasse?    - Klasse gleich?
        Trigger trigger = (Trigger)other;                                   //Casting           - 
        return read == trigger.read && fromState.equals(trigger.fromState); //Vergleich         - Was definiert Gleichheit?
    }
}

class Action {
    char write;
    Move move;
    String State;

    Action(char write, Move move, String State) {
        this.write = write;
        this.move = move;
        this.State = State;
    }
}

/*   
    Ein Array oder eine --Liste-- für das Band. -> Gemacht
    Einen Index oder eine Position für den Lese-/Schreibekopf. -> Gemacht
    Eine Datenstruktur für den Satz der Zustände und Übergangsregeln (z.B. HashMap oder 2D-Array).
    Eine Schleife oder ähnliche Kontrollstruktur, um die Übergänge zu verarbeiten. 
*/