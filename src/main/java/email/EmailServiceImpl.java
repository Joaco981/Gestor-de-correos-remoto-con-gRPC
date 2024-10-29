package email;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;

public class EmailServiceImpl extends EmailServiceGrpc.EmailServiceImplBase {
    private ArrayList<Email> bandejaEntrada = new ArrayList<>();

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

        // Agregar el correo a la bandeja de entrada
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
}
