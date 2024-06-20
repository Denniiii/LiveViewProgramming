import java.util.function.*;
import java.util.stream.*;

sealed interface Parser permits Success, Fail, Item, Or, Maybe, And2, And {
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

Parser sign = new Or(new Item("+"), new Item("-"));
Parser signOpt = new Maybe(sign);

record And2(Parser p1, Parser p2) implements Parser {
    public Result parse(String s) {
        Result r1 = p1.parse(s);
        if (r1.hasFailed()) return new Fail().parse(s);
        Result r2 = p2.parse(r1.rest);
        if (r2.hasFailed()) return new Fail().parse(s);
        return new Result(Optional.of(r1.parsed().get() + r2.parsed().get()), r2.rest);
    }
}

record And(Parser... parsers) implements Parser {
    public Result parse(String s) {
        return Arrays.stream(parsers)
            .reduce(new Success(), (x, y) -> new And2(X, y))
            .parse(s);
    }
}

