package de.infotutorien.simplepoll.endpoint;

import static de.infotutorien.simplepoll.util.ResponseHelper.notFound;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.infotutorien.simplepoll.config.PollUser;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.PollEntry;
import de.infotutorien.simplepoll.model.PollEntry.EntryType;
import de.infotutorien.simplepoll.model.Polls;
import io.dropwizard.auth.Auth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/poll")
public class PollCreateModifyEndpoint {

  private final Polls polls;

  public PollCreateModifyEndpoint(Polls polls) {
    this.polls = polls;
  }

  @PUT
  public JsonPoll createPoll(@Auth PollUser user, @Valid PollCreateRequest pollCreateRequest) {
    Poll poll = new Poll(
        pollCreateRequest.getName(),
        UUID.randomUUID(),
        user.getId(),
        pollCreateRequest.getPollEntries().stream()
            .map(CreatePollEntry::toPollEntry)
            .collect(toList()),
        pollCreateRequest.allowMultiple
    );
    polls.addPoll(poll);

    return JsonPoll.fromPrivilegedPoll(poll);
  }

  @PATCH
  public JsonPoll changePoll(@Auth PollUser user, @Valid PollPatchRequest patch) {
    Optional<Poll> pollOptional = polls.getPoll(patch.getPollId());
    if (pollOptional.isEmpty()) {
      throw new WebApplicationException(notFound());
    }

    Poll poll = pollOptional.get();

    if (!poll.getCreator().equals(user.getId())) {
      throw new WebApplicationException(notFound());
    }

    poll.setRevealResults(patch.isReveal());

    return JsonPoll.fromPrivilegedPoll(poll);
  }

  private static class PollCreateRequest {

    private final boolean allowMultiple;
    @NotNull
    private final String name;
    @Valid
    @NotNull
    private final List<CreatePollEntry> pollEntries;

    public PollCreateRequest(boolean allowMultiple, String name,
        List<CreatePollEntry> pollEntries) {
      this.allowMultiple = allowMultiple;
      this.name = name;
      this.pollEntries = pollEntries;
    }

    public String getName() {
      return name;
    }

    public List<CreatePollEntry> getPollEntries() {
      return pollEntries;
    }

    public boolean isAllowMultiple() {
      return allowMultiple;
    }
  }

  private static class CreatePollEntry {

    @NotNull
    private final String name;
    @NotNull
    private final EntryType type;
    @NotNull
    private final transient UUID id;

    public CreatePollEntry(String name, EntryType type) {
      this.name = name;
      this.type = type;
      this.id = UUID.randomUUID();
    }

    @JsonIgnore
    public PollEntry toPollEntry() {
      return new PollEntry(getName(), id, getType());
    }

    public String getName() {
      return name;
    }

    public EntryType getType() {
      return type;
    }
  }

  private static class PollPatchRequest {

    @NotNull
    private final UUID pollId;
    @NotNull
    private final boolean isReveal;

    public PollPatchRequest(UUID pollId, boolean isReveal) {
      this.pollId = pollId;
      this.isReveal = isReveal;
    }

    public UUID getPollId() {
      return pollId;
    }

    public boolean isReveal() {
      return isReveal;
    }
  }
}
