package de.infotutorien.simplepoll;

import de.infotutorien.simplepoll.config.PollAuthenticator;
import de.infotutorien.simplepoll.config.PollConfig;
import de.infotutorien.simplepoll.config.PollUser;
import de.infotutorien.simplepoll.endpoint.PollFetchEndpoint;
import de.infotutorien.simplepoll.endpoint.PollCreateModifyEndpoint;
import de.infotutorien.simplepoll.endpoint.PollVoteEndpoint;
import de.infotutorien.simplepoll.model.Polls;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;

public class Main extends Application<PollConfig> {

  @Override
  public void run(PollConfig pollConfig, Environment environment) {
    // API authentication
    environment.jersey().register(
        new AuthDynamicFeature(
            new BasicCredentialAuthFilter.Builder<PollUser>()
                .setAuthenticator(new PollAuthenticator())
                .buildAuthFilter()
        )
    );
    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(PollUser.class));

    Polls polls = new Polls();
    environment.jersey().register(new PollFetchEndpoint(polls));
    environment.jersey().register(new PollCreateModifyEndpoint(polls));
    environment.jersey().register(new PollVoteEndpoint(polls));
  }

  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }
}
