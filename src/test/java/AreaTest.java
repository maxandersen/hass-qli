import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dk.xam.hassq.HomeAssistantWS;
import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainLauncher;
import io.quarkus.test.junit.main.QuarkusMainTest;
import jakarta.inject.Inject;

@QuarkusMainTest
public class AreaTest {


    @Test
    @Launch({"area", "create", "areatocreate"})
    public void testCreateArea(LaunchResult result) {        
        assertThat(result.getErrorOutput()).isEmpty();
        assertThat(result.getOutput()).contains("Created area: areatocreate");
    }

    @Test
    @Launch({"area", "delete", "testarea"})
    public void testDeleteArea(LaunchResult result) {        
        assertThat(result.getErrorOutput()).isEmpty();
        assertThat(result.getOutput()).contains("Deleted area: testarea");
    }

    @Test
    @Launch
    public void testRenameArea(LaunchResult result, QuarkusMainLauncher launcher) {   
        launcher.launch("area", "create", "testarea");
        launcher.launch("area", "rename", "testarea", "newname");
        assertThat(result.getErrorOutput()).isEmpty();
        assertThat(result.getOutput()).contains("Renamed area: testarea");
    }

    @Test
    @Launch(value = {}, exitCode = 0)
    public void testLaunchCommandSucces(LaunchResult result) {
        
        assertThat(result.getOutput()).contains("Usage: hassq");

    }

   
}