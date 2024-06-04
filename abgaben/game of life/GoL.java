Clerk.markdown(STR."""
# Game of Life

_Dennis Diener_, _Technische Hochschule Mittelhessen_

Bei der folgenden Dokumentation werde ich kurz die Befehle für das Game of Life (GoL) erklären und die Implementierung in Java zeigen.

*Vorab: Die Leinwand der Turtle wird automatisch beim erstellen von einem GoL Objekt erstellt und die View realisiert sich auch automatisch beim ausführen von der run() Methode.*

In der Jshell können folgende Kommandos ausgeführt werden:

Befehl | Auswirkung
-------|-----------
`run(int steps)`        | Führt die GoL Simulation für die angegebene Anzahl an Schritten aus
`set(int row, int col)` | Setzt eine Zelle an die angegebenen Position
`rotate()`              | Rotiert die GoL Welt um 90 Grad im Uhrzeigersinn
`insert(int row, int col, GoL source)` | Fügt eine kleinere Zellenwelt in die aktuelle Spielwelt ein

Die Geschwindigkeit der Animation kann man mit der Variable `speed` einstellen. Die Geschwindigkeit wird in Millisekunden angegeben.
*Da sonst die Demo zu lange dauern würde, habe ich die Geschwindigkeit auf 10ms gesetzt.*
""");

Clerk.markdown(STR."""

## Kurze Eklärung zur View

Aus Zeit und Nerventechnischen Gründen habe ich die View so implementiert, dass sie sich bei einem Aufruf wie run(10) selbst erstellen soll.
Die Leinwand wird schon beim aufruf eines GoL Objektes erstellt (wie oben schon erwähnt). Das Zeichnen geschieht dann in der run() Methode.
     
```java
\{Text.cutOut("./GoL.java", "// View")}
```

""");

// View
void run(int steps){
    for(int i=1;i<=steps;i++) {
        startPosition(turtle); //Startposition
        rasterVerLine(turtle); //Male die vertikalen Linien
        rasterHorLine(turtle); //Male die horizontalen Linien
        timestep();for(int j=0;j<world.length;j++) {
            int rows=j/col+1;
            int colomn=j%col+1;
            int vertical=turtle.height/row;
            int horizontal=turtle.width/col;
        //System.out.println("Index: " + j + ", Row: " + rows + ", Column: " + colomn + ", Vertical: " + vertical + ", Horizontal: " + horizontal); // Testing/Debugging
            if(world[j]==alive) {
                turtle.penUp().forward(colomn*horizontal-(horizontal/2)).left(90).forward(rows*vertical-(vertical/2));
                drawCell(turtle);
                turtle.left(180).forward(rows*vertical-(vertical/2)).right(90).forward(colomn*horizontal-(horizontal/2)).right(180);
            } try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                System.out.println("Thread wurde unterbrochen");
                return; // Beendet die Ausführung der Methode
            }
        }
    }
}
// View

Clerk.markdown(STR."""
### Und hier eine kleine Demo:

```java
\{Text.cutOut("./GoL.java", "// Game of Life")}
```

""");

public class GoL {
    int[] world;
    int alive = 1;
    int dead = 0;
    int row; //Zeilen -> Horizontal
    int col; //Spalten -> Vertikal
    int speed = 10; //Thread.sleep geschwindigkeit in ms
    
    //Index in der Welt berechnen -> (row-1) * max col + (col-1)!
    Turtle turtle;
    
    GoL(int row, int col) {
        this.row = row;
        this.col = col;
        world = new int[row * col];
        int tHeight = row * 100;
        int tWidth = col * 100;
        turtle = new Turtle(tHeight, tWidth);
    }
    
    
    void run(int steps) {
        for (int i = 1; i <= steps; i++) {
            startPosition(turtle); //Startposition
            rasterVerLine(turtle); //Male die vertikalen Linien
            rasterHorLine(turtle); //Male die horizontalen Linien
            timestep();
            for(int j = 0; j < world.length; j++) {
                int rows = j / col+1;
                int colomn = j % col+1;
                int vertical = turtle.height / row;
                int horizontal = turtle.width / col;
                //System.out.println("Index: " + j + ", Row: " + rows + ", Column: " + colomn + ", Vertical: " + vertical + ", Horizontal: " + horizontal); // Ausgabe der Werte
                if(world[j] == alive) {
                    turtle.penUp().forward(colomn * horizontal-(horizontal/2)).left(90).forward(rows * vertical-(vertical/2));
                    drawCell(turtle);
                    turtle.left(180).forward(rows * vertical-(vertical/2)).right(90).forward(colomn * horizontal-(horizontal/2)).right(180);
                }
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    System.out.println("Thread wurde unterbrochen");
                    return; // Beendet die Ausführung der Methode
                }
            }
        }
    }
    
    void startPosition(Turtle t) {
        turtle.reset(); //Zurücksetzen
        turtle.penUp().forward(turtle.width/2).left(90).forward(turtle.height/2).left(90); //Startposition
    }
    
    void rasterVerLine(Turtle t) {
        int vertical = turtle.width / col;
        for (int i = 1; i < col; i++) {
            t.penUp().forward(i * vertical).left(90).penDown().forward(turtle.height).penUp().left(180).forward(turtle.height).right(90).forward(i * vertical).right(180);
        }
    }
    
    void rasterHorLine(Turtle t) {
        int horizontal = turtle.height / row;
        for (int i = 1; i < row; i++) {
            t.penUp().left(90).forward(i * horizontal).right(90).penDown().forward(turtle.width).penUp().left(180).forward(turtle.width).left(90).forward(i * horizontal).left(90);
        }
    }
    
    void drawCell(Turtle t) {
        int horizontal = turtle.height / row;
        int vertical = turtle.width / col;
        int maxRange = Math.min(horizontal, vertical) / 2 -5; //Größe der Zelle -> "Padding" zum Rand 5 px
        t.penDown();
        for (int i = 0; i < 360; i++) {
            t.color(255, 255, 0); //Gelb
            //t.color(255, 0, 255); //Rot
            //t.color(0, 255, 255); //Blau
            t.lineWidth(5); //Breite der Linie
            t.forward(maxRange).left(180).forward(maxRange).left(10);
        }
        t.penUp();
    }
    
    int rule(int center) {
        //Zähle lebende Nachbarn
        int count = 0;
        int centerRow = center / col;
        int centerCol = center % col;
        
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; //Zentrum überspringen
                int neighborRow = centerRow + i;
                int neighborCol = centerCol + j;
                //Überprüfen ob Nachbarzelle existiert
                if (neighborRow >= 0 && neighborRow < row && neighborCol >= 0 && neighborCol < col) {
                    int neighborIndex = neighborRow * col + neighborCol;
                    //Überprüfen ob Nachbarzelle lebendig ist
                    if (world[neighborIndex] == alive) {
                        count++;
                    }
                }
            }
        }
        //Regeln
        if(count == 3 && world[center] == dead) {                       //Unbelebte "center" Zelle hat exakt 3 lebende Nachbarn -> wird belebt
        return alive;
        } else if(count <= 1 && world[center] == alive) {               //Lebende "center" Zelle hat nur eine oder keine Nachbarn -> stirbt
        return dead;
        } else if((count == 2 || count == 3) && world[center] == alive){//Lebende "center" Zelle hat zwei oder drei Nachbarn -> bleibt am Leben
        return alive;
        } else if(count > 3 && world[center] == alive) {                //Lebende "center" Zelle hat  mehr als drei lebende Nachbarn -> stirbt
        return dead;
        }
        return dead;                                                    //Ansonsten bleibt die Zelle tot
    }

    GoL set(int row, int col) {                             //Gezielte einsetzen von Zellen
        world[(row - 1) * this.col + (col - 1)] = alive;    //Zelle an der Stelle wird belebt
        return this;                                        //GoL Objekt zurückgeben
    }

    void timestep() {                                       //Zeitschritt
        int[] newWorld = new int[world.length];             //Neue Welt erstellen
        for(int i = 0; i < world.length; i++) {             //Alle Zellen durchgehen
            newWorld[i] = rule(i);                          //Neuen Wert berechnen und in newWorld speichern
        }
        world = newWorld;                                   //Welt aktualisieren
    }

    GoL rotate() {
        GoL rotated = new GoL(col, row);                                //Neue GoL Welt mit vertauschten Zeilen und Spalten
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                int sourceIndex = i * col + j;                          //Index in der Quellwelt
                int targetIndex = j * row + (row - 1 - i);              //Vertauschen der Zeilen und Spalten
                rotated.world[targetIndex] = this.world[sourceIndex];   //Zelle in Zielwelt wird mit Zelle in Quellwelt überschrieben
            }
        }
        return rotated;
    }

    GoL insert(int row, int col, GoL source) {                              //Kleinere Zellenwelt einsetzen
        for(int i = 0; i < source.row; i++) {
            for(int j = 0; j < source.col; j++) {
                int sourceIndex = i * source.col + j;                       //Index in der Quellwelt
                int targetIndex = (row + i - 1) * this.col + (col + j - 1); //Index in der Zielwelt
                world[targetIndex] = source.world[sourceIndex];             //Zelle in Zielwelt wird mit Zelle in Quellwelt überschrieben
            }
        }
        return this;                                                        //GoL Objekt zurückgeben
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < world.length; i++) {
            if(i % col == 0) {
                str += "\n";
            }
            if(world[i] == alive) {
                str += "X";
            } else {
                str += ".";
            }
        }
        return str;
    }
}


// Game of Life
GoL glider = new GoL(3,3).set(1,3).set(2,3).set(3,3).set(2,1).set(3,2);
GoL smallWorld = new GoL(20,15).insert(2,1,glider);
glider.run(50);
smallWorld.run(5);
// Game of Life