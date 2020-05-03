package de.infotutorien.simplepoll;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;
import de.infotutorien.simplepoll.endpoint.CreatePollEndpoint;
import de.infotutorien.simplepoll.endpoint.GetPollEndpoint;
import de.infotutorien.simplepoll.endpoint.RevealPollEndpoint;
import de.infotutorien.simplepoll.endpoint.VoteOnPollEndpoint;
import de.infotutorien.simplepoll.model.Polls;
import de.infotutorien.simplepoll.util.AuthorizationHelper;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    Gson gson = new Gson();
    Polls polls = new Polls();

    port(8080);

    before((request, response) -> AuthorizationHelper.ensureUserIdPresent(request.session()));
    before((request, response) -> response.header("Content-Type", "application/json"));

    post("/create", new CreatePollEndpoint(gson, polls));
    post("/reveal", new RevealPollEndpoint(gson, polls));
    post("/vote", new VoteOnPollEndpoint(gson, polls));
    get("/get", new GetPollEndpoint(gson, polls));

    internalServerError(
        (request, response) -> gson.toJson(Map.of("error", "Internal server error"))
    );
    notFound((request, response) -> gson.toJson(Map.of("error", "not found")));
  }

}
