package dk.xam.hassq.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

    @Override
    public String[] getVersion() throws Exception {

        Instance<Config> configInstance = CDI.current().select(Config.class);
        String version = configInstance.get().getValue("quarkus.application.version", String.class);
        
        return new String[] { version };
    }

    
}
