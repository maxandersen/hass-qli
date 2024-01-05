package dk.xam.hassq.commands;

import static dk.xam.hassq.Util.column;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import io.quarkus.logging.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import dk.xam.hassq.HomeAssistant;
import dk.xam.hassq.PPrinter;
import dk.xam.hassq.Util;
import dk.xam.hassq.model.EntityState;

@Command(name = "state")
public class StateCommand extends BaseCommand {

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

    void render(List<EntityState> data) {
      if(parent.json()) {
          System.out.println(pretty.string(data));
          return;
      } else {
        System.out.println(Util.table().data(data,
                    List.of(column("ID").with(e -> unicode((e.id()))),
                            column("STATE").maxWidth(20).with(e -> e.state()))));
      }
    };
    
        Map<String, String> domainIcons = Map.ofEntries(
            Map.entry("light", "\uD83D\uDCA1 "), // 💡
            Map.entry("switch", "\uD83D\uDD0C "), // 🔌
            Map.entry("sensor", "\uD83D\uDCE6 "), // 📦
            Map.entry("climate", "\uD83C\uDF21️ "), // 🌡️
            Map.entry("media_player", "\uD83C\uDFB5 "), // 🎵
            Map.entry("camera", "\uD83D\uDCFD "), // 🎥
            Map.entry("lock", "\uD83D\uDD12 "), // 🔒
            Map.entry("alarm_control_panel", "\u23F0 "), // ⏰
            Map.entry("binary_sensor", "\uD83D\uDD18 "), // 🔘
            Map.entry("cover", "\uD83D\uDEAA "), // 🚪
            Map.entry("fan", "\uD83D\uDCA8 "), // 💨
            Map.entry("number", "\u2116"), // №
            Map.entry("automation", "\u2699️ "), // ⚙️
            Map.entry("scene", "\uD83C\uDFAC "), // 🎬
            Map.entry("script", "\uD83D\uDCDC "), // 📜
            Map.entry("vacuum", "\uD83E\uDDF9 "), // 🧹
            Map.entry("weather", "\u2600️ "), // ☀️
            Map.entry("person", "\uD83D\uDC64 "), // 👤
            Map.entry("group", "\uD83D\uDC65 "), // 👥
            Map.entry("input_boolean", "\u2705 "), // ✅
            Map.entry("input_select", "\uD83D\uDD3D "), // 🔽
            Map.entry("timer", "\u23F2️ "), // ⏲️
            Map.entry("remote", "\uD83D\uDCF1 "), // 📱
            Map.entry("device_tracker", "\uD83D\uDCCC ") // 📌
        );

    private String unicode(String entity) {
      if(entity==null || !entity.contains(".")) return entity;

      String domain = entity.substring(0, entity.indexOf("."));

      return domainIcons.getOrDefault(domain, "") + entity;
   }

   @Command(name = "get")
    public void list(String entityId) {
       var s = ha.getState(entityId);
       render(List.of(s));
    }

    @Command(name = "edit")
    public void edit(String entityId) {
       var s = ha.getState(entityId);
      
       try {
           Path tempFile = java.nio.file.Files.createTempFile("tempEntityState", ".json");
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
