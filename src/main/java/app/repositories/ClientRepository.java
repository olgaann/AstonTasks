package app.repositories;

import app.db.DataBase;
import app.entities.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private DataBase dataBase;

    public ClientRepository(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();

        try {
            dataBase.connect();
            Statement statement = dataBase.getStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");

            while (resultSet.next()) {
                Client client = new Client(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("phone"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }

        return clients;
    }


    public Client getById(long id) {
        Client client = null;
        try {
            dataBase.connect();
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT * FROM clients WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                client = new Client(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }

        return client;
    }

    public Client getByName(String name) {
        Client client = null;
        try {
            dataBase.connect();
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT * FROM clients WHERE name = ?"
            );
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                client = new Client(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }

        return client;
    }

    public List<Integer> getRoomsNumbersByClientId(long id) {
        List<Integer> numbers = new ArrayList<>();
        try {
            dataBase.connect();
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT rooms.number FROM rooms JOIN bookings ON rooms.id = bookings.room_id WHERE bookings.client_id = ?;"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int number = resultSet.getInt("number");
                numbers.add(number);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        System.out.println(numbers);
        return numbers;
    }

    public Client add(String name, String phone) throws SQLException {
        Client newClient = new Client(name, phone);
        String sql = "INSERT INTO clients (name, phone) VALUES (?, ?);";

        try {
            dataBase.connect();
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    newClient.setId(generatedKeys.getLong(1));
                }
            }
            return newClient;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return null;
    }

    public Client updateById(long id, String name, String phone) {
        Client updatedClient = new Client();
        try {
            dataBase.connect();
            StringBuilder sqlBuilder = new StringBuilder("UPDATE clients SET ");
            List<String> parameters = new ArrayList<>();
            if (name != null) {
                parameters.add("name = ?");
            }
            if (phone != null) {
                parameters.add("phone = ?");
            }
            sqlBuilder.append(String.join(", ", parameters));
            sqlBuilder.append(" WHERE id = ?");
            String sql = sqlBuilder.toString();
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            int parameterIndex = 1;
            if (name != null) {
                preparedStatement.setString(parameterIndex++, name);
            }
            if (phone != null) {
                preparedStatement.setString(parameterIndex++, phone);
            }
            preparedStatement.setLong(parameterIndex, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                PreparedStatement selectStatement = dataBase.getPreparedStatement("SELECT * FROM clients WHERE id = ?");
                selectStatement.setLong(1, id);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    updatedClient.setId(resultSet.getLong("id"));
                    updatedClient.setName(resultSet.getString("name"));
                    updatedClient.setPhone(resultSet.getString("phone"));
                }
                return updatedClient;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return null;
    }

    public Client deleteById( long id) {
        Client deletedClient = null;
        String selectSql = "SELECT id, name, phone FROM clients WHERE id = ?";
        String deleteSql = "DELETE FROM clients WHERE id = ?";

        try {
            dataBase.connect();
            PreparedStatement selectStatement = dataBase.getPreparedStatement(selectSql);
            selectStatement.setLong(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                deletedClient = new Client();
                deletedClient.setId(resultSet.getLong("id"));
                deletedClient.setName(resultSet.getString("name"));
                deletedClient.setPhone(resultSet.getString("phone"));
            }

            PreparedStatement deleteStatement = dataBase.getPreparedStatement(deleteSql);
            deleteStatement.setLong(1, id);
            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted == 0) {
                deletedClient = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return deletedClient;
    }
}
