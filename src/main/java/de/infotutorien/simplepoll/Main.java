package de.infotutorien.simplepoll;

import de.infotutorien.simplepoll.config.PollAuthenticator;
import de.infotutorien.simplepoll.config.PollConfig;
import de.infotutorien.simplepoll.config.PollUser;
import de.infotutorien.simplepoll.endpoint.PollCreateModifyEndpoint;
import de.infotutorien.simplepoll.endpoint.PollFetchEndpoint;
import de.infotutorien.simplepoll.endpoint.PollGroupEndpoint;
import de.infotutorien.simplepoll.endpoint.PollVoteEndpoint;
import de.infotutorien.simplepoll.model.Polls;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class Main extends Application<PollConfig> {

  @Override
  public void run(PollConfig pollConfig, Environment environment) {
    configureCors(environment);

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
    environment.jersey().register(new PollGroupEndpoint(polls));
  }

  private void configureCors(Environment environment) {
    var filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    filter.setInitParameter(
        CrossOriginFilter.ALLOWED_METHODS_PARAM,
        "GET,PUT,POST,DELETE,OPTIONS,PATCH"
    );
    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    filter.setInitParameter(
        "allowedHeaders",
        "Content-Type,Authorization,X-Requested-With,Content-Length,Accept-Encoding,Origin"
    );
    filter.setInitParameter("allowCredentials", "true");
  }


  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }
}
