package dk.xam.hassq.commands;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.ParentCommand;

public class BaseCommand {

    /** Home Assistant server url */
    @ConfigProperty(name="hass-server", defaultValue="http://localhost:8123")
    String hass_server;
    
    @ParentCommand hassq parent;

    @Mixin GlobalOptions globalOptions;
    
}
