package de.infotutorien.simplepoll.endpoint;

import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.PollEntry;
import de.infotutorien.simplepoll.model.UserVote;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JsonPoll {

  private final String humanName;
  private final UUID id;
  private final List<PollEntry> entries;
  private final Set<UserVote<?>> votes;

  public JsonPoll(String humanName, UUID id, List<PollEntry> entries, Set<UserVote<?>> votes) {
    this.humanName = humanName;
    this.id = id;
    this.entries = entries;
    this.votes = votes;
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

  public static JsonPoll fromUnprivilegedPoll(Poll poll) {
    return new JsonPoll(
        poll.getHumanName(),
        poll.getId(),
        poll.getEntries(),
        poll.isRevealResults() ? poll.getVotes() : Collections.emptySet()
    );
  }

  public static JsonPoll fromPrivilegedPoll(Poll poll) {
    return new JsonPoll(
        poll.getHumanName(),
        poll.getId(),
        poll.getEntries(),
        poll.getVotes()
    );
  }
}
