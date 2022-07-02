package pl.sda.arppl4.hibernate_wypozyczalnia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Samochod {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String nazwa;
    private String model;
    private LocalDate dataWypozyczenia;
    private int iloscPasazerow;
    @Enumerated(EnumType.STRING)
    private TypNadwozia typNadwozia;
}