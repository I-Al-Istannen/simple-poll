package de.infotutorien.simplepoll.config;

import java.security.Principal;
import java.util.UUID;

public class PollUser implements Principal {

  private final UUID id;
  private final String name;

  public PollUser(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * @return the unique id
   */
  public UUID getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }
}
