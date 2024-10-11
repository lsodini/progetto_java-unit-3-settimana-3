package LucaSodini;

import LucaSodini.dao.ElementoCatalogoDAO;
import LucaSodini.dao.PrestitoDAO;
import LucaSodini.dao.UtenteDAO;
import LucaSodini.entities.ElementoCatalogo;
import LucaSodini.entities.Libro;
import LucaSodini.entities.Prestito;
import LucaSodini.entities.Rivista;
import LucaSodini.entities.Utente;
import LucaSodini.exceptions.ElementoNonTrovatoException;
import LucaSodini.exceptions.PrestitoNonTrovatoException;
import LucaSodini.exceptions.UtenteNonTrovatoException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("catalogo_bibliografico");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            UtenteDAO utenteDAO = new UtenteDAO(em);
            ElementoCatalogoDAO elementoCatalogoDAO = new ElementoCatalogoDAO(em);
            PrestitoDAO prestitoDAO = new PrestitoDAO(em);

            // Rimuovi tutti gli elementi, prestiti e utenti (opzionale per inizializzare)
            elementoCatalogoDAO.removeAllElementi();
            prestitoDAO.removeAllPrestiti();
            utenteDAO.removeAllUtenti();
            em.getTransaction().commit();

            // Aggiunta di un elemento del catalogo
            em.getTransaction().begin();
            Libro nuovoLibro = new Libro("978-1234567890", "Il Grande Libro", 2023, 300, "Autore Uno", "Romanzo");
            elementoCatalogoDAO.addElemento(nuovoLibro);
            Libro nuovoLibro1 = new Libro("1000034567890", "harry potter e la camera dei segreti", 2013, 600, "J.K. Rowling", "Fantasy");
            elementoCatalogoDAO.addElemento(nuovoLibro1);
            Rivista nuovaRivista = new Rivista("987-1234567890", "Rivista Settimanale", 2023, 100, "SETTIMANALE");
            elementoCatalogoDAO.addElemento(nuovaRivista);
            em.getTransaction().commit();
            System.out.println("Elementi aggiunti al catalogo.");


            // Ricerca per ISBN
            em.getTransaction().begin();
            ElementoCatalogo libroRicercato = elementoCatalogoDAO.getElementoByISBN("978-1234567890");
            System.out.println("Ricerca per ISBN: " + (libroRicercato != null ? libroRicercato : "Libro non trovato."));
            em.getTransaction().commit();

            // Ricerca per anno di pubblicazione
            em.getTransaction().begin();
            List<ElementoCatalogo> libriPerAnno = elementoCatalogoDAO.getElementiByAnno(2013);
            System.out.println("Libri pubblicati nel 2023: " + libriPerAnno);
            em.getTransaction().commit();

            // Ricerca per autore
            em.getTransaction().begin();
            List<ElementoCatalogo> libriPerAutore = elementoCatalogoDAO.getElementiByAutore("Autore Uno");
            System.out.println("Libri di Autore Uno: " + libriPerAutore);
            em.getTransaction().commit();

            // Ricerca per titolo o parte di esso
            em.getTransaction().begin();
            List<ElementoCatalogo> libriPerTitolo = elementoCatalogoDAO.getElementiByTitolo("Grande");
            System.out.println("Libri con 'Grande' nel titolo: " + libriPerTitolo);
            em.getTransaction().commit();

            // Ricerca degli elementi attualmente in prestito dato un numero di tessera utente
            em.getTransaction().begin();
            Utente nuovoUtente = new Utente("12345", "Mario", "Rossi", LocalDate.of(1985, 5, 20));
            utenteDAO.addUtente(nuovoUtente); // Assicurati che l'utente sia persistente
            Prestito nuovoPrestito = new Prestito();
            nuovoPrestito.setUtente(nuovoUtente);
            nuovoPrestito.setElemento(nuovaRivista);
            nuovoPrestito.setDataInizioPrestito(LocalDate.of(2024,9,9));
            nuovoPrestito.setDataRestituzionePrevista(LocalDate.of(2024,9,9).plusDays(30));
            prestitoDAO.addPrestito(nuovoPrestito);
            List<Prestito> prestitiAttivi = prestitoDAO.getPrestitiByTessera("12345");
            System.out.println("Prestiti attivi per utente 12345: " + prestitiAttivi);
            em.getTransaction().commit();

            // Ricerca di tutti i prestiti scaduti e non ancora restituiti
            em.getTransaction().begin();
            List<Prestito> prestitiScaduti = prestitoDAO.getPrestitiScaduti();
            System.out.println("Prestiti scaduti e non restituiti: " + prestitiScaduti);
            em.getTransaction().commit();

        } catch (ElementoNonTrovatoException | PrestitoNonTrovatoException | UtenteNonTrovatoException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println(e.getMessage());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
