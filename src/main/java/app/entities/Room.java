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

    public Room(int number) {
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
