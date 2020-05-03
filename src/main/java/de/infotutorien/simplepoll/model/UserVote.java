package de.infotutorien.simplepoll.model;

import java.util.UUID;

/**
 * The single vote of a user.
 */
public class UserVote<T> {

  private final UUID user;
  private final UUID pollEntry;
  private final T value;

  public UserVote(UUID pollEntry, UUID user, T value) {
    this.pollEntry = pollEntry;
    this.user = user;
    this.value = value;
  }

  public UUID getPollEntry() {
    return pollEntry;
  }

  public UUID getUser() {
    return user;
  }

  public T getValue() {
    return value;
  }
}
