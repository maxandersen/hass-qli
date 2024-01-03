package dk.xam.hassq.model;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Entity(
    @JsonProperty("area_id") String areaId,
    @JsonProperty("config_entry_id") String configEntryId,
    @JsonProperty("device_id") String deviceId,
    @JsonProperty("disabled_by") String disabledBy,
    @JsonProperty("entity_category") String entityCategory,
    @JsonProperty("entity_id") String entityId,
    @JsonProperty("has_entity_name") Boolean hasEntityName,
    @JsonProperty("hidden_by") String hiddenBy,
    @JsonProperty("icon") String icon,
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("options") Map<String, Map<String, String>> options,
    @JsonProperty("original_name") String originalName,
    @JsonProperty("platform") String platform,
    @JsonProperty("translation_key") String translationKey,
    @JsonProperty("unique_id") String uniqueId
) {}
