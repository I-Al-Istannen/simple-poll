package de.infotutorien.simplepoll.endpoint;

import com.google.gson.Gson;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.Polls;
import de.infotutorien.simplepoll.model.UserVote;
import de.infotutorien.simplepoll.util.AuthorizationHelper;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

public class VoteOnPollEndpoint implements Route {

  private final Gson gson;
  private final Polls polls;

  public VoteOnPollEndpoint(Gson gson, Polls polls) {
    this.gson = gson;
    this.polls = polls;
  }

  @Override
  public Object handle(Request request, Response response) {
    PollVoteRequest pollRequest = gson.fromJson(request.body(), PollVoteRequest.class);

    Optional<Poll> pollOpt = polls.getPoll(pollRequest.pollId);

    if (pollOpt.isEmpty()) {
      System.out.println(polls);
      response.status(HttpStatus.NOT_FOUND_404);
      return gson.toJson(Map.of("error", "Not found"));
    }

    String userid = AuthorizationHelper.userid(request.session());

    Poll poll = pollOpt.get();
    poll.addVote(new UserVote(
        pollRequest.pollEntryId, userid, pollRequest.value
    ));

    response.status(HttpStatus.OK_200);
    return gson.toJson(Map.of("success", true));
  }

  private static class PollVoteRequest {

    UUID pollId;
    UUID pollEntryId;
    String value;

    public PollVoteRequest(UUID pollId, UUID pollEntryId, String value) {
      this.pollId = pollId;
      this.pollEntryId = pollEntryId;
      this.value = value;
    }
  }
}
