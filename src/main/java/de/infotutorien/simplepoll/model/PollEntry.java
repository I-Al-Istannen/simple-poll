package de.infotutorien.simplepoll.model;

import java.util.UUID;

public class PollEntry {

  private final String humanName;
  private final UUID id;
  private final EntryType type;

  public PollEntry(String humanName, UUID id, EntryType type) {
    this.humanName = humanName;
    this.id = id;
    this.type = type;
  }

  public String getHumanName() {
    return humanName;
  }

  public UUID getId() {
    return id;
  }

  public EntryType getType() {
    return type;
  }

  /**
   * The type of the entry.
   */
  public enum EntryType {
    /**
     * The user can just select this entry or not.
     */
    BOOLEAN,
    /**
     * The user can enter free form text.
     */
    TEXT
  }
}
