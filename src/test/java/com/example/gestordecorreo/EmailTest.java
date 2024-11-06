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
        e1.setContenido("Quiero consultar mis horarioa");
        e1.setRemitente(persona1);
        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));
    }

    @Test
    void crear_email_con_varias_personas_test(){
        Email e1 = new Email();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela Cano", "candelaria@gmail.com"); 
        Contacto persona3 = new Contacto("Carla Marturet", "carla@gmail.com"); 

        e1.setAsunto("Universidad");
        e1.setContenido("Pueden cambiar la fecha del examen?");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);
        e1.agregarDestinatario(persona3);

        assertTrue(e1.getDestinatarios().size() == 2);
        
        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));
        assertTrue(persona2.validarEmail(persona2.getEmail()));
        assertTrue(persona3.validarEmail(persona3.getEmail()));
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
        
        assertTrue(persona1.bandeja.getBandejaEnviados().size() == 1);

        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));

    }

    @Test
    void se_manda_mail_y_se_recibe(){
        Email e1 = new Email();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Carla Marturet", "carla@gmail.com"); 
        Contacto persona3 = new Contacto("Candela Cano", "candelaria@gmail.com"); 
        
        e1.setAsunto("Universidad");
        e1.setContenido("Darme de baja de la mesa de examen");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);
        assertTrue(persona1.bandeja.getBandejaEnviados().size() == 1);
        assertTrue(persona2.bandeja.getBandejaEntrada().size() == 1);
        assertTrue(persona3.bandeja.getBandejaEntrada().isEmpty());

        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));
        assertTrue(persona2.validarEmail(persona2.getEmail()));
        assertTrue(persona3.validarEmail(persona3.getEmail()));
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
        Contacto persona2 = new Contacto("Candela Cano", "candelaria@gmail.com"); 

        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));
        assertTrue(persona2.validarEmail(persona2.getEmail()));

        e1.setAsunto("Universidad");
        e1.setContenido("Pueden cambiar la fecha del examen?");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);
        //se asegura que el email se envio
        em1.enviarEmail(e1);
        assertTrue(persona1.bandeja.getBandejaEnviados().size() == 1);
        assertTrue(persona2.bandeja.getBandejaEntrada().size() == 1);

        //se borra el email de la bandeja de enviados
        //se comprueba que el email no se borra de la bandeja de entrada del destinatario  
        em1.borrarEmail(persona1.bandeja.getBandejaEnviados(), e1);
        assertTrue(persona1.bandeja.getBandejaEnviados().isEmpty());
        assertTrue(persona2.bandeja.getBandejaEntrada().size() == 1);

        //prueba
        //se borra el email de la bandeja de entrada
        //el correo se borra de ambas bandejas, del remitente y de su destinatario
        em1.borrarEmail(persona2.bandeja.getBandejaEntrada(), e1);
        assertTrue(persona1.bandeja.getBandejaEnviados().isEmpty());
        assertTrue(persona2.bandeja.getBandejaEntrada().isEmpty());

        
        
    }

//test para jacoco
 @Test
    public void Set_GetContenido() {
        Email email = new Email();
        email.setContenido("Este es el contenido del correo.");
        assertEquals("Este es el contenido del correo.", email.getContenido());
        
    }


     @Test
    void prueba_cambiar_asunto_email(){
        Email e1 = new Email();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela Cano", "cande@gmail.com");
        e1.setAsunto("Universidad");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);
        
        em1.enviarEmail(e1);
        
        assertTrue(persona1.bandeja.getBandejaEnviados().size() == 1);
        assertTrue(persona2.bandeja.getBandejaEntrada().size() == 1);

        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));

        //cambiamos el asunto
        e1.setAsunto("Hola");
        assertEquals("Universidad",persona2.bandeja.getBandejaEntrada().get(0).getAsunto());
    }

    @Test
    void prueba_cambiar_contenido_email(){
        Email e1 = new Email();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela Cano", "cande@gmail.com");
        e1.setAsunto("Universidad");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);
        
        em1.enviarEmail(e1);
        
        assertTrue(persona1.bandeja.getBandejaEnviados().size() == 1);
        assertTrue(persona2.bandeja.getBandejaEntrada().size() == 1);

        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));

        //cambiamos el asunto
        e1.setContenido("Ayuda por favor");
        assertEquals("holaaa",persona2.bandeja.getBandejaEntrada().get(0).getContenido());
    }


    @Test
    void marcar_correo_como_favorito(){
        Email e1 = new Email();
		Email e2 = new Email();
		
        EmailManager em1 = new EmailManager();	
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela Cano", "candelaria@gmail.com"); 

        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));
        assertTrue(persona2.validarEmail(persona2.getEmail()));
		
        //primer mail
        e1.setAsunto("Trabajo");
        e1.setContenido("Quiero consultar mis horarios");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);

		//segundo mail
		e2.setAsunto("Universidad");
		e2.setContenido("Subir las notas porfavor es importante");
		e2.setRemitente(persona1);
		e2.agregarDestinatario(persona2);

		em1.enviarEmail(e2);

        e2.marcarComoFav(e2, persona2);
        
        assertTrue(persona2.bandeja.getFavoritos().contains(e2));
        assertFalse(persona2.bandeja.getFavoritos().contains(e1));

        e2.marcarComoFav(e2, persona2);
        assertFalse(persona2.bandeja.getFavoritos().contains(e2));

    }
    
    @Test
    void crear_un_email_y_excluir_destinatario(){
        Email e1 = new Email();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela Cano", "candelaria@gmail.com");
        Contacto persona3 = new Contacto("Carla Marturet", "carla@gmail.com");
        //Prueba de que el email es valido
        assertTrue(persona1.validarEmail(persona1.getEmail()));
        assertTrue(persona2.validarEmail(persona2.getEmail()));

        e1.setAsunto("Trabajo");
        e1.setContenido("Quiero consultar mis horarios");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);
        e1.agregarDestinatario(persona3);

        em1.excluirContacto(e1, persona3);
        em1.enviarEmail(e1);

        assertTrue(persona1.bandeja.getBandejaEnviados().size() == 1);
        assertTrue(persona2.bandeja.getBandejaEntrada().size() == 1);
        assertTrue(persona3.bandeja.getBandejaEntrada().isEmpty());
    }
    
    
}
