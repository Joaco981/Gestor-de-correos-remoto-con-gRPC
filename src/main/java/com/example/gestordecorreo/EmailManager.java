package com.example.gestordecorreo;
import java.util.ArrayList;
//email manager es el encargado de gestionar los correos creados
public class EmailManager {
    
    // Se envía y se recibe el email
    public void enviarEmail(Email email) {
        // Agregar el correo a la bandeja de enviados del remitente
        email.getRemitente().bandeja.getBandejaEnviados().add(email);
        System.out.println("Correo enviado de " + email.getRemitente().getNombre() + " a:");

        // Agregar el correo a la bandeja de entrada de los destinatarios
        for (Contacto destinatario : email.getDestinatarios()) {
            destinatario.bandeja.getBandejaEntrada().add(email);
            System.out.println("- " + destinatario.getNombre());
        }
    }

    public void borrarEmail(ArrayList<Email> bandeja, Email emailAEliminar) {
        // Buscar el email que coincida con los atributos de emailAEliminar y eliminarlo
        bandeja.removeIf(email -> 
            email.getAsunto().equals(emailAEliminar.getAsunto()) &&
            email.getContenido().equals(emailAEliminar.getContenido()) &&
            email.getRemitente().equals(emailAEliminar.getRemitente()) &&
            email.getDestinatarios().equals(emailAEliminar.getDestinatarios())
        );
    }
    

  



}
