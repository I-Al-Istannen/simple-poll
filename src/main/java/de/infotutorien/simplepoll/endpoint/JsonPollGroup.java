package de.infotutorien.simplepoll.endpoint;

import de.infotutorien.simplepoll.model.PollGroup;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JsonPollGroup {

  private final List<JsonPoll> polls;
  private final UUID id;
  private final UUID creator;
  private final String name;

  public JsonPollGroup(List<JsonPoll> polls, UUID id, UUID creator, String name) {
    this.polls = polls;
    this.id = id;
    this.creator = creator;
    this.name = name;
  }

  public List<JsonPoll> getPolls() {
    return polls;
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

  public static JsonPollGroup fromUnprivilegedPollGroup(PollGroup poll) {
    return new JsonPollGroup(
        poll.getAllPolls()
            .stream()
            .map(JsonPoll::fromUnprivilegedPoll)
            .collect(Collectors.toList()),
        poll.getId(),
        poll.getCreator(),
        poll.getName()
    );
  }

  public static JsonPollGroup fromPrivilegedPollGroup(PollGroup poll) {
    return new JsonPollGroup(
        poll.getAllPolls().stream().map(JsonPoll::fromPrivilegedPoll).collect(Collectors.toList()),
        poll.getId(),
        poll.getCreator(),
        poll.getName()
    );
  }
}
