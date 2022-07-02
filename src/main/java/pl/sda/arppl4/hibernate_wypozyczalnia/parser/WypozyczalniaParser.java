package pl.sda.arppl4.hibernate_wypozyczalnia.parser;

import pl.sda.arppl4.hibernate_wypozyczalnia.dao.GenericDao;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.Samochod;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.TypNadwozia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WypozyczalniaParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Scanner scanner;
    private final GenericDao<Samochod> dao;

    public WypozyczalniaParser(Scanner scanner, GenericDao<Samochod> dao) {
        this.scanner = scanner;
        this.dao = dao;
    }

    public void wykonaj() {
        String komenda;
        do {
            System.out.println("Komenda: [dodaj, zwroc, lista, usun, update]");
            komenda = scanner.next();
            if (komenda.equalsIgnoreCase("dodaj")) {
                handleAddCommand();
            } else if (komenda.equalsIgnoreCase("zwroc")) {
                handleReturnCar();
            } else if (komenda.equalsIgnoreCase("lista")) {
                handleListCommand();
            } else if (komenda.equalsIgnoreCase("usun")) {
                handleReturnCar();
            } else if (komenda.equalsIgnoreCase("update")) {
                handleUpdateCommand();
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
        dao.dodaj(samochod);
    }

    private void handleListCommand() {
        List<Samochod> samochodLista = dao.list(Samochod.class);
        for (Samochod samochod : samochodLista) {
            System.out.println(samochod);
        }
        System.out.println();
    }

    private void handleReturnCar() {
        System.out.println("Podaj id samochodu");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = dao.znajdzPoId(id, Samochod.class);
        if (samochodOptional.isPresent()) {
            System.out.println("Samochod: " + samochodOptional.get());
        } else {
            System.out.println("Nie znaleziono samochodu");
        }
    }

    private void handleUpdateCommand() {
        System.out.println("Podaj id samochodu do aktualizacji:");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = dao.zwroc(id);
        if (samochodOptional.isPresent()) {
        Samochod samochod = samochodOptional.get();

        System.out.println("Co chciałbys uakttualnic? [nazwa, data wypozyczenia");
        String output = scanner.next();
        switch (output) {
            case "nazwa":
                System.out.println("Podaj nazwe:");
                String nazwa = scanner.next();

                samochod.setNazwa(nazwa);
                break;
            case "dataWypozyczenia":
                LocalDate dataWypozyczenia = zaladujdateWypozczenia();

                samochod.setDataWypozyczenia(dataWypozyczenia);
                break;

            default:
                System.out.println("Field with this name is not handled.");
        }

        dao.aktualizuj(samochod);
        System.out.println("Samochod zaktualizowano.");
    } else {

        System.out.println("Nie znaleziono samochodu");
    }
}
    private void handleDeleteCommand() {
        System.out.println("Provide id of the product");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = dao.zwroc(id);
        if (samochodOptional.isPresent()) {
            Samochod samochod = samochodOptional.get();
            dao.usun(samochod);
            System.out.println("Samochod usuniety");
        } else {
            System.out.println("Samochod nie znaleziony");
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
