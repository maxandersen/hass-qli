package dk.xam.hassq.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DomainInfo (
    @JsonProperty("domain") String domain,
    @JsonProperty("services") Map<String, Service> services) {

    public static record Service(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("fields") Map<String, Field> fields) {
    }

    public static record Field(
        @JsonProperty("required") Boolean required,
        @JsonProperty("example") Object example,
        @JsonProperty("selector") Map<String, Object> selector,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description) {
    }
}
