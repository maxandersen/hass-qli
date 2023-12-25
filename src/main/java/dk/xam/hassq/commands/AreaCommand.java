package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;
import static dk.xam.hassq.Util.stringToFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.ConfigProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import dk.xam.hassq.HomeAssistantWS;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Area;
import jakarta.inject.Inject;
import jakarta.websocket.DeploymentException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "area")
public class AreaCommand {

    @Inject
    HomeAssistantWS hass;

    @Inject
    ObjectMapper mapper;

    @Command
    void list(@Parameters(arity="0..1") Optional<String> areaFilter) throws IOException, DeploymentException, InterruptedException, ExecutionException, TimeoutException {
       // var hass = new HomeAssistantWS(mapper, "bogus");

       var areas = hass.getAreas();

       Pattern p = stringToFilter(areaFilter);

       
       render(areas.stream()
            .filter(s -> p.matcher(s.name()).find()).toList());

    }

    void render(List<Area> data) {
        var result = Util.table().data(data,
                    List.of(column("ID").with(e -> e.id()),
                            column("NAME").with(e -> e.name())));

        System.out.println(result);
    }
}
