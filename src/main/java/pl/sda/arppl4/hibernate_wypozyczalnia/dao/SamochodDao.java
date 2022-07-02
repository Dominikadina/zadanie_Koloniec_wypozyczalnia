package pl.sda.arppl4.hibernate_wypozyczalnia.dao;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pl.sda.arppl4.hibernate_wypozyczalnia.model.Samochod;
import pl.sda.arppl4.hibernate_wypozyczalnia.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SamochodDao implements ISamochodDao {
    public void dodajSamochod(Samochod samochod) {
        SessionFactory fabrykaPolaczen = HibernateUtil.getSessionFactory();

        Transaction transaction = null;
        try (Session session = fabrykaPolaczen.openSession()) {
            transaction = session.beginTransaction();

            session.merge(samochod);

            transaction.commit();
        } catch (SessionException sessionException) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void usunSamochod(Samochod samochod) {
        SessionFactory fabrykaPolaczen = HibernateUtil.getSessionFactory();
        try (Session session = fabrykaPolaczen.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.remove(samochod);

            transaction.commit();
        }
    }

    public Optional<Samochod> zwrocSamochod(Long id) {
        SessionFactory fabrykaPolaczen = HibernateUtil.getSessionFactory();
        try (Session session = fabrykaPolaczen.openSession()) {
            Samochod obiektSamochod = session.get(Samochod.class, id);

            return Optional.ofNullable(obiektSamochod);
        }
    }

    public List<Samochod> zwrocListeSamochodow() {
        List<Samochod> SamochodLista = new ArrayList<>();
        SessionFactory fabrykaPolaczen = HibernateUtil.getSessionFactory();
        try (Session session = fabrykaPolaczen.openSession()) {

            TypedQuery<Samochod> zapytanie = session.createQuery( "from Samochod", Samochod.class);
            List<Samochod> wynikZapytania = zapytanie.getResultList();

          SamochodLista.addAll(wynikZapytania);
        } catch (SessionException sessionException) {
            System.err.println("Błąd wczytywania danych.");
        }

        return SamochodLista;
    }
        }
