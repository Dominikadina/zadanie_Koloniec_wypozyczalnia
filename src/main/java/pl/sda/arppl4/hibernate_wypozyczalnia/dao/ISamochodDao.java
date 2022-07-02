package pl.sda.arppl4.hibernate_wypozyczalnia.dao;

import pl.sda.arppl4.hibernate_wypozyczalnia.model.Samochod;

import java.util.List;
import java.util.Optional;

public interface ISamochodDao {
    public void dodajSamochod(Samochod samochod);

//    public void usunSamochod(Samochod samochod);

    Optional <Samochod> zwrocSamochod(Long id);

    List <Samochod> zwrocListeSamochodow();


}
