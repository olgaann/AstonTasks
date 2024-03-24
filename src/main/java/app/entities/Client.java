package app.entities;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private long id;
    private String name;
    private String phone;
    private List<Room> rooms;


    public Client(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.rooms = new ArrayList<>();
    }

    public Client() {
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Client(long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.rooms = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
