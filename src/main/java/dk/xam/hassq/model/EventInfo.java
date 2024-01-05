package dk.xam.hassq.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EventInfo(
    @JsonProperty("event") String event,
    @JsonProperty("listener_count") int listenerCount,
    Map<String, Object> unknown) {

    public EventInfo {
        unknown = new HashMap<>();
    }

    @JsonAnySetter
    public void setUnknown(String name, Object value) {
        unknown.put(name, value);
    }
}
