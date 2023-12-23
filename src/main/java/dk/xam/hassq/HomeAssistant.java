package dk.xam.hassq;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import dk.xam.hassq.model.Config;
import dk.xam.hassq.model.DomainInfo;
import dk.xam.hassq.model.Entity;
import dk.xam.hassq.model.EventInfo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
@ClientHeaderParam(name = "Authorization", value = "Bearer ${hass-token}")
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface HomeAssistant {

    @GET @Path("/") Map<String, String> status();

    @GET @Path("/config") Config getConfig();

    @GET @Path("events") Set<EventInfo> getEvents();

    @GET @Path("services") List<DomainInfo> getServices();

    @GET @Path("states") List<Entity> getStates();

    @GET @Path("states/{entity}") Entity getState(String entity);

    @GET @Path("error_log") String getErrorLog();

    @GET @Path("/api/camera_proxy/{entity}") String getCameraProxy(String entity);
}