package pl.sda.arppl4.hibernate_wypozyczalnia.parser;

import pl.sda.arppl4.hibernate_wypozyczalnia.dao.GenericDao;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.Samochod;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.TypNadwozia;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.Wypozyczalnia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WypozyczalniaParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Scanner scanner;
    private final GenericDao<Samochod> daoS;
    private final GenericDao<Wypozyczalnia> daoW;

    public WypozyczalniaParser(Scanner scanner, GenericDao<Samochod> daoS, GenericDao<Wypozyczalnia> daoW) {
        this.scanner = scanner;
        this.daoS = daoS;
        this.daoW = daoW;
    }

    public void wykonaj() {
        String komenda;
        do {
            System.out.println("Komenda: [dodaj, zwroc, lista, usun, update, wyjanem]");
            komenda = scanner.next();
            if (komenda.equalsIgnoreCase("dodaj")) {
                handleAddCommand();
            } else if (komenda.equalsIgnoreCase("znajdz")) {
                handleReturnCar();
            } else if (komenda.equalsIgnoreCase("lista")) {
                handleListCommand();
            } else if (komenda.equalsIgnoreCase("usun")) {
                handleDeleteCommand();
            } else if (komenda.equalsIgnoreCase("update")) {
                handleUpdateCommand();
            } else if (komenda.equalsIgnoreCase("wynajem")) {
                handleRentalCar();
            } else if (komenda.equalsIgnoreCase("zwrocWynajem")) {
                handleReturnRentedCar();
            } else if (komenda.equalsIgnoreCase("sprawdz")) {
                handleCheckCarCommand();
            }
        } while (!komenda.equals("wyjdz"));
    }


    private void handleRentalCar() {
        System.out.println("Podaj id samochodu");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = daoS.znajdzPoId(id, Samochod.class);
        if (samochodOptional.isPresent()) {
            Samochod samochod = samochodOptional.get();

            if (sprawdzCzySamochodJestDostepny(samochod)) {
                System.out.println("Podaj imie");
                String imie = scanner.next();

                System.out.println("Podaj nazwisko");
                String nazwisko = scanner.next();

                LocalDateTime dataCzasWynajmu = LocalDateTime.now();
                System.out.println("Data i czas wynajmu :" + dataCzasWynajmu);

                Wypozyczalnia wypozyczalnia = new Wypozyczalnia(imie, nazwisko, dataCzasWynajmu, samochod);
                daoW.dodaj(wypozyczalnia);

            } else {
                System.out.println("Samochod nie znaleziony");
            }
        }
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

        Samochod samochod = new Samochod(nazwa, model, dataWypozyczenia, iloscPasazerow, typNadwozia);
        daoS.dodaj(samochod);
    }

    private void handleListCommand() {
        List<Samochod> samochodLista = daoS.list(Samochod.class);
        for (Samochod samochod : samochodLista) {
            System.out.println(samochod);
        }
        System.out.println();
    }

    private void handleReturnCar() {
        System.out.println("Podaj id samochodu");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = daoS.znajdzPoId(id, Samochod.class);
        if (samochodOptional.isPresent()) {
            System.out.println("Samochod: " + samochodOptional.get());
        } else {
            System.out.println("Nie znaleziono samochodu");
        }
    }

    private void handleUpdateCommand() {
        System.out.println("Podaj id samochodu do aktualizacji:");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = daoS.znajdzPoId(id, Samochod.class);
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

            daoS.aktualizuj(samochod);
            System.out.println("Samochod zaktualizowano.");
        } else {

            System.out.println("Nie znaleziono samochodu");
        }
    }

    private void handleDeleteCommand() {
        System.out.println("Provide id of the product");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = daoS.znajdzPoId(id, Samochod.class);
        if (samochodOptional.isPresent()) {
            Samochod samochod = samochodOptional.get();
            daoS.usun(samochod);
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

    private void handleReturnRentedCar() {
        System.out.println("Podaj id samochodu");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = daoS.znajdzPoId(id, Samochod.class);
        if (samochodOptional.isPresent()) {
            Samochod samochod = samochodOptional.get();

            Optional<Wypozyczalnia> optionalWypozyczalnia = znajdzAKtywnyWynajem(samochod);
            if (optionalWypozyczalnia.isPresent()) {
                Wypozyczalnia wypozyczalnia = optionalWypozyczalnia.get();
                wypozyczalnia.setCzas_zwrotu(LocalDateTime.now());

                daoW.aktualizuj(wypozyczalnia);
            } else {
                System.out.println("Nie znaleziono samochodu");
            }
        }
    }

    private Optional<Wypozyczalnia> znajdzAKtywnyWynajem(Samochod samochod) {
        if (samochod.getWypozyczalnie().isEmpty()) {

            return Optional.empty();
        }
        for (Wypozyczalnia wypozyczalnia : samochod.getWypozyczalnie()) {
            if (wypozyczalnia.getCzas_zwrotu() == null) {
                return Optional.of(wypozyczalnia);
            }
        }
        return Optional.empty();
    }

    private void handleCheckCarCommand() {
        System.out.println("Podaj id samochodu");
        Long id = scanner.nextLong();

        Optional<Samochod> samochodOptional = daoS.znajdzPoId(id, Samochod.class);
        if (samochodOptional.isPresent()) {
            Samochod samochod = samochodOptional.get();
            if(sprawdzCzySamochodJestDostepny(samochod)){
                System.out.println("Samochod jest dostepy");
            }else{
                System.out.println("Samochod nie jest dostepny");
            }
        } else {
            System.out.println("Samochod nie znaleziony");
        }
    }

    private boolean sprawdzCzySamochodJestDostepny(Samochod samochod){
        Optional<Wypozyczalnia> optionalWypozyczalnia = znajdzAKtywnyWynajem(samochod);
        return !optionalWypozyczalnia.isPresent();
    }
}
