package app;

import app.entities.Client;
import app.entities.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Session session = null;

        session = factory.getCurrentSession();
        session.beginTransaction();
        Client client = session.load(Client.class, 1);
        Room room = session.load(Room.class, 4);
        client.getRooms().add(room);
        System.out.println(client);
        System.out.println(room);
        System.out.println(client.getRooms().toString());
        session.getTransaction().commit();

    }
}
