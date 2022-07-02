package pl.sda.arppl4.hibernate_wypozyczalnia;

import pl.sda.arppl4.hibernate_wypozyczalnia.dao.SamochodDao;
import pl.sda.arppl4.hibernate_wypozyczalnia.parser.WypozyczalniaParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        SamochodDao samochodDao = new SamochodDao();

        WypozyczalniaParser parser = new WypozyczalniaParser(scanner, samochodDao);
        parser.wykonaj();
    }
}
