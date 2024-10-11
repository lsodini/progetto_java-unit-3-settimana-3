package LucaSodini.dao;

import LucaSodini.entities.ElementoCatalogo;
import LucaSodini.exceptions.ElementoNonTrovatoException;
import LucaSodini.exceptions.ElementoGiaEsistenteException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ElementoCatalogoDAO {
    private EntityManager entityManager;

    public ElementoCatalogoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addElemento(ElementoCatalogo elemento) {
        if (entityManager.find(ElementoCatalogo.class, elemento.getIsbn()) != null) {
            throw new ElementoGiaEsistenteException("Elemento con ISBN " + elemento.getIsbn() + " già esistente.");
        }
        entityManager.persist(elemento);
    }

    public void removeElemento(String isbn) {
        ElementoCatalogo elemento = entityManager.find(ElementoCatalogo.class, isbn);
        if (elemento == null) {
            throw new ElementoNonTrovatoException("Elemento con ISBN " + isbn + " non trovato.");
        }
        entityManager.remove(elemento);
    }

    public ElementoCatalogo getElementoByISBN(String isbn) {
        ElementoCatalogo elemento = entityManager.find(ElementoCatalogo.class, isbn);
        if (elemento == null) {
            throw new ElementoNonTrovatoException("Elemento con ISBN " + isbn + " non trovato.");
        }
        return elemento;
    }

    public List<ElementoCatalogo> getElementiByAnno(int annoPubblicazione) {
        TypedQuery<ElementoCatalogo> query = entityManager.createQuery(
                "SELECT e FROM ElementoCatalogo e WHERE e.annoPubblicazione = :anno", ElementoCatalogo.class);
        query.setParameter("anno", annoPubblicazione);
        return query.getResultList();
    }

    public List<ElementoCatalogo> getElementiByAutore(String autore) {
        TypedQuery<ElementoCatalogo> query = entityManager.createQuery(
                "SELECT l FROM Libro l WHERE l.autore = :autore", ElementoCatalogo.class);
        query.setParameter("autore", autore);
        return query.getResultList();
    }

    public List<ElementoCatalogo> getElementiByTitolo(String titolo) {
        TypedQuery<ElementoCatalogo> query = entityManager.createQuery(
                "SELECT e FROM ElementoCatalogo e WHERE e.titolo LIKE :titolo", ElementoCatalogo.class);
        query.setParameter("titolo", "%" + titolo + "%");
        return query.getResultList();
    }

    public void removeAllElementi() {
        entityManager.createQuery("DELETE FROM ElementoCatalogo").executeUpdate();
    }
}
