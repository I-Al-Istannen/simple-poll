package de.infotutorien.simplepoll.util;

import java.util.UUID;
import spark.Session;

public class AuthorizationHelper {

  public static final String USER_ID_KEY = "user_id";

  public static String userid(Session session) {
    return session.attribute(USER_ID_KEY).toString();
  }

  /**
   * Wraps a route and ensure {@link #userid(Session)} can find an id.
   */
  public static void ensureUserIdPresent(Session session) {
    if (!session.attributes().contains(USER_ID_KEY)) {
      session.attribute(USER_ID_KEY, UUID.randomUUID());
    }
  }
}
