package de.infotutorien.simplepoll.endpoint;

import com.google.gson.Gson;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.Polls;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetPollEndpoint implements Route {

  private final Gson gson;
  private final Polls polls;

  public GetPollEndpoint(Gson gson, Polls polls) {
    this.gson = gson;
    this.polls = polls;
  }

  @Override
  public Object handle(Request request, Response response) {
    UUID pollId = UUID.fromString(request.queryMap().get("pollId").value());

    Optional<Poll> poll = polls.getPoll(pollId);

    if (poll.isPresent()) {
      return gson.toJson(poll);
    } else {
      response.status(HttpStatus.NOT_FOUND_404);
      return gson.toJson(Map.of("error", "not found"));
    }
  }
}
