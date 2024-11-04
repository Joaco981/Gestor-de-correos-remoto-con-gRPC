package email;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;

public class EmailServiceImpl extends EmailServiceGrpc.EmailServiceImplBase {
    private ArrayList<Email> bandejaEntrada = new ArrayList<>();
    private ArrayList<Email> bandejaEnviados = new ArrayList<>();

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

        // Agregar el correo a la bandeja de enviados del remitente
        bandejaEnviados.add(email);

        // Agregar el correo a la bandeja de entrada de cada destinatario
        bandejaEntrada.add(email);

        // Enviar la respuesta
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
        EmailOuterClass.Contacto clientContacto = request.getClientEmail(); // Cambiado a Contacto

        // Obtener los emails de la bandeja de entrada y de enviados del cliente usando el objeto Contacto
        ArrayList<Email> bandejaEntradaCliente = obtenerBandejaEntradaPorEmail(clientContacto);
        ArrayList<Email> bandejaEnviadosCliente = obtenerBandejaEnviadosPorEmail(clientContacto);

        // Construir la respuesta
        EmailOuterClass.BandejaResponse.Builder responseBuilder = EmailOuterClass.BandejaResponse.newBuilder();
        for (Email email : bandejaEntradaCliente) {
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
            responseBuilder.addBandejaEntrada(emailProto);
        }

        for (Email email : bandejaEnviadosCliente) {
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
            responseBuilder.addBandejaEnviados(emailProto);
        }

        // Enviar la respuesta completa al cliente
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
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
