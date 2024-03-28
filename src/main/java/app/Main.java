package app;

import app.entities.Client;
import app.entities.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        Session session = null;

        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            //формулируем запрос для выборки всех SingleRoom и тех SuiteRoom, которые находятся на третьем этаже
            String hql = "FROM Room r WHERE (TYPE(r) = SingleRoom) OR (TYPE(r) = SuiteRoom AND r.floor = 3)";
            List<Room> rooms = session.createQuery(hql, Room.class).getResultList();
            System.out.println(rooms);
            session.getTransaction().commit();
        } finally {
            factory.close();
            session.close();
        }
    }
}
