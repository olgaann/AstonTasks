package app.entities;

public class Room {
    private long id;
    private int number;

    public Room(int number) {
        this.number = number;
    }

    public Room(long id, int number) {
        this.id = id;
        this.number = number;
    }

    public Room() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number=" + number +
                '}';
    }
}
