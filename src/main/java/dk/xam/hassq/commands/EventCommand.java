package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.quarkus.logging.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import dk.xam.hassq.HomeAssistantWS;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Area;

@Command(name = "event")
public class EventCommand extends BaseCommand {

    @Inject
    HomeAssistantWS hass;

    @Inject
    ObjectMapper mapper;

    @Command
    void watch(@Parameters(description="Event(s) name to monitor") List<String> event) {

        if(event == null || event.isEmpty()) {
            hass.watch(null, this::callback);
        } else {
            event.forEach(e -> {
                System.out.println("Watching for event: " + e);
                hass.watch(e, this::callback);
            });
        }

        Log.info("Waiting for events...");
        try {
            var msg = hass.waitForMessage();
            System.out.println("Event: " + msg);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    void callback(String msg) {
        System.out.println("Event: " + msg);
    }
    
    @Inject
    dk.xam.hassq.PPrinter pretty;

    void render(List<Area> data) {

        if(parent.json()) {
            System.out.println(pretty.string(data));
            return;
        } else {
            var result = Util.table().data(data,
                        List.of(column("ID").with(e -> e.id()),
                                column("NAME").with(e -> e.name())));

            System.out.println(result);
        }
    }
}
