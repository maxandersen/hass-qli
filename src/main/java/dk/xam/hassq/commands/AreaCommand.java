package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.stringToFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.ConfigProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.vandermeer.asciitable.AsciiTable;
import dk.xam.hassq.HomeAssistantWS;
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

       final var at = new AsciiTable();
       at.addRule();
       at.addRow("id", "name");
       at.addRule();
        // print states matching the p regex
        areas.stream()
            .filter(s -> p.matcher(s.name()).find())
            .forEach(s -> at.addRow(s.id(), s.name()));

        at.addRule();
        //at.getRenderer().setCWC(new CWC_LongestWordMax(new int[]{400,400}));
		System.out.println(at.render());
     
    }
}
