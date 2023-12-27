package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;
import static dk.xam.hassq.Util.stringToFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.xam.hassq.HomeAssistantWS;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Area;
import jakarta.inject.Inject;
import jakarta.websocket.DeploymentException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "area")
public class AreaCommand extends BaseCommand {

    @Inject
    HomeAssistantWS hass;

    @Inject
    ObjectMapper mapper;

    @Command
    void create(@Parameters(description="one or more area names to create") List<String> names) {

        
        names.forEach(n -> {
            hass.createArea(n);
            System.out.println("Created area: " + n);
        });
    }

    @Command
    void delete(@Parameters(description="one or more area names to delete") List<String> idOrNames) {

        var areas = hass.getAreas();
        
        idOrNames.forEach(nid -> {
            Area area = areas.stream().filter(a -> a.id().equals(nid) || a.name().equals(nid)).findFirst().orElse(null);
            if(area==null) {
                System.out.println("Area not found: " + nid);
            } else {
                hass.deleteArea(area.id());
               System.out.println("Deleted area: " + area.id());
            }
        });
    }

    @Command
    void rename(@Parameters(description="Old area name") String oldName, @Parameters(description="New area name") String newName) {

        var areas = hass.getAreas();
        
            Area area = areas.stream().filter(a -> a.id().equals(oldName) || a.name().equals(oldName)).findFirst().orElse(null);
            if(area==null) {
                System.out.println("Area not found: " + oldName);
            } else {
                hass.renameArea(area.id(), newName);
               System.out.println("Renamed area: " + area.id());
            }
        }
    


    @Command
    void list(@Parameters(arity="0..1") Optional<String> areaFilter) throws IOException, DeploymentException, InterruptedException, ExecutionException, TimeoutException {
       // var hass = new HomeAssistantWS(mapper, "bogus");

       var areas = hass.getAreas();

       Pattern p = stringToFilter(areaFilter);

       
       render(areas.stream()
            .filter(s -> p.matcher(s.name()).find()).toList());

    }

    void findAreas(String filter) {

    }

    void render(List<Area> data) {
        var result = Util.table().data(data,
                    List.of(column("ID").with(e -> e.id()),
                            column("NAME").with(e -> e.name())));

        System.out.println(result);
    }
}
