package de.infotutorien.simplepoll.model;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Polls {

  private final Map<UUID, Poll> activePolls;
  private final Map<UUID, PollGroup> pollGroups;

  public Polls() {
    this.activePolls = new ConcurrentHashMap<>();
    this.pollGroups = new ConcurrentHashMap<>();
  }

  public Optional<Poll> getPoll(UUID uuid) {
    return Optional.ofNullable(activePolls.get(uuid));
  }

  public void addGroup(PollGroup group) {
    pollGroups.put(group.getId(), group);
  }

  public Optional<PollGroup> getGroup(UUID uuid) {
    return Optional.ofNullable(pollGroups.get(uuid));
  }

  public List<PollGroup> getGroupsOfUser(UUID creator) {
    return pollGroups.values()
        .stream()
        .filter(it -> it.getCreator().equals(creator))
        .collect(toList());
  }

  public void addPoll(Poll poll) {
    activePolls.put(poll.getId(), poll);
  }
}
