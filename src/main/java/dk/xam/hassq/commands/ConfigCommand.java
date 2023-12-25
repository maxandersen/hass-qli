package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;
import static dk.xam.hassq.Util.str;
import static dk.xam.hassq.Util.stringToFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.AsciiTableBuilder;
import com.github.freva.asciitable.Column;

import dk.xam.hassq.HomeAssistant;
import dk.xam.hassq.HomeAssistantWS;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Area;
import dk.xam.hassq.model.Config;
import jakarta.inject.Inject;
import jakarta.websocket.DeploymentException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "config")
public class ConfigCommand {

    @Inject @RestClient
    HomeAssistant hass;

    @Inject
    ObjectMapper mapper;

    @Command
    void full() throws IOException, DeploymentException, InterruptedException, ExecutionException, TimeoutException {
       // var hass = new HomeAssistantWS(mapper, "bogus");

       var data = hass.getConfig();
       
       render(List.of(data));

    }

    void render(List<Config> data) {
        var result = Util.table()
                    .data(data,
                    List.of(column("VERSION").with(e -> e.version()),
                            column("DIR").with(e -> e.configDir()),
                            column("LATITUDE").with(e -> str(e.latitude())),
                            column("LONGITUDE").with(e -> str(e.longitude())),
                            column("ELEVATION").with(e -> str(e.elevation())),
                            column("TZ").with(e -> e.timeZone()),
                            column("UNIT_SYSTEM").with(e -> str(e.unitSystem()))
        ));

        System.out.println(result);
    }


}
