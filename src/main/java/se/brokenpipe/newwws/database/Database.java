package se.brokenpipe.newwws.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import se.brokenpipe.newwws.database.tables.Item;

import java.util.List;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-06-30
 */
public class Database {

    private final static Logger LOGGER = Logger.getLogger(Database.class.getName());
    private final DatabaseSettings settings;
    private static SessionFactory sessionFactory;

    public Database(final DatabaseSettings settings) {
        this.settings = settings;
    }

    public static void setup() throws DatabaseException {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when setting up database", ex);
        }
    }

    public static List<Item> getAllItems() throws DatabaseException {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Item> list = (List<Item>) session.createQuery("from Item").list();
            session.getTransaction().commit();
            session.close();
            return list;
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when getting all items from database", ex);
        }
    }

    public static void insertItem(final Item item) throws DatabaseException {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when inserting new item in database: " + item.toString(), ex);
        }
    }
}
