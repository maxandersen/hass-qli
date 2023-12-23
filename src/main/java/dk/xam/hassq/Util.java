package dk.xam.hassq;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class Util {

    static public Pattern stringToFilter(Optional<String> filter) {
        return filter.isPresent() ? Pattern.compile(filter.get()) : Pattern.compile(".*");
    }

    static public String str(String s) {
        return Objects.toString(s,"");
    }
}
