package de.infotutorien.simplepoll.endpoint;

import static de.infotutorien.simplepoll.util.ResponseHelper.notFound;
import static de.infotutorien.simplepoll.util.ResponseHelper.withError;
import static java.util.function.Function.identity;

import de.infotutorien.simplepoll.config.PollUser;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.PollEntry;
import de.infotutorien.simplepoll.model.Polls;
import de.infotutorien.simplepoll.model.UserVote;
import io.dropwizard.auth.Auth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

@Produces(MediaType.APPLICATION_JSON)
@Path("/vote")
public class PollVoteEndpoint {

  private final Polls polls;

  public PollVoteEndpoint(Polls polls) {
    this.polls = polls;
  }

  @POST
  public JsonPoll vote(@Auth PollUser user, @Valid PollVoteRequest voteRequest) {
    Optional<Poll> pollOptional = polls.getPoll(voteRequest.getPollId());

    if (pollOptional.isEmpty()) {
      throw new WebApplicationException(notFound());
    }

    Poll poll = pollOptional.get();

    if (poll.isRevealResults()) {
      throw new WebApplicationException(withError(Status.BAD_REQUEST, "Voting closed"));
    }

    Map<UUID, PollEntry> entries = poll.getEntries()
        .stream()
        .collect(Collectors.toMap(PollEntry::getId, identity()));

    boolean allExist = voteRequest.getVotes().stream()
        .allMatch(it -> entries.containsKey(it.getEntryId()));

    if (!allExist) {
      throw new WebApplicationException(withError(Status.BAD_REQUEST, "Not all entries exist!"));
    }

    for (PollEntryVote vote : voteRequest.getVotes()) {
      PollEntry pollEntry = entries.get(vote.getEntryId());

      Object value = switch (pollEntry.getType()) {
        case BOOLEAN -> Boolean.parseBoolean(vote.getValue());
        case TEXT -> vote.getValue();
      };

      poll.addVote(new UserVote<>(pollEntry.getId(), user.getId(), value));
    }

    return JsonPoll.fromUnprivilegedPoll(poll);
  }

  private static class PollVoteRequest {

    @NotNull
    private final UUID pollId;
    @NotNull
    private final List<PollEntryVote> votes;

    public PollVoteRequest(UUID pollId, List<PollEntryVote> votes) {
      this.pollId = pollId;
      this.votes = votes;
    }

    public UUID getPollId() {
      return pollId;
    }

    public List<PollEntryVote> getVotes() {
      return votes;
    }
  }

  private static class PollEntryVote {

    @NotNull
    private final UUID entryId;
    @NotNull
    private final String value;

    public PollEntryVote(UUID entryId, String value) {
      this.entryId = entryId;
      this.value = value;
    }

    public UUID getEntryId() {
      return entryId;
    }

    public String getValue() {
      return value;
    }
  }
}
