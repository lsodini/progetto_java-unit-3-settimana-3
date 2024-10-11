package LucaSodini.entities;

import jakarta.persistence.*;

@Entity
public class Rivista extends ElementoCatalogo {
    private String periodicità;

    public Rivista(String isbn, String titolo, int annoPubblicazione, int numeroPagine, String periodicità) {
        super(isbn, titolo, annoPubblicazione, numeroPagine); // Chiama il costruttore della superclasse
        this.periodicità = periodicità;
    }

    public Rivista() {}

    public String getPeriodicità() {
        return periodicità;
    }

    public void setPeriodicità(String periodicità) {
        this.periodicità = periodicità;
    }

    @Override
    public String toString() {
        return "Rivista{" +
                "periodicità='" + periodicità + '\'' +
                "} " + super.toString();
    }
}
