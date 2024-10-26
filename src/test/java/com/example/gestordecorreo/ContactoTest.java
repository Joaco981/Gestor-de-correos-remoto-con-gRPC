package com.example.gestordecorreo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
//test para cobertura de jacoco

public class ContactoTest {
    @Test
    void crear_contacto_test() {
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com"); 

        assert(persona1 != null);
        
    }

@Test
    public void test_GetNombre() {
        Contacto contacto = new Contacto("Taylor Swift", "ts13@gmail.com");
        assertEquals("Taylor Swift", contacto.getNombre());
    }
    

@Test
    public void test_SetNombre() {
        Contacto contacto = new Contacto("Candelaria", "candelaria9@gmail.com");
        contacto.setNombre("Carla Marturet");
        assertEquals("Carla Marturet", contacto.getNombre());
    }










}
