package app.repositories;

import app.db.DataBase;
import app.entities.Booking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingRepository {
    private DataBase dataBase;
    private StringBuilder stringBuilder = new StringBuilder();

    public BookingRepository(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<Booking> findAll() {
        List<Booking> bookingList = new ArrayList<>();
        String sql = "SELECT c.id, c.name, r.number \n" +
                "FROM bookings b\n" +
                "JOIN clients c ON b.client_id = c.id \n" +
                "JOIN rooms r ON b.room_id = r.id;";
        try {
            dataBase.connect();
            Statement statement = dataBase.getStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setClientId(resultSet.getLong("id"));
                booking.setClientName(resultSet.getString("name"));
                booking.setNumber(resultSet.getInt("number"));
                bookingList.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return bookingList;
    }

    public List<Booking> deleteAllByClientId(long clientId) {
        List<Booking> bookingList = new ArrayList<>();
        String selectSql = "SELECT c.id, c.name, r.number \n" +
                "FROM bookings b\n" +
                "JOIN clients c ON b.client_id = c.id \n" +
                "JOIN rooms r ON b.room_id = r.id\n" +
                "WHERE c.id = ?;";
        String deleteSql = "DELETE FROM bookings WHERE client_id = ?";
        try {
            dataBase.connect();
            PreparedStatement selectStatement = dataBase.getPreparedStatement(selectSql);
            selectStatement.setLong(1, clientId);
            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setClientId(resultSet.getLong("id"));
                booking.setClientName(resultSet.getString("name"));
                booking.setNumber(resultSet.getInt("number"));
                bookingList.add(booking);
            }
            PreparedStatement deleteStatement = dataBase.getPreparedStatement(deleteSql);
            deleteStatement.setLong(1, clientId);
            int rowsDeleted = deleteStatement.executeUpdate();
            if (rowsDeleted == bookingList.size()) {
                return bookingList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return new ArrayList<>();
    }

    public List<Booking> deleteAllByClientIdAndNumber(long clientId, int number) {
        List<Booking> bookingList = new ArrayList<>();
        String selectSql = "SELECT c.id, r.id AS room_id, c.name, r.number \n" +
                "FROM bookings b\n" +
                "JOIN clients c ON b.client_id = c.id \n" +
                "JOIN rooms r ON b.room_id = r.id\n" +
                "WHERE c.id = ? AND r.number = ?;";

        String deleteSql = "DELETE FROM bookings WHERE client_id = ? AND room_id = ?;";
        try {
            dataBase.connect();
            PreparedStatement selectStatement = dataBase.getPreparedStatement(selectSql);
            selectStatement.setLong(1, clientId);
            selectStatement.setLong(2, number);
            ResultSet resultSet = selectStatement.executeQuery();
            long roomId = 0;
            while (resultSet.next()) {
                roomId = resultSet.getLong("room_id");
                Booking booking = new Booking();
                booking.setClientId(resultSet.getLong("id"));
                booking.setClientName(resultSet.getString("name"));
                booking.setNumber(resultSet.getInt("number"));
                bookingList.add(booking);
            }
            PreparedStatement deleteStatement = dataBase.getPreparedStatement(deleteSql);
            deleteStatement.setLong(1, clientId);
            deleteStatement.setLong(2, roomId);
            int rowsDeleted = deleteStatement.executeUpdate();
            if (rowsDeleted == bookingList.size()) {
                return bookingList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return new ArrayList<>();
    }

    public Optional<Booking> add(long clientId, long roomId) {
        try {
            dataBase.connect();
            // Получаем имя клиента
            Optional<String> clientName = getClientNameById(clientId);
            if (clientName.isEmpty()) {
                return Optional.empty();
            }
            //получаем номер комнаты
            Optional<Integer> number = getRoomNumber(roomId);
            if (number.isEmpty()) {
                return Optional.empty();
            }
            // Создаем бронирование
            String sql = "INSERT INTO bookings (client_id, room_id) VALUES (?, ?) RETURNING client_id, room_id";
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setLong(1, clientId);
            preparedStatement.setLong(2, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Booking newBooking = new Booking(resultSet.getLong("client_id"), clientName.get(), number.get());
                return Optional.of(newBooking);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataBase.disconnect();
        }
        return Optional.empty();
    }


    public Optional<String> getClientNameById(long clientId) throws SQLException {
        String sql = "SELECT name FROM clients WHERE id = ?";
        try {
            dataBase.connect();
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return Optional.empty();
    }

    private Optional<Integer> getRoomNumber(long roomId) throws SQLException {
        String sql = "SELECT number FROM rooms WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            dataBase.connect();
            preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setLong(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int roomNumber = resultSet.getInt("number");
                return Optional.of(roomNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.connect();
        }
        return Optional.empty();
    }
}
