package de.infotutorien.simplepoll.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PollGroup {

  private final Map<UUID, Poll> polls;
  private final UUID id;
  private final UUID creator;
  private final String name;

  public PollGroup(UUID id, UUID creator, String name) {
    this.polls = new ConcurrentHashMap<>();
    this.id = id;
    this.creator = creator;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public UUID getCreator() {
    return creator;
  }

  public String getName() {
    return name;
  }

  public void addPoll(Poll poll) {
    polls.put(poll.getId(), poll);
  }

  public Collection<Poll> getAllPolls() {
    return Collections.unmodifiableCollection(polls.values());
  }
}
