package com.example.gestordecorreo;
import java.util.ArrayList;
//email manager es el encargado de gestionar los correos creados
public class EmailManager {
    
    //se envia y se recibe el email
    public void enviarEmail(Email email) {
    Email emailClonado = email.clonarEmail();

    emailClonado.getRemitente().bandeja.getBandejaEnviados().add(emailClonado);

        for (Contacto destinatario : email.getDestinatarios()) {
             destinatario.bandeja.getBandejaEntrada().add(emailClonado);
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
