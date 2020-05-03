package de.infotutorien.simplepoll.endpoint;

import com.google.gson.Gson;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.PollEntry;
import de.infotutorien.simplepoll.model.Polls;
import de.infotutorien.simplepoll.util.AuthorizationHelper;
import java.util.List;
import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreatePollEndpoint implements Route {

  private final Gson gson;
  private final Polls polls;

  public CreatePollEndpoint(Gson gson, Polls polls) {
    this.gson = gson;
    this.polls = polls;
  }

  @Override
  public Object handle(Request request, Response response) {
    PollRequest pollRequest = gson.fromJson(request.body(), PollRequest.class);

    Poll poll = new Poll(
        pollRequest.name,
        UUID.randomUUID(),
        AuthorizationHelper.userid(request.session()),
        pollRequest.entries
    );
    polls.addPoll(poll);

    return gson.toJson(poll);
  }

  private static class PollRequest {

    String name;
    List<PollEntry> entries;

    public PollRequest(String name, List<PollEntry> entries) {
      this.name = name;
      this.entries = entries;
    }
  }
}
