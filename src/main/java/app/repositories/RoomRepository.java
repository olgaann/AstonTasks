package app.repositories;

import app.db.DataBase;
import app.entities.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepository {
    private DataBase dataBase;

    public RoomRepository(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<Room> findAll() {
        List<Room> roomList = new ArrayList<>();

        try {
            dataBase.connect();
            Statement statement = dataBase.getStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rooms");

            while (resultSet.next()) {
                Room room = new Room(resultSet.getLong("id"), resultSet.getInt("number"));
                roomList.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }

        return roomList;
    }

    public Optional<Room> add(int number) {
        try {
            dataBase.connect();
            String sql = "INSERT INTO rooms(number) VALUES (?) RETURNING id, number";

            PreparedStatement statement = dataBase.getPreparedStatement(sql);
            statement.setInt(1, number);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Room newRoom = new Room();
                newRoom.setId(resultSet.getLong("id"));
                newRoom.setNumber(resultSet.getInt("number"));
                return Optional.of(newRoom);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataBase.disconnect();
        }

        return Optional.empty();
    }
}
