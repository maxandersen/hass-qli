package dk.xam.hassq.commands;

import static picocli.CommandLine.Spec.Target.MIXEE;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

/**
 * This class is just used to tell hassql commands they have some global options that can be used
 * by all commands. We parse and fetch them naively in the main method of hassq thus we don't need
 * to store them here. Just here to have better usage description and not have parsing fail picocli
 * does get to participate.
 */
public class GlobalOptions {
    private @Spec(MIXEE) CommandSpec mixee; // spec of the command where the @Mixin is used

    private static GlobalOptions getTopLevelCommandLoggingMixin(CommandSpec commandSpec) {
        return ((hassq) commandSpec.root().userObject()).globalOptions;
    }

    /** Ensures command methods also have globla options */
    /*public static int executionStrategy(ParseResult parseResult) {
        getTopLevelCommandLoggingMixin(parseResult.commandSpec()).configureLoggers();
        return new CommandLine.RunLast().execute(parseResult);
    }*/
    
    /**
     * Sets the specified verbosity on the LoggingMixin of the top-level command.
     * @param verbosity the new verbosity value
     */
    @Option(names = {"--hass-token"}, description = {
            "Token to use for authentication."})
    public void setToken(String token) {
       // we grab this value befor main thus for now we don't need/care to store it
       // but if we wanted to we could do something like this
       // ((MyApp) mixee.root().userObject()).loggingMixin.verbosity = verbosity;
    }

     /**
     * Sets the specified verbosity on the LoggingMixin of the top-level command.
     * @param verbosity the new verbosity value
     */
    @Option(names = {"--hass-server"}, description = {
        "Server to use for authentication. Defaults to http://homeassistant.local:8123"})
    public void setServer(String server) {
    // we grab this value befor main thus for now we don't need/care to store it
    // but if we wanted to we could do something like this
    // ((MyApp) mixee.root().userObject()).loggingMixin.verbosity = verbosity;
    }

    @Option(names = {"--debug"}, description = {"Debug output"})
    boolean debug;
    
}