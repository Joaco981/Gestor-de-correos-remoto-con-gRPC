package email;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;
import java.util.function.Consumer;


public class EmailServiceImpl extends EmailServiceGrpc.EmailServiceImplBase {
    private ArrayList<Email> bandejaEntrada = new ArrayList<>();
    private ArrayList<Email> bandejaEnviados = new ArrayList<>();
    private ArrayList<Email> bandejaFavoritos = new ArrayList<>();
    private ArrayList<Contacto> listaContactos = new ArrayList<>();

    // Método para buscar un contacto por email
    public Contacto buscarContactoPorEmail(String email) {
        for (Contacto contacto : listaContactos) {
            if (contacto.getEmail().equalsIgnoreCase(email)) {
                return contacto;
            }
        }
        return null; // Devuelve null si no encuentra el contacto
    }
    @Override
    public void enviarEmail(EmailOuterClass.EmailRequest request, StreamObserver<EmailOuterClass.EmailResponse> responseObserver) {
        EmailOuterClass.Email emailProto = request.getEmail();
        
        // Convertir EmailOuterClass.Email a tu objeto Email
        Email email = new Email();
        email.setAsunto(emailProto.getAsunto());
        email.setContenido(emailProto.getContenido());
        
        // Establecer el remitente usando un constructor existente
        Contacto remitente = new Contacto(emailProto.getRemitente().getNombreCompleto(), emailProto.getRemitente().getEmail());
        email.setRemitente(remitente);
        
        // Agregar los destinatarios
        for (EmailOuterClass.Contacto contacto : emailProto.getDestinatariosList()) {
            Contacto destinatario = new Contacto(contacto.getNombreCompleto(), contacto.getEmail());
            email.agregarDestinatario(destinatario);
        }

        bandejaEnviados.add(email);
        bandejaEntrada.add(email);

        if (email.getDestinatarios().size() > 1) {
            System.out.println(remitente.getNombre() + " ha enviado un correo a un grupo");
        } else if (email.getDestinatarios().size() == 1) {
            System.out.println(remitente.getNombre() + " ha enviado un correo a " + email.getDestinatarios().get(0).getNombre());
        }

        EmailOuterClass.EmailResponse response = EmailOuterClass.EmailResponse.newBuilder()
                .setMessage("Email enviado correctamente")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void recibirEmails(EmailOuterClass.ReceiveRequest request, StreamObserver<EmailOuterClass.Email> responseObserver) {
        for (Email email : bandejaEntrada) {
            // Enviar cada correo a través del StreamObserver
            EmailOuterClass.Email emailProto = EmailOuterClass.Email.newBuilder()
                    .setAsunto(email.getAsunto())
                    .setContenido(email.getContenido())
                    // Establecer el remitente
                    .setRemitente(EmailOuterClass.Contacto.newBuilder()
                            .setNombreCompleto(email.getRemitente().getNombre())
                            .setEmail(email.getRemitente().getEmail())
                            .build())
                    // Agregar los destinatarios
                    .addAllDestinatarios(email.getDestinatarios().stream().map(destinatario -> 
                            EmailOuterClass.Contacto.newBuilder()
                                    .setNombreCompleto(destinatario.getNombre())
                                    .setEmail(destinatario.getEmail())
                                    .build()
                    ).toList())
                    .build();
            responseObserver.onNext(emailProto);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void verBandeja(EmailOuterClass.BandejaRequest request, StreamObserver<EmailOuterClass.BandejaResponse> responseObserver) {
        EmailOuterClass.Contacto clientContacto = request.getClientEmail();

        ArrayList<Email> bandejaEntradaCliente = obtenerBandejaEntradaPorEmail(clientContacto);
        ArrayList<Email> bandejaEnviadosCliente = obtenerBandejaEnviadosPorEmail(clientContacto);
        ArrayList<Email> bandejaFavoritosCliente = obtenerBandejaFavoritosPorEmail(clientContacto);

        EmailOuterClass.BandejaResponse.Builder responseBuilder = EmailOuterClass.BandejaResponse.newBuilder();

        // Agregar los correos de bandeja de entrada, enviados y favoritos
        agregarEmailsABandeja(bandejaEntradaCliente, responseBuilder::addBandejaEntrada);
        agregarEmailsABandeja(bandejaEnviadosCliente, responseBuilder::addBandejaEnviados);
        agregarEmailsABandeja(bandejaFavoritosCliente, responseBuilder::addFavoritos);

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private void agregarEmailsABandeja(ArrayList<Email> emails, Consumer<EmailOuterClass.Email> addMethod) {
        for (Email email : emails) {
            EmailOuterClass.Email emailProto = EmailOuterClass.Email.newBuilder()
                .setAsunto(email.getAsunto())
                .setContenido(email.getContenido())
                .setRemitente(EmailOuterClass.Contacto.newBuilder()
                        .setNombreCompleto(email.getRemitente().getNombre())
                        .setEmail(email.getRemitente().getEmail())
                        .build())
                .addAllDestinatarios(email.getDestinatarios().stream()
                        .map(dest -> EmailOuterClass.Contacto.newBuilder()
                                .setNombreCompleto(dest.getNombre())
                                .setEmail(dest.getEmail())
                                .build())
                        .collect(Collectors.toList()))
                .build();
            addMethod.accept(emailProto);
        }
    }

    // Método para agregar un correo a favoritos
    public void agregarFavorito(Email email, Contacto cliente) {
        cliente.bandeja.agregarFav(email); // Usar la bandeja específica del contacto
        System.out.println("El email con asunto '" + email.getAsunto() + "' ha sido agregado a favoritos para " + cliente.getNombre());
    }
    

    private ArrayList<Email> obtenerBandejaFavoritosPorEmail(EmailOuterClass.Contacto clientContacto) {
        ArrayList<Email> bandejaDelCliente = new ArrayList<>();
        for (Email email : bandejaFavoritos) {
            if (email.getDestinatarios().stream().anyMatch(dest -> dest.getEmail().equals(clientContacto.getEmail()))) {
                bandejaDelCliente.add(email);
            }
        }
        return bandejaDelCliente;
    }

    private ArrayList<Email> obtenerBandejaEntradaPorEmail(EmailOuterClass.Contacto clientContacto) {
        ArrayList<Email> bandejaDelCliente = new ArrayList<>();
        for (Email email : bandejaEntrada) {
            for (Contacto destinatario : email.getDestinatarios()) {
                if (destinatario.getEmail().equals(clientContacto.getEmail())) {
                    bandejaDelCliente.add(email);
                    break;
                }
            }
        }
        return bandejaDelCliente;
    }

    private ArrayList<Email> obtenerBandejaEnviadosPorEmail(EmailOuterClass.Contacto clientContacto) {
        ArrayList<Email> bandejaDelCliente = new ArrayList<>();
        for (Email email : bandejaEnviados) {
            if (email.getRemitente().getEmail().equals(clientContacto.getEmail())) {
                bandejaDelCliente.add(email);
            }
        }
        return bandejaDelCliente;
    }
}
