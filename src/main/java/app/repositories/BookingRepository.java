package app.repositories;

import app.db.DataBase;
import app.entities.Booking;
import app.entities.Client;
import app.entities.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingRepository {
    private DataBase dataBase;
    private SessionFactory sessionFactory;

    public BookingRepository(DataBase dataBase, SessionFactory sessionFactory) {
        this.dataBase = dataBase;
        this.sessionFactory = sessionFactory;
    }

    public List<Booking> findAll() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            List<Booking> bookings;

            transaction = session.beginTransaction();
            String hql = "FROM Booking b JOIN FETCH b.client JOIN FETCH b.room";
            Query<Booking> query = session.createQuery(hql, Booking.class);
            bookings = query.list();

            transaction.commit();
            return bookings;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }


    public List<Booking> deleteAllByClientId(long clientId) {

        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            String hql = "FROM Booking b JOIN FETCH b.client JOIN FETCH b.room WHERE b.client.id = :clientId";
            List<Booking> bookingsToDelete = session.createQuery(hql, Booking.class)
                    .setParameter("clientId", clientId)
                    .list();

            List<Booking> deletedBookings = new ArrayList<>();
            for (Booking booking : bookingsToDelete) {
                session.delete(booking);
                deletedBookings.add(booking);
            }

            transaction.commit();
            return deletedBookings;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return new ArrayList<>();
        } finally {
            session.close();
        }


    }

    public List<Booking> deleteAllByClientIdAndNumber(long clientId, int number) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            String hql = "FROM Booking b JOIN FETCH b.client JOIN FETCH b.room WHERE b.client.id = :clientId and b.room.number = :number";
            List<Booking> bookingsToDelete = session.createQuery(hql, Booking.class)
                    .setParameter("clientId", clientId)
                    .setParameter("number", number)
                    .list();

            List<Booking> deletedBookings = new ArrayList<>();
            for (Booking booking : bookingsToDelete) {
                session.delete(booking);
                deletedBookings.add(booking);
            }

            transaction.commit();
            return deletedBookings;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }

    public Optional<Booking> add(long clientId, long roomId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Client client = session.get(Client.class, clientId);
            Room room = session.get(Room.class, roomId);

            if (client != null && room != null) {
                Booking newBooking = new Booking();
                newBooking.setClient(client);
                newBooking.setRoom(room);

                session.save(newBooking);
                transaction.commit();

                return Optional.of(newBooking);
            } else {
                transaction.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return Optional.empty();
    }

}
