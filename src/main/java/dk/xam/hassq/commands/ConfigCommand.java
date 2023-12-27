package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;
import static dk.xam.hassq.Util.str;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.xam.hassq.HomeAssistant;
import dk.xam.hassq.PPrinter;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Config;
import jakarta.inject.Inject;
import jakarta.websocket.DeploymentException;
import picocli.CommandLine.Command;

@Command(name = "config")
public class ConfigCommand extends BaseCommand {

    @Inject @RestClient
    HomeAssistant hass;

    @Inject
    PPrinter pretty;
    
    @Command
    void full() throws IOException, DeploymentException, InterruptedException, ExecutionException, TimeoutException {

       var data = List.of(hass.getConfig());
       
       System.out.println(pretty.string(data));

       var result = Util.table()
                    .data(data,
                    List.of(column("VERSION").with(e -> e.version()),
                            column("DIR").with(e -> e.configDir()),
                            column("LATITUDE").with(e -> str(e.latitude())),
                            column("LONGITUDE").with(e -> str(e.longitude())),
                            column("ELEVATION").with(e -> str(e.elevation())),
                            column("TZ").with(e -> e.timeZone()),
                            column("COMPONENTS").with(e -> str(e.components().size()))
        ));

        System.out.println(result);

    }

    @Command
    void components() throws IOException, DeploymentException, InterruptedException, ExecutionException, TimeoutException {

       var data = hass.getConfig().components();
       
       var result = Util.table()
                    .data(data,
                    List.of(column("COMPONENT").with(e -> e)));

        System.out.println(result);

    }


    void render(List<Config> data) {
        
    }


}
