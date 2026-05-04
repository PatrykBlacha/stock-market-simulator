package pl.edu.agh.to.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankStock {

    @Id
    private String name; // Nazwa akcji jest unikalna, więc może być kluczem głównym

    private int quantity;
}
