package de.infotutorien.simplepoll.endpoint;

import static de.infotutorien.simplepoll.util.ResponseHelper.notFound;

import de.infotutorien.simplepoll.config.PollUser;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.Polls;
import io.dropwizard.auth.Auth;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/poll")
public class PollFetchEndpoint {

  private final Polls polls;

  public PollFetchEndpoint(Polls polls) {
    this.polls = polls;
  }

  @GET
  public JsonPoll getPoll(@Auth PollUser user, @NotNull @QueryParam("id") UUID id) {
    Optional<Poll> pollOptional = polls.getPoll(id);

    if (pollOptional.isEmpty()) {
      throw new WebApplicationException(notFound());
    }

    Poll poll = pollOptional.get();

    if (user.getId().equals(poll.getCreator())) {
      return JsonPoll.fromPrivilegedPoll(poll);
    }
    return JsonPoll.fromUnprivilegedPoll(poll);
  }
}
