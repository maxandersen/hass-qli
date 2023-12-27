package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import dk.xam.hassq.HomeAssistant;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Entity;
import io.quarkus.logging.Log;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "state")
public class StateCommand extends BaseCommand {

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

       states = states.stream().filter(e -> p.matcher(e.id()).find() && sf.matcher(e.state()).find()).toList();

       render(states);

    }

    void render(List<Entity> data) {
        System.out.println(Util.table().data(data,
                    List.of(column("ID").with(e -> e.id()),
                            column("STATE").maxWidth(20).with(e -> e.state()))));
    };
    
    @Command(name = "get")
    public void list(String entityId) {
       var s = ha.getState(entityId);
       render(List.of(s));
    }
}
