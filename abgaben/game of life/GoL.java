public class GoL {
    int[] world;
    int alive = 1;
    int dead = 0;
    int row; //Zeilen -> Horizontal
    int col; //Spalten -> Vertikal
    
    //Index in der Welt berechnen -> (row-1) * max col + (col-1)!
    Turtle turtle = new Turtle(400, 400);
    
    GoL(int row, int col) {
        this.row = row;
        this.col = col;
        world = new int[row * col];
    }
    
    void run(int steps) {
        for (int i = 1; i <= steps; i++) {
            turtle.reset(); //Zurücksetzen
            turtle.penUp().forward(200).left(90).forward(200).left(90); //Oberer rechter Rand Startposition
            rasterVerLine(turtle);
            rasterHorLine(turtle);
            timestep();
            for(int j = 0; j < world.length; j++) {
                int row = j / col;
                int colomn = j % col;
                int vertical = 400 / col;
                int horizontal = 400 / (row+1);
                if(world[j] == alive) {
                    turtle.penUp().forward(colomn * vertical-(vertical/2)).left(90).forward(row * horizontal-(horizontal/2));
                    drawCell(turtle);
                    turtle.left(180).forward(row * horizontal-(horizontal/2)).right(90).forward(colomn * vertical-(vertical/2)).right(180);
                }
            }
        }
    }

    private Turtle rasterVerLine(Turtle t) {
        int vertical = 400 / col;
        for (int i = 1; i < col; i++) {
            t.penUp().forward(i * vertical).left(90).penDown().forward(400).penUp().left(180).forward(400).right(90).forward(i * vertical).right(180);
        }
        return t;
    }

    private Turtle rasterHorLine(Turtle t) {
        int horizontal = 400 / row;
        for (int i = 1; i < row; i++) {
            t.penUp().left(90).forward(i * horizontal).right(90).penDown().forward(400).penUp().left(180).forward(400).left(90).forward(i * horizontal).left(90);
        }
        return t;
    }

    private Turtle drawCell(Turtle t) {
        t.right(45).penDown();
        for (int i = 0; i < 4; i++) {
            t.forward(20).right(180).penUp().forward(20).left(90).penDown();
        }
        return t.penUp().left(45);
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
                //  Überprüfen ob Nachbarzelle existiert
                if (neighborRow >= 0 && neighborRow < row && neighborCol >= 0 && neighborCol < col) {
                    int neighborIndex = neighborRow * col + neighborCol;
                    //  Überprüfen ob Nachbarzelle lebendig ist
                    if (world[neighborIndex] == alive) {
                        count++;
                    }
                }
            }
        }

        if(count == 3 && world[center] == dead) {                       //Unbelebte "center" Zelle hat exakt 3 lebende Nachbarn -> wird belebt
            return world[center] = alive;
        } else if(count <= 1 && world[center] == alive) {               //Lebende "center" Zelle hat nur eine oder keine Nachbarn -> stirbt
            return world[center] = dead;
        } else if((count == 2 || count == 3) && world[center] == alive){//Lebende "center" Zelle hat zwei oder drei Nachbarn -> bleibt am Leben
            return world[center] = alive;
        } else if(count > 3 && world[center] == alive) {                //Lebende "center" Zelle hat  mehr als drei lebende Nachbarn -> stirbt
            return world[center] = dead;
        }
        return world[center];
    }

    GoL set(int row, int col) {                             //Gezielte einsetzen von Zellen
        world[(row - 1) * this.col + (col - 1)] = alive;    //Zelle an der Stelle wird belebt
        return this;                                        //GoL Objekt zurückgeben
    }

    void timestep() {                                       //Zeitschritt
        for(int i = 0; i < world.length; i++) {
            rule(i);
        }
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