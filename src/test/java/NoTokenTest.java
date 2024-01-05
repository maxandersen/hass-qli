import static org.assertj.core.api.Assertions.assertThat;

import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
 
@QuarkusMainTest
@TestProfile(NoTokenTestProfile.class)
public class NoTokenTest {

    @Test
    @Launch()
    public void testNoToken(LaunchResult result) {
        assertThat(result.getErrorOutput()).contains("Did you forget to set the hass-token property?");
    }

    @Test
    @Launch(value = {}, exitCode = 0)
    public void testLaunchCommandSucces(LaunchResult result) {
        
        assertThat(result.getOutput()).contains("Usage: hassq");

    }

    @Test
    @Launch(value = {"config","full"}, exitCode = 0)
    public void testManualLaunch(LaunchResult result) {
        Assertions.assertEquals(0, result.exitCode());
       assertThat(result.getOutput()).contains("Usage: hassq");
       assertThat(result.getErrorOutput()).contains("hass-token");

    }
}