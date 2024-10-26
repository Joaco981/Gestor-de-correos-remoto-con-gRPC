package com.example.gestordecorreo;
import java.util.*;

public class Email  {
    private String asunto;
    private String contenido;
    private Contacto remitente;
    private ArrayList<Contacto> destinatarios;
   
    //elemento del email
    public Email(){
        destinatarios = new ArrayList<Contacto>();
    }
    public String getAsunto(){
        return asunto;
    }

    public String getContenido(){
        return contenido;
    }

    public Contacto getRemitente(){
        return remitente;
    }

    public ArrayList<Contacto> getDestinatarios(){
        return destinatarios;
    }

    public void setAsunto(String valor){
        asunto = valor;   
    }

    public void setContenido(String valor){
        contenido = valor;
    }

    public void setRemitente(Contacto valor){
        remitente = valor;
    }
    //es el para
    public void agregarDestinatario(Contacto valor){
        destinatarios.add(valor);
    }

    

   
    
}