package dk.xam.hassq;

import dk.xam.hassq.commands.AreaCommand;
import dk.xam.hassq.commands.ConfigCommand;
import dk.xam.hassq.commands.StateCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@QuarkusMain
@Command(name = "hassq", mixinStandardHelpOptions = true, subcommands = { StateCommand.class, AreaCommand.class, ConfigCommand.class})
public class hassq implements Runnable, QuarkusApplication {

  @Inject
  HomeAssistantWS hass;

  

    @Override
    public void run() {
      CommandLine.usage(this, System.out);

        /*Log.infof("Hello %s, go go commando!\n", homeAssistant.status().get("message"));

        Log.info("Config component count: " + homeAssistant.getConfig().components().size());

        Log.info("Events:" + homeAssistant.getEvents());

        Log.info("Services:" + homeAssistant.getServices().size());

        Log.info("States:" + homeAssistant.getStates().size());

        Log.info("State of:" + homeAssistant.getState("binary_sensor.always_on"));

      // Log.info("Error log:" + homeAssistant.getErrorLog());

        Log.info("Camera proxy:" + homeAssistant.getCameraProxy("camera.demo_camera"));
        */
    }


    @Inject
    CommandLine.IFactory factory; 

    @Override
    public int run(String... args) throws Exception {
      return new CommandLine(this, factory).execute(args);
    }

    public static void main(String[] args) {
        // poor man hack to get config from command line
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.substring(2).split("=");
                if (parts.length == 2) {
                  //  System.out.println("Setting " + parts[0] + " to " + parts[1]);
                    CLIConfigSource.put(parts[0], parts[1]);
                }
            }
        }
        Quarkus.run(hassq.class, args);
    }

}
