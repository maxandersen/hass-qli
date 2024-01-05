package dk.xam.hassq.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Config(
    @JsonProperty("components") List<String> components,
    @JsonProperty("config_dir") String configDir,
    @JsonProperty("elevation") int elevation,
    @JsonProperty("latitude") double latitude,
    @JsonProperty("location_name") String locationName,
    @JsonProperty("longitude") double longitude,
    @JsonProperty("time_zone") String timeZone,
    @JsonProperty("unit_system") Map<String, String> unitSystem,
    @JsonProperty("version") String version,
    @JsonProperty("whitelist_external_dirs") List<String> whitelistExternalDirs,
    @JsonProperty("allowlist_external_dirs") List<String> allowlistExternalDirs,
    @JsonProperty("allowlist_external_urls") List<String> allowlistExternalUrls,
    @JsonProperty("config_source") String configSource,
    @JsonProperty("recovery_mode") boolean recoveryMode,
    @JsonProperty("state") String state,
    @JsonProperty("external_url") String externalUrl,
    @JsonProperty("internal_url") String internalUrl,
    @JsonProperty("currency") String currency,
    @JsonProperty("country") String country,
    @JsonProperty("language") String language,
    @JsonProperty("safe_mode") boolean safeMode) {

}
