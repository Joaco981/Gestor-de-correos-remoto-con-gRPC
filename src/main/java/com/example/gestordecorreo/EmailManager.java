package com.example.gestordecorreo;

import java.util.ArrayList;

public class EmailManager {

    public void enviarEmail(Email email) {
        email.getRemitente().bandeja.getBandejaEnviados().add(email);

        for (Contacto destinatario : email.getDestinatarios()) {
            destinatario.bandeja.getBandejaEntrada().add(email);
        }
    }

    public void enviarEmailAGrupo(Email email, GrupoDeUsuarios grupo, ArrayList<Contacto> excluirContactos) {
        for (Contacto contacto : grupo.getContactos()) {
            email.agregarDestinatario(contacto);
            contacto.bandeja.getBandejaEntrada().add(email);
        }
        for (Contacto contacto : excluirContactos) {
            excluirContacto(email, contacto); 
            contacto.bandeja.getBandejaEntrada().remove(email);
        }
        email.getRemitente().bandeja.getBandejaEnviados().add(email);
    }
    

    public void excluirContacto(Email email, Contacto contacto) {
        email.getDestinatarios().remove(contacto);
    }

    public void borrarEmail(ArrayList<Email> bandeja, Email emailAEliminar) {
        bandeja.removeIf(email ->
            email.getAsunto().equals(emailAEliminar.getAsunto()) &&
            email.getContenido().equals(emailAEliminar.getContenido()) &&
            email.getRemitente().equals(emailAEliminar.getRemitente()) &&
            email.getDestinatarios().equals(emailAEliminar.getDestinatarios())
        );
    }
}
