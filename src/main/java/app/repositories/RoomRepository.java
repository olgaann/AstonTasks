package app.repositories;

import app.entities.Room;
import app.entities.SingleRoom;
import app.entities.SuiteRoom;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepository {
    private SessionFactory sessionFactory;

    public RoomRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Room> findAll() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            List<Room> roomList;

            transaction = session.beginTransaction();
            String hql = "FROM Room";
            Query<Room> query = session.createQuery(hql, Room.class);
            roomList = query.list();

            transaction.commit();
            return roomList;
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

    public Optional<Room> add(int number) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Room newRoom = new SuiteRoom(number, "KING");
            //Room newRoom = new SingleRoom(number, 15);
            newRoom.setNumber(number);
            session.save(newRoom);
            transaction.commit();

            return Optional.of(newRoom);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return Optional.empty();
        } finally {
            session.close();
        }
    }
}
