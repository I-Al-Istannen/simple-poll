package de.infotutorien.simplepoll.config;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.Optional;
import java.util.UUID;

public class PollAuthenticator implements Authenticator<BasicCredentials, PollUser> {

  @Override
  public Optional<PollUser> authenticate(BasicCredentials credentials) {
    String username = credentials.getUsername();
    String idString = credentials.getPassword();

    try {
      UUID id = UUID.fromString(idString);
      return Optional.of(new PollUser(id, username));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
