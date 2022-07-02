package pl.sda.arppl4.hibernate_wypozyczalnia.parser;

import pl.sda.arppl4.hibernate_wypozyczalnia.dao.SamochodDao;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.Samochod;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.TypNadwozia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WypozyczalniaParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public WypozyczalniaParser(Scanner scanner, SamochodDao dao) {
        this.scanner = scanner;
        this.dao = dao;
    }

    private final Scanner scanner;
    private final SamochodDao dao;

    public void wykonaj() {
        String komenda;
        do {
            System.out.println("Komenda");
            komenda = scanner.next();
            if (komenda.equalsIgnoreCase("dodaj")) {
                handleAddCommand();
            } else if (komenda.equalsIgnoreCase("zwroc")) {
                handleReturnCar();
            } else if (komenda.equalsIgnoreCase("lista")) {
                handleListCommand();
            }

        } while (!komenda.equals("wyjdz"));
    }

    private void handleAddCommand() {
        System.out.println("Podaj nazwe samochodu");
        String nazwa = scanner.next();

        System.out.println("Podaj model");
        String model = scanner.next();

        LocalDate dataWypozyczenia = zaladujdateWypozczenia();

        System.out.println("Podaj ilosc pasazerow:");
        Integer iloscPasazerow = scanner.nextInt();

        TypNadwozia typNadwozia = zaladujTypNadwozia();

        Samochod samochod = new Samochod(null, nazwa, model, dataWypozyczenia, iloscPasazerow, typNadwozia);
        dao.dodajSamochod(samochod);
    }

    private void handleListCommand() {
        List<Samochod> SamochodLista = dao.zwrocListeSamochodow();
        for (Samochod samochod : SamochodLista) {
            System.out.println(samochod);
        }
        System.out.println();
    }
    private void handleReturnCar(){
        System.out.println("Podaj id samochodu");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = dao.zwrocSamochod(id);
        if(samochodOptional.isPresent()) {
            Samochod samochod = samochodOptional.get();
            dao.usunSamochod(samochod);
            System.out.println("Samochod usuniety");
        }else{
            System.out.println("Nie znaleziono samochodu");
        }
    }
    private LocalDate zaladujdateWypozczenia() {
        LocalDate dataWypozyczenia = null;
        do {
            try {
                System.out.println("Podaj edate wypozyczenia");
                String wprowadzonaDataWypozyczenia = scanner.next();

                dataWypozyczenia = LocalDate.parse(wprowadzonaDataWypozyczenia, FORMATTER);

                LocalDate today = LocalDate.now();
                if (dataWypozyczenia.isBefore(today)) {
                    throw new IllegalArgumentException(("Podano wczesniejsza date niz dzisiaj"));
                }

            } catch (IllegalArgumentException iae) {
                dataWypozyczenia = null;
                System.err.println("Wrong date, please provide date in format: yyy-MM-dd");
            }
        } while (dataWypozyczenia == null);
        return dataWypozyczenia;
    }

    private TypNadwozia zaladujTypNadwozia() {
        TypNadwozia typNadwozia = null;
        do {
            try {
                System.out.println("Podaj typ nadwozia");
                String unitString = scanner.next();

                typNadwozia = TypNadwozia.valueOf(unitString.toUpperCase());
            } catch (IllegalArgumentException iae) {
                System.err.println("Nie ma takiego podwozia");
            }
        } while (typNadwozia == null);
        return typNadwozia;
    }
}
