package LucaSodini.dao;

import LucaSodini.entities.Prestito;
import LucaSodini.exceptions.PrestitoNonTrovatoException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PrestitoDAO {
    private EntityManager entityManager;

    public PrestitoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addPrestito(Prestito prestito) {
        entityManager.persist(prestito);
    }

    public List<Prestito> getPrestitiByTessera(String numeroTessera) {
        TypedQuery<Prestito> query = entityManager.createQuery(
                "SELECT p FROM Prestito p WHERE p.utente.numeroTessera = :numeroTessera", Prestito.class);
        query.setParameter("numeroTessera", numeroTessera);
        List<Prestito> prestiti = query.getResultList();
        if (prestiti.isEmpty()) {
            throw new PrestitoNonTrovatoException("Nessun prestito trovato per il numero di tessera: " + numeroTessera);
        }
        return prestiti;
    }

    public List<Prestito> getPrestitiScaduti() {
        TypedQuery<Prestito> query = entityManager.createQuery(
                "SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < CURRENT_DATE AND p.dataRestituzioneEffettiva IS NULL", Prestito.class);
        return query.getResultList();
    }

    public void removeAllPrestiti() {
        entityManager.createQuery("DELETE FROM Prestito").executeUpdate();
    }
}
