package dk.xam.hassq.commands;

import picocli.CommandLine.Mixin;
import picocli.CommandLine.ParentCommand;

public class BaseCommand {

    @ParentCommand hassq parent;

    @Mixin GlobalOptions globalOptions;
    
}
