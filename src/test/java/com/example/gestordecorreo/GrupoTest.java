package com.example.gestordecorreo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class GrupoTest {
    
    @Test
    void crear_un_grupo_test() {
        Contacto contacto1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto contacto2 = new Contacto("Candela Cano", "candelaria@gmail.com");
        Contacto contacto3 = new Contacto("Carla Marturet", "carla@gmail.com");

        
        GrupoDeUsuarios grupo = new GrupoDeUsuarios("El Club");
        grupo.agregarAlGrupo(contacto1);
        grupo.agregarAlGrupo(contacto2);
        grupo.agregarAlGrupo(contacto3);

        assertEquals(3, grupo.getContactos().size());
    }

    
}
