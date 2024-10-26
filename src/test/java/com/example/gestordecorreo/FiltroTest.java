package com.example.gestordecorreo;

import java.util.ArrayList;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FiltroTest {

//filtros simples
//se comprueba que los filtros funcionen correctamente
//se comprueban los metodos para filtrar
@Test 
void filtro_por_asunto_simple(){
		//primer correo
        Email e1 = new Email();
		Email e2 = new Email();
		Email e3 = new Email();
		Email e4 = new Email();
        Bandeja b1 = new Bandeja();
        EmailManager em1 = new EmailManager();	
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela", "joaco@gmail.com"); 
		
        //primer mail
        e1.setAsunto("Trabajo");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);

		//segundo mail
		e2.setAsunto("Universidad");
		e2.setContenido("holaaa");
		e2.setRemitente(persona1);
		e2.agregarDestinatario(persona2);

		em1.enviarEmail(e2);

		//tercer mail
		e3.setAsunto("Universidad");
       	e3.setContenido("holaaa");
       	e3.setRemitente(persona1);
       	e3.agregarDestinatario(persona2);

		em1.enviarEmail(e3);
	   
		//cuarto email
		e4.setAsunto("Universidad");
		e4.setContenido("holaaa");
		e4.setRemitente(persona1);
		e4.agregarDestinatario(persona2);

		em1.enviarEmail(e4);

        String asuntoParaFiltrar = "Universidad";
 
        ArrayList<Email> emailsfiltrados = b1.filtros(em1.getBandejaEntrada(persona2), b1.filtroPorAsunto(asuntoParaFiltrar));

        assertEquals(3, emailsfiltrados.size());
        //se verifica la existencia de los correos en correosfiltrados
        assertTrue(emailsfiltrados.contains(e2));
        assertTrue(emailsfiltrados.contains(e3));
        assertTrue(emailsfiltrados.contains(e4));  
	}

    @Test 
    void filtro_por_remitente(){
		//primer correo
        Email e1 = new Email();
		Email e2 = new Email();
		Email e3 = new Email();
		Email e4 = new Email();
        Bandeja b1 = new Bandeja();
        EmailManager em1 = new EmailManager();	
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela", "candelaria@gmail.com"); 
		Contacto persona3 = new Contacto("Carla", "carlita@gmail.com"); 
		Contacto persona4 = new Contacto("Taylor", "ts13@gmail.com"); 
        
		//primer mail de persona 1 a persona 2
        e1.setAsunto("Trabajo");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);

		//primer mail persona 3 a persona 2
		e2.setAsunto("Universidad");
		e2.setContenido("holaaa");
		e2.setRemitente(persona3);
		e2.agregarDestinatario(persona2);

		em1.enviarEmail(e2);

		//tercer mail
		//persona 4 a persona 2
		e3.setAsunto("Universidad");
       	e3.setContenido("holaaa");
       	e3.setRemitente(persona4);
       	e3.agregarDestinatario(persona2);

		em1.enviarEmail(e3);
	   
		//cuarto email
		//persona 4 a persona 2
		e4.setAsunto("Universidad");
		e4.setContenido("holaaa");
		e4.setRemitente(persona4);
		e4.agregarDestinatario(persona2);

		em1.enviarEmail(e4);

        ArrayList<Email> emailsfiltrados = b1.filtros(em1.getBandejaEntrada(persona2), b1.filtroPorRemitente(persona4));

        assertEquals(2, emailsfiltrados.size());
        //se verifica la existencia de los correos en correosfiltrados
        assertFalse(emailsfiltrados.contains(e2));
        assertTrue(emailsfiltrados.contains(e3));
        assertTrue(emailsfiltrados.contains(e4));
	
	}

	@Test 
    void filtro_por_destinatarios(){
		
        Email e1 = new Email();
		Email e2 = new Email();
        Bandeja b1 = new Bandeja();
        EmailManager em1 = new EmailManager();	
        Contacto persona1 = new Contacto("Joaquin Flores", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Candela", "candelaria@gmail.com"); 
		Contacto persona3 = new Contacto("Carla", "carlita@gmail.com"); 
		Contacto persona4 = new Contacto("Taylor", "ts13@gmail.com"); 
        
		//primer mail de persona 1 a persona 2
        e1.setAsunto("Trabajo");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);
     
        //persona 1 manda email a persona3 y persona 4
		e2.setAsunto("Universidad");
		e2.setContenido("Revisen el Trabajo");
		e2.setRemitente(persona1);
		e2.agregarDestinatario(persona3);
		e2.agregarDestinatario(persona4);

		em1.enviarEmail(e2);
			
        ArrayList<Contacto> destinatarios = e2.getDestinatarios();
		
        ArrayList<Email> emailsfiltrados = b1.filtros(em1.getBandejaEnviados(persona1), b1.filtroPorDestinatario(destinatarios));

        assertEquals(1, emailsfiltrados.size());
        //se verifica la existencia de los correos en correosfiltrados
        assertTrue(emailsfiltrados.contains(e2));
        assertFalse(emailsfiltrados.contains(e1));
	}

    @Test 
    void filtro_por_dominio_test(){
		
		//primer correo
        Email e1 = new Email();
		Email e2 = new Email();
        Bandeja b1 = new Bandeja();
        EmailManager em1 = new EmailManager();	
        Contacto persona1 = new Contacto("Joaquin Flores", "info@ucp.edu.ar");
        Contacto persona2 = new Contacto("Candela", "candelaria@gmail.com"); 
        Contacto persona3 = new Contacto("Carla", "alumnado@ucp.edu.ar"); 
        
		//primer mail de cuenca (persona1) a yo(persona2)
        e1.setAsunto("Trabajo");
        e1.setContenido("holaaa");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);
     
        //segundo mail de cuenca (persona3) a yo(persona2)
		e2.setAsunto("Universidad");
		e2.setContenido("Revisen el Trabajo");
		e2.setRemitente(persona3);
		e2.agregarDestinatario(persona2);

		em1.enviarEmail(e2);
			
        ArrayList<Email> emailsfiltrados = b1.filtros(em1.getBandejaEntrada(persona2), b1.filtroPorDominio("@ucp.edu.ar"));
      

        assertEquals(2, emailsfiltrados.size());

	}

    //TEST CON FILTROS COMPLEJOS
    @Test
    void se_comprueba_la_descripcion_test() {
        Bandeja b1 = new Bandeja();
        String descripcion = "Filtro por asunto importante";
        Predicate<Email> predicado = b1.filtroPorAsunto("importante");
        Filtro filtro = new Filtro(descripcion, predicado);

        assertEquals(descripcion, filtro.getDescripcion());
    }

    @Test
    void se_comprueba_el_predicado_test() {
        Bandeja b1 = new Bandeja();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Joaco", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Joaco", "joaco@gmail.com");
        Predicate<Email> predicado = b1.filtroPorAsunto("importante");
        Filtro filtro = new Filtro("Filtro por asunto importante", predicado);

        Email e1 = new Email();
        e1.setAsunto("Subir las notas porfavor es importante");
        e1.setRemitente(persona1);
        em1.enviarEmail(e1);

        Email e2 = new Email();
        e1.setRemitente(persona2);
        e2.setAsunto("Esto no pinta nada bien");

        assertTrue(filtro.getPredicado().test(e1));
        assertFalse(filtro.getPredicado().test(e2));
    }

    @Test
    void filtros_complejos_por_asunto_y_por_remitente_test(){
        Email e1 = new Email();
        Email e2 = new Email();
        EmailManager em1 = new EmailManager();

        Contacto persona1 = new Contacto("Joaco", "joaco@gmail.com");
        Contacto persona2 = new Contacto("Cande", "cande@gmail.com");
        Bandeja b1 = new Bandeja();

        // Definir los predicados y filtros
        Predicate<Email> predicado = b1.filtroPorAsunto("Trabajo");
        Predicate<Email> predicado2 = b1.filtroPorRemitente(persona1);
        Filtro f1 = new Filtro("Correos que contengan asunto Trabajo", predicado);
        Filtro f2 = new Filtro("Correos de persona1", predicado2);

        Predicate<Email> predicadoFinal = f1.getPredicado().and(f2.getPredicado());

        // Primer mail de persona 1 a persona 2
        e1.setAsunto("Trabajo");
        e1.setContenido("Le envio mi Trabajo Práctico");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);

        // Segundo mail de persona 1 a persona 2
        e2.setAsunto("Universidad");
        e2.setContenido("holaaa todo bien");
        e2.setRemitente(persona1);
        e2.agregarDestinatario(persona2);

        em1.enviarEmail(e2);

        ArrayList<Email> correosFiltrados = b1.filtros(em1.getBandejaEntrada(persona2), predicadoFinal);

        assertEquals(1, correosFiltrados.size());
        assertTrue(correosFiltrados.contains(e1));
        assertFalse(correosFiltrados.contains(e2));
    }

    @Test
    void filtro_compuesto_bandeja_enviados(){
        Email e1 = new Email();
        Email e2 = new Email();
        Bandeja b1 = new Bandeja();
        EmailManager em1 = new EmailManager();
        Contacto persona1 = new Contacto("Candela Cano ", "cande@gmail.com");
        Contacto persona2 = new Contacto("Jose Fernandez", "jose@gmail.com");
        Contacto persona3 = new Contacto("Gilda Romero", "gromero@gmail.com");
      
        //primer email de persona 1 a destinatario 2
        e1.setAsunto("Trabajo Practico 2");
        e1.setContenido("Le envio mi Trabajo Práctico número 2");
        e1.setRemitente(persona1);
        e1.agregarDestinatario(persona2);

        em1.enviarEmail(e1);

        // Email de persona 1 a destinatario 3
        e2.setAsunto("Cambio fecha");
        e2.setContenido("Podemos cambiar la fecha del parcial?");
        e2.setRemitente(persona1);
        e2.agregarDestinatario(persona3);

        em1.enviarEmail(e2);

        // Definir los predicados y filtros
        Predicate<Email> predicado = b1.filtroPorAsunto("Trabajo Practico 2");
        Predicate<Email> predicado2 = b1.filtroPorDestinatario(e1.getDestinatarios());
        Filtro f1 = new Filtro("Correos que contengan asunto Trabajo Practico 2", predicado);
        Filtro f2 = new Filtro("Correos para la persona2", predicado2);

        Predicate<Email> predicadoFinal = f1.getPredicado().and(f2.getPredicado());

        ArrayList<Email> correosFiltrados = b1.filtros(em1.getBandejaEnviados(persona1), predicadoFinal);

        assertEquals(1, correosFiltrados.size());
        assertTrue(correosFiltrados.contains(e1));
        assertFalse(correosFiltrados.contains(e2));
    }

  

}