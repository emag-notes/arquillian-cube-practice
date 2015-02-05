package arquilliancubepractice.jaxrspersistence.api;

import arquilliancubepractice.jaxrspersistence.model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserResourceTest {

  private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);
  private static final String PATH_PREFIX = UserResource.class.getAnnotation(Path.class).value().substring(1);
  private static final String BASE_PATH = RESOURCE_PREFIX + "/" + PATH_PREFIX;

  @Deployment
  public static WebArchive deployment() {
    return ShrinkWrap
              .create(WebArchive.class, "test.war")
              .addClasses(User.class, UserResource.class)
              .addPackages(true, "arquilliancubepractice.jaxrspersistence")
              .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
              .addAsWebInfResource("test-ds.xml", "arquilliancube-ds.xml")
              .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
              .addAsManifestResource(new StringAsset("Dependencies: com.h2database.h2\n"), "MANIFEST.MF");
  }

  @ArquillianResource
  private URL deploymentUrl;

  private static Client client;
  private static Response response;

  @BeforeClass
  public static void setUpClass() throws Exception {
    Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:1521/opt/h2-data/test", "sa", "sa");
    PreparedStatement ps = conn.prepareStatement("INSERT INTO user (id, name) VALUES (?, ?)");
    ps.setInt(1, 1);
    ps.setString(2, "user1");
    ps.executeUpdate();

    ps.setInt(1, 2);
    ps.setString(2, "user2");
    ps.executeUpdate();
  }

  @Before
  public void setUp() throws Exception {
    client = ClientBuilder.newClient();
  }

  @After
  public void tearDown() throws Exception {
    if (response != null) {
      response.close();
    }
    client.close();
  }

  @Test @RunAsClient
  public void test1() throws Exception {
    // Setup
    WebTarget target = client.target(deploymentUrl.toString() + BASE_PATH);
    // Exercise
    response = target.request(MediaType.APPLICATION_JSON).get();
    // Verify
    assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

    List<User> users =  response.readEntity(new GenericType<List<User>>(){});
    assertThat(users.size(), is(2));
    System.out.println(users);
  }

  @Test @RunAsClient
  public void test2() throws Exception {
    // Setup
    WebTarget target = client.target(deploymentUrl.toString() + BASE_PATH + "/1");
    // Exercise
    response = target.request(MediaType.APPLICATION_JSON).get();
    // Verify
    assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

    User user =  response.readEntity(User.class);
    System.out.println(user);
  }

}