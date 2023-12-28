package dk.xam.hassq;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.AsciiTableBuilder;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;

public class Util<T> {

    static public Pattern stringToFilter(Optional<String> filter) {
        return filter.isPresent() ? Pattern.compile(filter.get()) : Pattern.compile(".*");
    }

    static public String str(Object s) {
        return Objects.toString(s,"");
    }

    static public Column column(String name) {
        return new Column().header(name).dataAlign(HorizontalAlign.LEFT);
    }

    public static String str(int i) {
        return i + "";
    }

    public static String str(double latitude) {
        return latitude + "";
    }

    public static AsciiTableBuilder table() {
        return AsciiTable.builder()
                    .border(AsciiTable.NO_BORDERS);
    };
    
}
