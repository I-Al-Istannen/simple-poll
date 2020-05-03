package de.infotutorien.simplepoll.endpoint;

import static de.infotutorien.simplepoll.util.ResponseHelper.notFound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.infotutorien.simplepoll.config.PollUser;
import de.infotutorien.simplepoll.model.Poll;
import de.infotutorien.simplepoll.model.PollGroup;
import de.infotutorien.simplepoll.model.Polls;
import io.dropwizard.auth.Auth;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/group")
public class PollGroupEndpoint {

  private final Polls polls;

  public PollGroupEndpoint(Polls polls) {
    this.polls = polls;
  }

  @PUT
  public PollGroup createGroup(@Auth PollUser user, @Valid PollGroupCreationRequest request) {
    PollGroup group = new PollGroup(
        UUID.randomUUID(),
        user.getId(),
        request.getName()
    );
    polls.addGroup(group);

    return group;
  }

  @PATCH
  public PollGroup addPoll(@Auth PollUser user, @Valid PollAddRequest request) {
    Optional<PollGroup> groupOptional = polls.getGroup(request.getGroupId());

    if (groupOptional.isEmpty()) {
      throw new WebApplicationException(notFound());
    }

    PollGroup group = groupOptional.get();

    Optional<Poll> pollOptional = polls.getPoll(request.getPollId());

    if (pollOptional.isEmpty()) {
      throw new WebApplicationException(notFound());
    }

    group.addPoll(pollOptional.get());

    return group;
  }

  @GET
  public PollGroup getPollGroup(@Auth PollUser user, @NotNull @QueryParam("id") UUID groupId) {
    Optional<PollGroup> groupOptional = polls.getGroup(groupId);

    if (groupOptional.isEmpty()) {
      throw new WebApplicationException(notFound());
    }
    return groupOptional.get();
  }

  @Path("/for-user")
  @GET
  public List<PollGroup> getGroupsForUser(@Auth PollUser user) {
    return polls.getGroupsOfUser(user.getId());
  }

  @Path("/polls")
  @GET
  public Collection<Poll> getPollsInGroup(@Auth PollUser user,
      @NotNull @QueryParam("id") UUID groupId) {
    Optional<PollGroup> groupOptional = polls.getGroup(groupId);

    if (groupOptional.isEmpty()) {
      throw new WebApplicationException(notFound());
    }

    return groupOptional.get().getAllPolls();
  }

  private static class PollGroupCreationRequest {

    @NotNull
    private final String name;

    @JsonCreator
    public PollGroupCreationRequest(@JsonProperty("name") String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  private static class PollAddRequest {

    @NotNull
    private final UUID groupId;
    @NotNull
    private final UUID pollId;

    public PollAddRequest(@NotNull UUID groupId, @NotNull UUID pollId) {
      this.groupId = groupId;
      this.pollId = pollId;
    }

    public UUID getGroupId() {
      return groupId;
    }

    public UUID getPollId() {
      return pollId;
    }
  }

}
