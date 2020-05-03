package de.infotutorien.simplepoll.model;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Polls {

  private final Map<UUID, Poll> activePolls;

  public Polls() {
    this.activePolls = new ConcurrentHashMap<>();
  }

  public Optional<Poll> getPoll(UUID uuid) {
    return Optional.ofNullable(activePolls.get(uuid));
  }

  public void addPoll(Poll poll) {
    activePolls.put(poll.getId(), poll);
  }
}
