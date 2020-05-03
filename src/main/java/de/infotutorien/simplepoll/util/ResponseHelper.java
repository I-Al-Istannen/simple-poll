package de.infotutorien.simplepoll.util;

import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ResponseHelper {

  public static Response withError(Status status, String error) {
    return Response.status(status).entity(Map.of("error", error)).build();
  }

  public static Response notFound() {
    return withError(Status.NOT_FOUND, "Not found");
  }
}
