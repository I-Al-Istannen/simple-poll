package de.infotutorien.simplepoll.model;

import java.util.UUID;

/**
 * The single vote of a user.
 */
public class UserVote {

  private final String user;
  private final UUID pollEntry;
  private final String value;

  public UserVote(UUID pollEntry, String user, String value) {
    this.pollEntry = pollEntry;
    this.user = user;
    this.value = value;
  }

  public UUID getPollEntry() {
    return pollEntry;
  }

  public String getUser() {
    return user;
  }

  public String getValue() {
    return value;
  }
}
