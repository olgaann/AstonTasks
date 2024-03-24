package app.services;

import app.entities.Room;
import app.repositories.RoomRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomService {
    RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> add(int number) throws SQLException {
        return roomRepository.add(number);
    }

    public String roomListToPrintView(List<Room> roomList) {
        return roomList.stream()
                .map(room -> String.format("%s %s", room.getId(), room.getNumber()))
                .collect(Collectors.joining("\n"));

    }
}
