package pl.sda.arppl4.hibernate_wypozyczalnia.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Samochod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;
    private String model;
    private LocalDate dataWypozyczenia;
    private int iloscPasazerow;
    @Enumerated(EnumType.STRING)
    private TypNadwozia typNadwozia;


    @OneToMany(mappedBy = "samochod", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Wypozyczalnia> wypozyczalnie;

    public Samochod(String nazwa, String model, LocalDate dataWypozyczenia, int iloscPasazerow, TypNadwozia typNadwozia) {
        this.nazwa = nazwa;
        this.model = model;
        this.dataWypozyczenia = dataWypozyczenia;
        this.iloscPasazerow = iloscPasazerow;
        this.typNadwozia = typNadwozia;
    }
}