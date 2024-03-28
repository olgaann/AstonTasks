package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suite_rooms")
@Data
@NoArgsConstructor
public class SuiteRoom extends Room {

    @Column(name = "bed_size")
    private String bedSize;

    public SuiteRoom(int number, String bedSize) {
        super(number);
        this.bedSize = bedSize;
    }

    @Override
    public String toString() {
        return "№" + this.getNumber() + " этаж "+ this.getFloor() + " " + bedSize;
    }
}