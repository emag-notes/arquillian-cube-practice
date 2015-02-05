package arquilliancubepractice.jaxrspersistence.repository;

import arquilliancubepractice.jaxrspersistence.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Yoshimasa Tanabe
 */
@Stateless
public class UserRepository {

  @PersistenceContext
  private EntityManager em;

  public List<User> findAll() {
    TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
    return query.getResultList();
  }

  public User find(int id) {
    return em.find(User.class, id);
  }

}
