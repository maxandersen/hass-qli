package dk.xam.hassq.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Area(
    @JsonProperty("area_id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("aliases") String[] aliases,
    @JsonProperty("picture") String picture,
    Map<String, Object> unknown) {

    public Area {
        unknown = new HashMap<>();
    }

    @JsonAnySetter
    public void setUnknown(String name, Object value) {
        unknown.put(name, value);
    }
}
