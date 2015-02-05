package arquilliancubepractice.jaxrspersistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Yoshimasa Tanabe
 */
@Entity
public class User {

  @Id
  @GeneratedValue
  private int id;

  private String name;

  protected User() {}

  public User(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "User{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      '}';
  }
}
