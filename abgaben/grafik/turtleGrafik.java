//new Turtle
Turtle t = new Turtle();
Turtle tu = new Turtle();
Random rand = new Random();

void hexa(Turtle t, double size) {
    int n = rand.nextInt(100);
    int deg = rand.nextInt(75);
    for (int j = 0; j < n; j++) {
        for (int i = 0; i <= 7; i++) {
            t.forward(size / 3).right(45);
        }
        t.right(deg);
    }
}

void tri(Turtle t, double size) {
    int n = rand.nextInt(12);
    int deg = rand.nextInt(60);
    for (int j = 0; j < n; j++) {    
        for (int i = 0; i <= 2; i++) {
            t.forward(size / 3).right(120);
        }
        t.right(deg);
    }
    for (int j = 0; j < n; j++) {    
        for (int i = 0; i <= 2; i++) {
            t.forward(size / 2).right(120);
        }
        t.right(deg);
    }
    for (int j = 0; j < n; j++) {    
        for (int i = 0; i <= 2; i++) {
            t.forward(size).right(120);
        }
        t.right(deg);
    }
    for (int j = 0; j < n; j++) {    
        for (int i = 0; i <= 2; i++) {
            t.forward(size / 1.5).right(120);
        }
        t.right(deg);
    }
    for (int j = 0; j < n; j++) {    
        for (int i = 0; i <= 2; i++) {
            t.forward(size / 2.5).right(120);
        }
        t.right(deg);
    }
}

hexa(t, 300);
tri(tu, 300);