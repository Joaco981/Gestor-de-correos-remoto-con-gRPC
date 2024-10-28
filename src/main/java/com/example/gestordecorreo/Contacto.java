package com.example.gestordecorreo;
import  java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contacto {
    private String nombreCompleto;
    private String email;
    public Bandeja bandeja;

    public Contacto(String nombreCompleto, String email) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.bandeja = new Bandeja();
    }

    public String getNombre() {
        return nombreCompleto;
    }

    public void setNombre(String valor) {
        nombreCompleto = valor;
    }

    public String getEmail(){
        return email;
    }

    //recibe la direccion de  email a validar
    //devuelve valor booleano 
    //clase Pattern: utiliza un patron de expresion regular para validar un email
    //la cadena de caracteres son las "partes" que debe cumplir el email para que sea valido
    //clase Matcher: se encarga de comparar el email con el patron de expresion regular
    public boolean validarEmail(String email){
        Pattern pattern= Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();

    }
    
}