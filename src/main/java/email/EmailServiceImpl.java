package email;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.UUID;

import com.example.gestordecorreo.EmailManager;
import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;

public class EmailServiceImpl extends EmailServiceGrpc.EmailServiceImplBase {
    private EmailManager emailManager = new EmailManager();

    @Override
    public void enviarEmail(EmailOuterClass.EmailRequest request, StreamObserver<EmailOuterClass.EmailResponse> responseObserver) {
        EmailOuterClass.Email emailProto = request.getEmail();
        
        // Convertir EmailOuterClass.Email a com.example.gestordecorreo.Email
        Email email = new Email();
        email.setAsunto(emailProto.getAsunto());
        email.setContenido(emailProto.getContenido());
        email.setRemitente(new Contacto(emailProto.getRemitente().getNombreCompleto(), emailProto.getRemitente().getEmail()));
        
        for (EmailOuterClass.Contacto destinatarioProto : emailProto.getDestinatariosList()) {
            email.agregarDestinatario(new Contacto(destinatarioProto.getNombreCompleto(), destinatarioProto.getEmail()));
        }

        emailManager.enviarEmail(email);

        EmailOuterClass.EmailResponse response = EmailOuterClass.EmailResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Email enviado correctamente")
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void recibirEmails(EmailOuterClass.ReceiveRequest request, StreamObserver<EmailOuterClass.Email> responseObserver) {
        
        Contacto contacto = new Contacto("Nombre del contacto", request.getEmail());
        ArrayList<Email> emails = emailManager.getBandejaEntrada(contacto);
        
        for (Email email : emails) {
            EmailOuterClass.Email emailProto = EmailOuterClass.Email.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setAsunto(email.getAsunto())
                    .setContenido(email.getContenido())
                    .setRemitente(EmailOuterClass.Contacto.newBuilder()
                            .setNombreCompleto(email.getRemitente().getNombre())
                            .setEmail(email.getRemitente().getEmail())
                            .build())
                    .build();
            
            for (Contacto destinatario : email.getDestinatarios()) {
                emailProto = emailProto.toBuilder()
                        .addDestinatarios(EmailOuterClass.Contacto.newBuilder()
                                .setNombreCompleto(destinatario.getNombre())
                                .setEmail(destinatario.getEmail())
                                .build())
                        .build();
            }
            
            responseObserver.onNext(emailProto);
        }
        
        responseObserver.onCompleted();
    }
}