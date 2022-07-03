package pl.sda.arppl4.hibernate_wypozyczalnia.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wypozyczalnia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String klient_imie;
    private String klient_nazwisko;
    private LocalDateTime czas_wynajmu;
    private LocalDateTime czas_zwrotu;

    //Relacje:
    //OneToMany
    //ManyToOne
    //ManyToMany
    //OneToOne


    public Wypozyczalnia(String klient_imie, String klient_nazwisko, LocalDateTime czas_wynajmu, Samochod samochod) {
        this.klient_imie = klient_imie;
        this.klient_nazwisko = klient_nazwisko;
        this.czas_wynajmu = czas_wynajmu;
        this.samochod = samochod;
    }

    @ManyToOne()
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Samochod samochod;

}
