package pl.sda.arppl4.hibernate_wypozyczalnia;

import pl.sda.arppl4.hibernate_wypozyczalnia.dao.GenericDao;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.Samochod;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.Wypozyczalnia;
import pl.sda.arppl4.hibernate_wypozyczalnia.parser.WypozyczalniaParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        GenericDao <Samochod> samochodGenericDao = new GenericDao<>();
        GenericDao <Wypozyczalnia> wypozyczalniaGenericDao = new GenericDao<>();

        WypozyczalniaParser parser = new WypozyczalniaParser(scanner, samochodGenericDao, wypozyczalniaGenericDao);
        parser.wykonaj();
    }
}
