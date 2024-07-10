import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.*;

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


// sign := "+" | "-"
// signOpt := sign?#
// integer := "0" |
//            signOpt digit1-9 digit0-9*

Parser sign = new Or(new Item("+"), new Item("-"));
Parser signOpt = new Maybe(sign);
Parser digit1_9 = new Or(new Item("1"), new Item("2"), new Item("3"), 
                         new Item("4"), new Item("5"), new Item("6"),
                         new Item("7"), new Item("8"), new Item("9"));
Parser digit0_9 = new Or(new Item("0"), digit1_9);
Parser integer = new Or(new Item("0"), new And(signOpt, digit1_9, new Many(digit0_9)));
Parser flt = new Or(new And(integer, new Item("."), new Many(digit0_9)));

//p* heißt Zero or more
//der stern * steht für beliebig oft

//reduce x = erstes Element aus der Kette
//reduce y = letztes Element aus der Kette