package com.example.gestordecorreo;
import java.util.ArrayList;
//email manager es el encargado de gestionar los correos creados
public class EmailManager {
    //atributos de la clase bandeja donde se almacenan los correos
    private Bandeja bandeja;

    //contructor de bandeja donde se almacenaran los correos enviados y recibidos
    public EmailManager(){
        this.bandeja = new Bandeja();
    }
    
    //se envia y se recibe el email
    public void enviarEmail(Email email) {

    //se obtiene la bandeja de enviados del remitente y se agrega el email
    bandeja.getBandejaEnviados(email.getRemitente()).add(email);
    
    //para cada destinatario se obtiene la bandeja de entrada y se agrega el email
        for (Contacto destinatario : email.getDestinatarios()) {
            bandeja.getBandejaEntrada(destinatario).add(email);
        }
    }


    public ArrayList<Email> getBandejaEnviados(Contacto persona){
        return bandeja.getBandejaEnviados(persona);
    }

    public ArrayList<Email> getBandejaEntrada(Contacto persona){
        return bandeja.getBandejaEntrada(persona);
    }
    
    public void borrarEmail(ArrayList<Email> bandeja, Email email ){
        bandeja.remove(email);

    }




}
