package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;


public class UserDaoHibernateImpl implements UserDao {

    Session session;

    public UserDaoHibernateImpl() {

    }

    private static final String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
            "        ID BIGINT NOT NULL AUTO_INCREMENT,\n" +
            "        name CHAR(45) NOT NULL,\n" +
            "        lastName CHAR(45) NOT NULL,\n" +
            "        age TINYINT NOT NULL,\n" +
            "        PRIMARY KEY (id))\n" +
            "        ENGINE = InnoDB\n" +
            "        DEFAULT CHARACTER SET = utf8;";

    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()){
            session.beginTransaction();
            session.createNativeQuery(sql, User.class).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Table was created");
        } catch (Exception e) {
            if (session.getTransaction() != null) {session.getTransaction().rollback();}
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()){
            session.beginTransaction();
            session.createNativeQuery("drop table if exists users;", User.class).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Table was dropped");
        } catch (Exception e) {
            if (session.getTransaction() != null) {session.getTransaction().rollback();}
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()){
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            System.out.println("User was added, name: " + name + ", age: " + age);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {session.getTransaction().rollback();}
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()){
            session.beginTransaction();
            session.delete(session.get(User.class, id));
            System.out.println("User was deleted, user's ID: " + id);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {session.getTransaction().rollback();}
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = getSessionFactory().openSession();
        List<User> users = (List<User>) session.createQuery("From User").list();
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery("delete from users").executeUpdate();
            System.out.println("Table was cleaned");
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {session.getTransaction().rollback();}
            e.printStackTrace();
        }
    }
}

