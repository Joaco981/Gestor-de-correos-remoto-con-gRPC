package com.example.gestordecorreo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    void crear_email_test(){
        Email e1 = new Email();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");

        e1.setAsunto("Universidad");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
    }

    @Test
    void crear_email_con_varias_personas_test(){
        Email e1 = new Email();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela Cano", "candelaria@gmail.com"); 
        Contacto persona3 = new Contacto("Carla Marturet", "@gmail.com"); 

        e1.setAsunto("Universidad");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);
        e1.agregarDestinatario(persona3);

        assertTrue(e1.getDestinatarios().size() == 2);
    }

    @Test
    void enviar_email_test(){
        Email e1 = new Email();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");

        
        e1.setAsunto("Universidad");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);

        em1.enviarEmail(e1);
        
        assertTrue(em1.getBandejaEnviados(persona1).size() == 1);

    }

    @Test
    void se_manda_mail_y_se_recibe(){
        Email e1 = new Email();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela Cano", "cande@gmail.com"); 
  
        e1.setAsunto("Universidad");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);
        assertTrue(em1.getBandejaEnviados(persona1).size() == 1);
        assertTrue(em1.getBandejaEntrada(persona2).size() == 1);
    }


    @Test
    void prueba_de_correo_valido(){
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        
        assertTrue(persona1.validarEmail(persona1.getEmail()));
    }

    //test para la cobertura de jacoco
    @Test
    public void validar_Email_Incorrecto() {
        Contacto contacto = new Contacto("Joaquin Flores", "joaquinho@gmail.com");
        assertFalse(contacto.validarEmail("joaquinho@example"));
    }

    @Test
    void se_manda_mail_y_se_borra_de_bandeja_de_enviados_test(){
        Email e1 = new Email();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Joaquin Flores", "joaco@gmail.com"); 
  
        e1.setAsunto("Universidad");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);
        assertTrue(em1.getBandejaEnviados(persona1).size() == 1);

        em1.borrarEmail(em1.getBandejaEnviados(persona1), e1);
        assertTrue(em1.getBandejaEnviados(persona1).isEmpty());
        
    }

//test para jacoco
 @Test
    public void Set_GetContenido() {
        Email email = new Email();
        email.setContenido("Este es el contenido del correo.");
        assertEquals("Este es el contenido del correo.", email.getContenido());
    }





    
    
}
