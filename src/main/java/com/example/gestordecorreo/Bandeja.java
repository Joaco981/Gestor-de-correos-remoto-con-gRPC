package com.example.gestordecorreo;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Bandeja  {
    private ArrayList<Email> bandejaEntrada = new ArrayList<>();
    private ArrayList<Email> bandejaEnviados = new ArrayList<>();
    private ArrayList<Email> favoritos = new ArrayList<>();
  
  
    public void agregarFav(Email email ){
            if (!favoritos.contains(email) ){
            favoritos.add(email);
        }else{
            if (favoritos.contains(email)) {
                 favoritos.remove(email);
            }    
           
        }
        
    }


    public ArrayList<Email> getBandejaEnviados(){
        return bandejaEnviados;
    }

    public ArrayList<Email> getBandejaEntrada(){
        return bandejaEntrada;
    }

    public ArrayList<Email> getFavoritos(){
        return favoritos;
    }

    //metodos de filtros simples
    //se utiliza una lista de emails y un filtro (o condicion) que se aplica a cada email
    public ArrayList<Email> filtros (ArrayList<Email> email,  Predicate<Email> filtro){
                
        return email.stream()//la lista de emails se convierte en un flujo/conjunto de datos
                    .filter(filtro)//se aplica el filtro al conjunto de datos
                    .collect(Collectors
                    .toCollection(ArrayList::new));//los correos que cumplen con el filtro se almacenan en una nueva lista
    }

    //se utiliza expresiones lambda para los filtros
    //donde (e ->) que indica que para cada objeto de email (e) se aplica el filtro, ya sea que se busca un asunto determinado, un remitente, destinatario o dominio.
    //filtro asunto
    public Predicate<Email> filtroPorAsunto(String asuntoparaFiltrar){
        Predicate<Email> filtro= e -> e.getAsunto().contains(asuntoparaFiltrar);
        return filtro; 
    }

    //filtro remitente
    public Predicate<Email> filtroPorRemitente(Contacto contactoAFiltrar){
        Predicate<Email> filtro= e -> e.getRemitente().equals(contactoAFiltrar);
        return filtro; 
    }

    //filtro por destinataario
    public Predicate<Email> filtroPorDestinatario(ArrayList<Contacto> destinatariosAfiltrar){
        Predicate<Email> filtro= e -> e.getDestinatarios().equals(destinatariosAfiltrar);
        return filtro; 
    }

    //filtrar correos de la ucp
    public Predicate<Email> filtroPorDominio(String email){
        Predicate<Email> filtro= e -> e.getRemitente().getEmail().contains(email);
        return filtro; 
    }

    
}
 