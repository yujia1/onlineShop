package onlineShop.dao;
/**
 * The Data Access Object (DAO) support in Spring is aimed at making it easy to work
 * with data access technologies like JDBC, Hibernate, JPA or JDO in a consistent way.
 */

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.entity.Authorities;
import onlineShop.entity.Customer;
import onlineShop.entity.User;
/**
 * The @Repository annotation is a marker for any class that fulfils
 * the role or stereotype of a repository
 * (also known as Data Access Object or DAO
 * */
@Repository
public class CustomerDao {

    @Autowired
    private SessionFactory sessionFactory; // 连接数据库

    public void addCustomer(Customer customer) {
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmailId(customer.getUser().getEmailId());
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(customer);
            session.getTransaction().commit(); // 保存authority, customer到数据库
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback(); // 回滚回之前异常
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Customer getCustomerByUserName(String userName) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            // 获取query，然后constrains
            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("emailId", userName)).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null)
            return user.getCustomer();
        return null;
    }
}

