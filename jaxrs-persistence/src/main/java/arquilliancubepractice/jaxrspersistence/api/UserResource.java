package arquilliancubepractice.jaxrspersistence.api;

import arquilliancubepractice.jaxrspersistence.model.User;
import arquilliancubepractice.jaxrspersistence.repository.UserRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Yoshimasa Tanabe
 */
@Path("/users")
public class UserResource {

  @Inject
  private UserRepository repository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> get() {
    List<User> users = repository.findAll();
    return users;
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public User get(@PathParam("id") int id) {
    return repository.find(id);
  }

}
