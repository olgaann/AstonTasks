package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "single_rooms")
//@SequenceGenerator(name = "room_seq", sequenceName = "id_sequence_id_seq", allocationSize = 1)
@Data
@NoArgsConstructor
public class SingleRoom extends Room {

    @Column(name = "discount")
    private int discount;

    public SingleRoom(int number, int discount) {
        super(number);
        this.discount = discount;
    }

    @Override
    public String toString() {
        return this.getNumber() + " " + discount;
    }
}
