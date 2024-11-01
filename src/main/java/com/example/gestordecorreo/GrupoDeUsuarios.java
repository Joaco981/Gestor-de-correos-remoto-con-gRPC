package com.example.gestordecorreo;

import java.util.ArrayList;

public class GrupoDeUsuarios {
    private String nombre;
    private ArrayList<Contacto> contactos;

    public GrupoDeUsuarios(String nombre) {
        this.nombre = nombre;
        this.contactos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    public void agregarAlGrupo(Contacto contacto) {
        if (!contactos.contains(contacto)) {
            contactos.add(contacto);
        }
    }
    
}
