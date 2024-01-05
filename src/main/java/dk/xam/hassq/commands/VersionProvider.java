package dk.xam.hassq.commands;

import io.quarkus.arc.Unremovable;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import picocli.CommandLine.IVersionProvider;

/**
 * This class provides the version of the application.
 * It implements the IVersionProvider interface from the picocli library.
 * The @Singleton annotation enables us to inject this into picocli globally inside Quarkus.
 * The @Unremovable annotation is needed to prevent Quarkus from removing this class during build time
 * as Picocli will not be able to find it during runtime.
 * 
 * The version is injected from the "quarkus.application.version" configuration property.
 **/
@Unremovable @Singleton
public class VersionProvider implements IVersionProvider {

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @Override
    public String[] getVersion() throws Exception {
        return new String[] { version };
    }
}
