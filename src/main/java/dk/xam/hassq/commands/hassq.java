package dk.xam.hassq.commands;

import dk.xam.hassq.CLIConfigSource;
import dk.xam.hassq.HomeAssistantWS;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.inject.spi.DeploymentException;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParseResult;

import java.io.PrintWriter;
@QuarkusMain
@Command(name = "hassq", mixinStandardHelpOptions = true, subcommands = { StateCommand.class, AreaCommand.class, ConfigCommand.class})
public class hassq extends BaseCommand implements Runnable, QuarkusApplication {

  @picocli.CommandLine.Option(names = {"--json"}, description = {"Output as json"}, scope = picocli.CommandLine.ScopeType.INHERIT)
  boolean json;

  public boolean json() {
    return json;
  }

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
      return new CommandLine(this, factory)
      .setExecutionExceptionHandler(this::handleExecutionException)
      .execute(args);
    }

    public static void printExceptionCauseChain(PrintWriter printWriter, Throwable throwable) {
      if (throwable == null) {
          printWriter.println("No exception provided");
          return;
      }
  
      StringBuilder indent = new StringBuilder();
  
      while (throwable != null) {
          printWriter.println(indent + throwable.getClass().getSimpleName() + " - " + throwable.getMessage());
          throwable = throwable.getCause();
          indent.append("  "); // Add two spaces for each level of indentation
      }
  }

  public static Throwable findCauseOfType(Throwable throwable, Class<?> exceptionType) {
    while (throwable != null) {
        if (exceptionType.isInstance(throwable)) {
            return throwable;
        }
        throwable = throwable.getCause();
    }
    return null;
}

    public static void main(String[] args) {
        // poor man hack to get hass- config from command line
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.substring(2).split("=");
                if (parts.length == 2) {
                    //System.out.println("Setting " + parts[0] + " to " + parts[1]);
                    CLIConfigSource.put(parts[0], parts[1]);
                }
            }
        }

        try {
           Quarkus.run(hassq.class, (exitCode, error) -> {
            if(error != null) {
              printExceptionCauseChain(new PrintWriter(System.err), error);

              var cause = findCauseOfType(error, DeploymentException.class);
              if(cause!=null) {
                System.err.println("Did you forget to set the hass-token property?");
                System.err.println("For example by setting the HASS_TOKEN environment variable or by adding -Dhass-token=your-token to the command line");


              }
              CommandLine.usage(new hassq(), System.out);

           }
           Quarkus.asyncExit(exitCode);
         //   System.exit(exitCode); // if we exit here it will fail in devmode
          }, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int handleExecutionException(Exception exception, CommandLine commandLine, ParseResult parseresult) {
      commandLine.getErr().printf("Error invoking: '%s'\n\n", String.join(" ", parseresult.expandedArgs()));
      printExceptionCauseChain(commandLine.getErr(), exception);
      commandLine.usage(commandLine.getErr());
      return commandLine.getCommandSpec().exitCodeOnExecutionException();
    }

}
