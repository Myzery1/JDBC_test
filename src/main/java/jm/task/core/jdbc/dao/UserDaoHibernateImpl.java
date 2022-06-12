package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;


public class UserDaoHibernateImpl implements UserDao {

    Transaction transaction;

    public UserDaoHibernateImpl() {

    }

    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                    "        ID BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "        name CHAR(45) NOT NULL,\n" +
                    "        lastName CHAR(45) NOT NULL,\n" +
                    "        age TINYINT NOT NULL,\n" +
                    "        PRIMARY KEY (id))\n" +
                    "        ENGINE = InnoDB\n" +
                    "        DEFAULT CHARACTER SET = utf8;";

            session.createNativeQuery(sql, User.class).executeUpdate();

            transaction.commit();
            System.out.println("Table was created");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String sql = "drop table if exists users;";

            session.createNativeQuery(sql, User.class).executeUpdate();

            transaction.commit();
            System.out.println("Table was dropped");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));

            System.out.println("User was added, name: " + name + ", age: " + age);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {

        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(User.class, id));

            System.out.println("User was deleted, user's ID: " + id);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {

        Session session = getSessionFactory().openSession();
        transaction = session.beginTransaction();

        List<User> users = (List<User>) session.createQuery("From User").list();

        transaction.commit();
        session.close();

        return users;
    }

    @Override
    public void cleanUsersTable() {

        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createNativeQuery("delete from users").executeUpdate();

            System.out.println("Table was cleaned");
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

