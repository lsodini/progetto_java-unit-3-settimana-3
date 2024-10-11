package LucaSodini.dao;

import LucaSodini.entities.Utente;
import LucaSodini.exceptions.UtenteNonTrovatoException;

import jakarta.persistence.EntityManager;

public class UtenteDAO {
    private EntityManager entityManager;

    public UtenteDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addUtente(Utente utente) {
        entityManager.persist(utente);
    }

    public Utente getUtenteByTessera(String numeroTessera) {
        Utente utente = entityManager.find(Utente.class, numeroTessera);
        if (utente == null) {
            throw new UtenteNonTrovatoException("Utente con numero di tessera " + numeroTessera + " non trovato.");
        }
        return utente;
    }

    public void removeAllUtenti() {
        entityManager.createQuery("DELETE FROM Utente").executeUpdate();
    }
}
