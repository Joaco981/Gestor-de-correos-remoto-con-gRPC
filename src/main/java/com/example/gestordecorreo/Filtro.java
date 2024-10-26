package com.example.gestordecorreo;
import java.util.function.Predicate;

public class Filtro {
    private String descripcion;
    private Predicate<Email> predicado;
    
    //descripcion del filtro y predicado que se aplica a cada email, para filtros complejos
    public Filtro(String descripcion, Predicate<Email> predicado) {
        this.descripcion = descripcion;
        this.predicado = predicado;
    }

    public Predicate<Email> getPredicado() {
        return predicado;
    }

    public String getDescripcion() {
        return descripcion;
    }

}

