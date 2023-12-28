package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.xam.hassq.HomeAssistant;
import dk.xam.hassq.PPrinter;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.Entity;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "state")
public class StateCommand extends BaseCommand {

    /** Home Assistant server url */
    @ConfigProperty(name="hass-server", defaultValue="http://localhost:8123")
    String hass_server;

    @Inject PPrinter pretty;
    
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
      if(parent.json()) {
          System.out.println(pretty.string(data));
          return;
      } else {
        System.out.println(Util.table().data(data,
                    List.of(column("ID").with(e -> e.id()),
                            column("STATE").maxWidth(20).with(e -> e.state()))));
      }
    };
    
    @Command(name = "get")
    public void list(String entityId) {
       var s = ha.getState(entityId);
       render(List.of(s));
    }

    @Command(name = "edit")
    public void edit(String entityId) {
       var s = ha.getState(entityId);
      
       try {
           Path tempFile = java.nio.file.Files.createTempFile("tempEntity", ".json");
           FileWriter writer = new FileWriter(tempFile.toFile());
           writer.write(pretty.string(s));
           writer.close();

           String editor = System.getenv("VISUAL") != null ? System.getenv("VISUAL") : System.getenv("EDITOR");
           if (editor == null) {
               editor = System.getProperty("os.name").startsWith("Windows") ? "notepad" : "vi";
           }
           long originalLastModified = Files.getLastModifiedTime(tempFile).toMillis();

           Log.debug("Using editor: " + editor);
           ProcessBuilder pb = new ProcessBuilder(editor, tempFile.toString());
           pb.inheritIO();
           Process process = pb.start();
           process.waitFor();

           Thread.sleep(1000); // wait for a second to ensure the file has been saved

           if (Files.getLastModifiedTime(tempFile).toMillis() > originalLastModified) {
               Log.debug("File modified, updating state");
               ObjectMapper mapper = new ObjectMapper();
               var result = mapper.readTree(tempFile.toUri().toURL());
               ha.updateState(entityId, result);
           } else {
               Log.debug("File not modified, skipping update");
           }

           Files.delete(tempFile);
          } catch (Exception e) {
           e.printStackTrace();
       }
    }

    @Command
    public void delete(String entityId) {
      var response = ha.deleteState(entityId);

      if(response.getStatus() != 200) {
          System.out.println("Error deleting " + entityId + ": " + response.getStatus());
          return;
      } else {
         System.out.println("Deleted " + entityId);
      }
    }
}
