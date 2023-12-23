package dk.xam.hassq.commands;

import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMax;
import dk.xam.hassq.HomeAssistant;
import io.quarkus.logging.Log;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "state")
public class StateCommand {

    /** Home Assistant server url */
    @ConfigProperty(name="hass-server", defaultValue="http://localhost:8123")
    String hass_server;

    @RestClient HomeAssistant ha;

    @Command(name = "list")
    public void list(@Parameters(arity="0..1") Optional<String> entityFilter, @Option(names="--state") Optional<String> stateFilter) {
        
       var states = ha.getStates();

       Pattern p = entityFilter.isPresent() ? Pattern.compile(entityFilter.get()) : Pattern.compile(".*");
       Pattern sf = stateFilter.isPresent() ? Pattern.compile(stateFilter.get()) : Pattern.compile(".*");

       Log.info("States matching " + p.pattern());

       final var at = new AsciiTable();
       at.addRule();
       at.addRow("Entity", "State");
       at.addRule();
        // print states matching the p regex
        states.stream()
            .filter(s -> p.matcher(s.id()).find())
            .filter(s -> sf.matcher(s.state()).find())
            .forEach(s -> at.addRow(s.id(), s.state()));

        at.addRule();
        at.getRenderer().setCWC(new CWC_LongestWordMax(new int[]{-1,-1}));
		System.out.println(at.render());
    }

    @Command(name = "get")
    public void list(String entityId) {
       var s = ha.getState(entityId);
       System.out.println(String.format("%s=%s", s.id(), s.state()));
    }
}
