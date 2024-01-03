package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import dk.xam.hassq.HomeAssistantWS;
import dk.xam.hassq.PPrinter;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Entity;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "entity")
public class EntityCommand extends BaseCommand {

    @Inject PPrinter pretty;
    
    @Inject
    HomeAssistantWS ha;

    @Command(name = "list")
    public void list(@Parameters(arity="0..1") Optional<String> entityFilter, @Option(names="--state") Optional<String> stateFilter) {
        
       var states = ha.getEntities();

       Pattern p = entityFilter.isPresent() ? Pattern.compile(entityFilter.get()) : Pattern.compile(".*");
       Pattern sf = stateFilter.isPresent() ? Pattern.compile(stateFilter.get()) : Pattern.compile(".*");

       Log.info("States matching " + p.pattern());

       states = states.stream().filter(e -> p.matcher(e.id()).find()).toList();

       render(states);

    }

    void render(List<Entity> data) {
      if(parent.json()) {
          System.out.println(pretty.string(data));
          return;
      } else {
        System.out.println(Util.table().data(data,
                    List.of(column("ENTITY_ID").with(e -> e.entityId()),
                            column("NAME").maxWidth(20).with(e -> e.name()==null ? e.originalName() : e.name()),
                            column("DEVICE_ID").with(e -> e.deviceId()),
                            column("PLATFORM").with(e -> e.platform()),
                            column("AREA").with(e -> e.areaId()),
                            column("CONFIG_ENTRY_ID").with(e -> e.configEntryId()),
                            column("DISABLED_BY").with(e -> e.disabledBy())
                            )));
                        
      }
    };
    
      
    }
