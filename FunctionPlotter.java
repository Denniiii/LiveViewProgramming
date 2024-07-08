import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.*;
import java.util.function.DoubleUnaryOperator;

class FunctionPlotter {
    String function;
    Turtle t;
    
    FunctionPlotter(String function) {
        Turtle t = new Turtle();
        this.function = function;
    }

    void parseFunction(String function) {

    }

    void evaluateFunction() {}

    void drawAxes() {}

    void drawFunction() {}

    void scaleX() {}

    void scaleY() {}

    void zoom() {}
}

DoubleUnaryOperator function = x -> x * x + 2 + 3;

Map<String,DoubleUnaryOperator> functions = new HashMap<>();
// interface ParseFunction {
//     static final Map<String,DoubleUnaryOperator> functions = new HashMap<>();
//     static {
//         functions.put("sqrt", Math::sqrt);
//         functions.put("sin", Math::sin);
//         functions.put("cos", Math::cos);
//         functions.put("tan", Math::tan);
//         functions.put("asin", Math::asin);
//         functions.put("acos", Math::acos);
//         functions.put("atan", Math::atan);
//         functions.put("abs", Math::abs);
//         functions.put("log", Math::log10);
//         functions.put("ln", Math::log);
//         functions.put("exp", Math::exp);
//     }
// }
