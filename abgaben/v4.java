package abgaben;
interface Calc {
    int add(int x, int y);
    int neg(int x);
    default int sub (int x, int y) {
        return add(x, neg(y));
    }
}

// class CalcImpl implements Calc {
//     public int add(int x, int y) { 
//         return x + y; 
//     }

//     public int neg(int x) { 
//         return -x; 
//     }
// }

Calc c = new Calc() {       //Anonyme Klasse, dass selbe wie oben
    public int add(int x, int y) { 
        return x + y; 
    }

    public int neg(int x) { 
        return -x; 
    }
};

class Calculator {
    int add(int x, int y) {
        return x + y;
    }
    int sub(int x, int y) {
        return x - y;
    }
}

var calc = new Calculator() {
    int neg(int x) {
        return sub(x, 2*x);
    }
};