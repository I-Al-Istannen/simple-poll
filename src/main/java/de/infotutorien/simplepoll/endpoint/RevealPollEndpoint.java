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

public class RevealPollEndpoint implements Route {

  private final Gson gson;
  private final Polls polls;

  public RevealPollEndpoint(Gson gson, Polls polls) {
    this.gson = gson;
    this.polls = polls;
  }

  @Override
  public Object handle(Request request, Response response) {
    RevealPollRequest pollRequest = gson.fromJson(request.body(), RevealPollRequest.class);

    Optional<Poll> poll = polls.getPoll(pollRequest.pollId);

    if (poll.isPresent()) {
      poll.get().setRevealResults(true);
      return gson.toJson(poll);
    } else {
      response.status(HttpStatus.NOT_FOUND_404);
      return gson.toJson(Map.of("error", "not found"));
    }
  }

  private static class RevealPollRequest {

    UUID pollId;

    public RevealPollRequest(UUID pollId) {
      this.pollId = pollId;
    }
  }
}
