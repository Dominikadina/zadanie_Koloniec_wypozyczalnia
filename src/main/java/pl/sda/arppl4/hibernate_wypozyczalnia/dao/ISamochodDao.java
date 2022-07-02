package pl.sda.arppl4.hibernate_wypozyczalnia.dao;

import pl.sda.arppl4.hibernate_wypozyczalnia.model.Samochod;

import java.util.List;
import java.util.Optional;

public interface ISamochodDao {
    public void dodaj(Samochod samochod);

    public void update(Samochod samochod);

    public void usun(Samochod samochod);

    Optional <Samochod> zwroc(Long id);

    List <Samochod> zwrocListeSamochodow();


}
