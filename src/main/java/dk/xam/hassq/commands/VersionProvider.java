package dk.xam.hassq.commands;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Singleton;
import picocli.CommandLine.IVersionProvider;

/**
 * This class provides the version of the application.
 * It implements the IVersionProvider interface from the picocli library.
 * The @Singleton annotation enables us to inject this into picocli globally inside Quarkus.
 * The version is injected from the "quarkus.application.version" configuration property.
 **/
@Singleton
public class VersionProvider implements IVersionProvider {

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @Override
    public String[] getVersion() throws Exception {
        return new String[] { version };
    }
}
