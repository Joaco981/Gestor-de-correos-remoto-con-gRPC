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

    @Test
    void crear_grupo_y_mandar_email_al_grupo() {
        EmailManager em1 = new EmailManager();
        Contacto contacto1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto contacto2 = new Contacto("Candela Cano", "candelaria@gmail.com");
        Contacto contacto3 = new Contacto("Carla Marturet", "carla@gmail.com");
        
        GrupoDeUsuarios g1 = new GrupoDeUsuarios("El Club");
        g1.agregarAlGrupo(contacto1);
        g1.agregarAlGrupo(contacto2);
        g1.agregarAlGrupo(contacto3);

        Email email = new Email();
        email.setAsunto("Universidad");
        email.setContenido("Hoy no hay clase vamoooo");
        email.setRemitente(contacto1);

        em1.enviarEmailAGrupo(email, g1, new ArrayList<>());

        assertEquals(3, email.getDestinatarios().size());

        assertEquals(1, contacto1.bandeja.getBandejaEntrada().size());
        assertEquals(1, contacto2.bandeja.getBandejaEntrada().size());
        assertEquals(1, contacto3.bandeja.getBandejaEntrada().size());

    }

    @Test
    void crear_grupo_y_mandar_email_al_grupo_pero_excluyendo_un_contacto() {

        EmailManager em1 = new EmailManager();
        Contacto contacto1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto contacto2 = new Contacto("Candela Cano", "candelaria@gmail.com");
        Contacto contacto3 = new Contacto("Carla Marturet", "carla@gmail.com");
        
        GrupoDeUsuarios g1 = new GrupoDeUsuarios("El Club");
        g1.agregarAlGrupo(contacto1);
        g1.agregarAlGrupo(contacto2);
        g1.agregarAlGrupo(contacto3);

        Email email = new Email();
        email.setAsunto("Universidad");
        email.setContenido("Hoy no hay clase vamoooo"); 
        email.setRemitente(contacto1);

        ArrayList<Contacto> excluirContactos = new ArrayList<>();
        excluirContactos.add(contacto2);

        em1.enviarEmailAGrupo(email, g1, excluirContactos);

        assertEquals(2, email.getDestinatarios().size());

        assertEquals(1, contacto1.bandeja.getBandejaEntrada().size());
        assertEquals(0, contacto2.bandeja.getBandejaEntrada().size());
        assertEquals(1, contacto3.bandeja.getBandejaEntrada().size());

    }

}
