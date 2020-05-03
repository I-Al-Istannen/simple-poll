package de.infotutorien.simplepoll.model;

import static java.util.function.Function.identity;

import de.infotutorien.simplepoll.model.PollEntry.EntryType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * A poll a user created!
 */
public class Poll {

  private final String humanName;
  private final UUID id;
  private final UUID creator;
  private final List<PollEntry> entries;
  private final Set<UserVote<?>> votes;
  private final AtomicBoolean revealResults;
  private final boolean allowMultiple;

  public Poll(String humanName, UUID id, UUID creator, List<PollEntry> entries,
      boolean allowMultiple) {
    this.humanName = humanName;
    this.id = id;
    this.creator = creator;
    this.entries = new ArrayList<>(entries);
    this.allowMultiple = allowMultiple;
    this.votes = Collections.newSetFromMap(new ConcurrentHashMap<>());
    this.revealResults = new AtomicBoolean();
  }

  public String getHumanName() {
    return humanName;
  }

  public UUID getId() {
    return id;
  }

  public UUID getCreator() {
    return creator;
  }

  public List<PollEntry> getEntries() {
    return Collections.unmodifiableList(entries);
  }

  public Set<UserVote<?>> getVotes() {
    return Collections.unmodifiableSet(votes);
  }

  public void setRevealResults(boolean revealResults) {
    this.revealResults.set(revealResults);
  }

  public boolean isRevealResults() {
    return revealResults.get();
  }

  public boolean isAllowMultiple() {
    return allowMultiple;
  }

  public void addVote(UserVote<?> vote) {
    // Finished
    if (isRevealResults()) {
      return;
    }
    Optional<PollEntry> entry = entries.stream()
        .filter(it -> it.getId().equals(vote.getPollEntry()))
        .findFirst();

    if (entry.isEmpty()) {
      throw new IllegalArgumentException("Invalid poll entry id");
    }

    if (!isAllowMultiple()) {
      Map<UUID, PollEntry> entries = this.entries.stream()
          .collect(Collectors.toMap(PollEntry::getId, identity()));
      votes.removeIf(it -> entries.get(it.getPollEntry()).getType() == EntryType.BOOLEAN);
    }

    votes.add(vote);
  }
}
