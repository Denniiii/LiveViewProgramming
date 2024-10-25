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

@FunctionalInterface interface Parser {
    Result parse(String s);
}

record Result(Optional<String> parsed, String rest) {
    boolean hasFailed() { return parsed.isEmpty(); }
    boolean hasNotFailed() { return !hasFailed(); } 
}

record Success() implements Parser {
    public Result parse(String s) { return new Result(Optional.of(""), s); }
}

record Fail() implements Parser {
    public Result parse(String s) { return new Result(Optional.empty(), s); }
}

record Item(String item) implements Parser {
    public Result parse(String s) {
        if (!s.startsWith(item)) return new Fail().parse(s);
        return new Result(Optional.of(item), s.substring(item.length()));
    }
}

record Or(Parser... parsers) implements Parser {
    public Result parse(String s) {
        return Arrays.stream(parsers)
            .map(p -> p.parse(s))
            .filter(Result::hasNotFailed)   //filter(r -> r.hasNotFailed())
            .findFirst()
            .orElseGet(() -> new Fail().parse(s));
    }
}

record Maybe(Parser p) implements Parser {
    public Result parse(String s) {
        return new Or(p, new Success()).parse(s);
    }
}

record And2(Parser p1, Parser p2) implements Parser {
    public Result parse(String s) {
        Result r1 = p1.parse(s);
        if (r1.hasFailed()) return new Fail().parse(s);
        Result r2 = p2.parse(r1.rest());
        if (r2.hasFailed()) return new Fail().parse(s);
        return new Result(Optional.of(r1.parsed().get() + r2.parsed().get()), r2.rest());
    }
}

record And(Parser... parsers) implements Parser {
    public Result parse(String s) {
        return Arrays.stream(parsers)
            .reduce(new Success(), (x, y) -> new And2(x, y))
            .parse(s);
    }
}

record Many(Parser parser) implements Parser { //Zero or more, as many as possible
    public Result parse(String s) {
        return Stream.<Parser>iterate(parser, p -> new And2(p, parser))
            .map(p -> p.parse(s))
            .takeWhile(Result::hasNotFailed)
            .reduce(new Success().parse(s), (x,y) -> y);
    }
}

record More(Parser parser) implements Parser { //one or more
    public Result parse(String s) {
        return new And2(parser, new Many(parser)).parse(s);
    }
}

record Drop(Parser parser) implements Parser { //apply Parser, drop result
    public Result parse(String s) {
        Result r = parser.parse(s);
        if (r.hasNotFailed()) return new Success().parse(r.rest());
        return r;
    }
}

record Lazy(Supplier<Parser> parser) implements Parser {
    public Result parse(String s) { return parser.get().parse(s); }
}
