package app.repositories;

import app.entities.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository {
    private SessionFactory sessionFactory;

    public ClientRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Client> getAllClients() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            List<Client> clients;

            transaction = session.beginTransaction();
            String hql = "FROM Client";
            Query<Client> query = session.createQuery(hql, Client.class);
            clients = query.list();

            transaction.commit();
            return clients;
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

    public Optional<Client> getById(long id) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            String hql = "FROM Client c JOIN FETCH c.rooms WHERE c.id = :id";
            Query<Client> query = session.createQuery(hql, Client.class);
            query.setParameter("id", id);
            Client client = query.getSingleResult();
            transaction.commit();

            return Optional.ofNullable(client);
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

    public List<Client> getByName(String name) {
        Session session = null;
        Transaction transaction = null;

        try {
            List<Client> clients;
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            String hql = "FROM Client c WHERE c.name = :name";
            Query<Client> query = session.createQuery(hql, Client.class);
            query.setParameter("name", name);
            clients = query.list();

            transaction.commit();

            return clients;
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

    public Optional<Client> add(String name, String phone) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Client newClient = new Client();
            newClient.setName(name);
            newClient.setPhone(phone);
            session.save(newClient);
            transaction.commit();

            return Optional.of(newClient);
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

    public Optional<Client> updateById(long id, String name, String phone) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Client client = session.get(Client.class, id);

            if (client != null) {
                client.setName(name);
                client.setPhone(phone);

                session.update(client);
                transaction.commit();

                return Optional.of(client);
            } else {
                return Optional.empty();
            }
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

    public Optional<Client> deleteById(long id) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Client client = session.get(Client.class, id);

            if (client != null) {
                session.delete(client);
                transaction.commit();
                return Optional.of(client);
            } else {
                return Optional.empty();
            }
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
