package app.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "rooms")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "room_seq", sequenceName = "id_sequence_id_seq", allocationSize = 1)
@Data
@NoArgsConstructor
public abstract class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private int number;

    public Room(int number, int floor) {
        this.number = number;
        this.floor = floor;
    }

    @Column(name = "floor") // Новое поле "этаж"
    private int floor;

    public Room(int number) {
        this.number = number;
        this.floor = extractFloorFromNumber(number);
    }

    private int extractFloorFromNumber(int roomNumber) {
        return Integer.parseInt(String.valueOf(String.valueOf(roomNumber).charAt(0)));
    }

}
