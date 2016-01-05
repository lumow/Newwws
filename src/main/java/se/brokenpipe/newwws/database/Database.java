package se.brokenpipe.newwws.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import se.brokenpipe.newwws.database.tables.Channel;
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

    public static void insertItem(final Item item, final Channel channel) throws DatabaseException {
        try {
            item.setChannelId(channel);
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when inserting new item in database: " + item.toString(), ex);
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

    public static void insertItemWithCheck(final Item item) throws DatabaseException {
        try {
            Session session = sessionFactory.openSession();

            if (!entityExists(session, item.getClass(), "title", item.getTitle())) {
                session.beginTransaction();
                session.save(item);
                session.getTransaction().commit();
                session.close();
            }
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when inserting new item in database: " + item.toString(), ex);
        }
    }

    private static boolean entityExists(Session session, Class clazz, String identifierKey, Object identifierValue) throws HibernateException {
        return session.createCriteria(clazz)
                .add(Restrictions.eq(identifierKey, identifierValue))
                .setProjection(Projections.property(identifierKey))
                .uniqueResult() != null;
    }

    public static void insertChannel(final Channel channel) throws DatabaseException {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(channel);
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when inserting new channel in database: " + channel.toString(), ex);
        }
    }

    public static void insertChannelWithCheck(final Channel channel) throws DatabaseException {
        try {
            Session session = sessionFactory.openSession();

            if (!entityExists(session, channel.getClass(), "link", channel.getLink())) {
                session.beginTransaction();
                session.save(channel);
                session.getTransaction().commit();
                session.close();
            }
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when inserting new channel in database: " + channel.toString(), ex);
        }
    }

    public static void clearItemsWithChannel(final Channel channel) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("delete from Item where channel_id = " + channel.getId());
        session.getTransaction().commit();
        session.close();
    }

    public static boolean channelExists(final Channel channel) {
        Session session = sessionFactory.openSession();
        return entityExists(session, channel.getClass(), "link", channel.getLink());
    }

    public static List<Channel> getAllChannels() throws DatabaseException {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Channel> list = (List<Channel>) session.createQuery("from Channel").list();
            session.getTransaction().commit();
            session.close();
            return list;
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when getting all items from database", ex);
        }
    }

    public static Channel getChannel(final Channel channel) throws DatabaseException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Channel> list = (List<Channel>) session.createQuery("from Channel where link = " + channel.getLink()).list();
        session.getTransaction().commit();
        session.close();
        if (list.size() != 1) {
            throw new DatabaseException("Got " + list.size() + " channels from query instead of 1.");
        }
        return list.get(0);
    }

    public static List<Item> getItemsForChannel(Channel channel) throws DatabaseException {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Item> list = (List<Item>) session.createQuery("from Item where channel_id = " + channel.getId()).list();
            session.getTransaction().commit();
            session.close();
            return list;
        } catch (HibernateException ex) {
            throw new DatabaseException("Error when getting all items from database", ex);
        }
    }
}
