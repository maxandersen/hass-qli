package dk.xam.hassq.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public record Config(
    @JsonProperty("components") ArrayList<String> components,
    @JsonProperty("config_dir") String configDir,
    @JsonProperty("elevation") int elevation,
    @JsonProperty("latitude") double latitude,
    @JsonProperty("location_name") String locationName,
    @JsonProperty("longitude") double longitude,
    @JsonProperty("time_zone") String timeZone,
    @JsonProperty("unit_system") Map<String, String> unitSystem,
    @JsonProperty("version") String version,
    @JsonProperty("whitelist_external_dirs") ArrayList<String> whitelistExternalDirs,
    Map<String, Object> unknown) {

    public Config {
        unknown = new HashMap<>();
    }

    @JsonAnySetter
    public void setUnknown(String name, Object value) {
        unknown.put(name, value);
    }
}

