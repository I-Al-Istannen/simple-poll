package de.infotutorien.simplepoll.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.PollEntry;
import de.infotutorien.simplepoll.model.UserVote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class JsonPoll {

  private final String humanName;
  private final UUID id;
  private final UUID creator;
  private final List<PollEntry> entries;
  private final Set<UserVote<?>> votes;
  private final boolean isRevealed;
  private final boolean isAllowMultiple;

  public JsonPoll(String humanName, UUID id, UUID creator, Collection<PollEntry> entries,
      Set<UserVote<?>> votes, boolean isRevealed, boolean isAllowMultiple) {
    this.humanName = humanName;
    this.id = id;
    this.creator = creator;
    this.entries = new ArrayList<>(entries);
    this.votes = votes;
    this.isRevealed = isRevealed;
    this.isAllowMultiple = isAllowMultiple;
  }

  public String getHumanName() {
    return humanName;
  }

  public UUID getId() {
    return id;
  }

  public List<PollEntry> getEntries() {
    return entries;
  }

  public Set<UserVote<?>> getVotes() {
    return Collections.unmodifiableSet(votes);
  }

  public UUID getCreator() {
    return creator;
  }

  public boolean getRevealed() {
    return isRevealed;
  }

  public boolean isAllowMultiple() {
    return isAllowMultiple;
  }

  public static JsonPoll fromUnprivilegedPoll(Poll poll) {
    return new JsonPoll(
        poll.getHumanName(),
        poll.getId(),
        null,
        poll.getEntries(),
        poll.isRevealResults() ? poll.getVotes() : Collections.emptySet(),
        poll.isRevealResults(),
        poll.isAllowMultiple()
    );
  }

  public static JsonPoll fromPrivilegedPoll(Poll poll) {
    return new JsonPoll(
        poll.getHumanName(),
        poll.getId(),
        poll.getCreator(),
        poll.getEntries(),
        poll.getVotes(),
        poll.isRevealResults(),
        poll.isAllowMultiple()
    );
  }
}
