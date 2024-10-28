package email;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class EmailServiceImpl extends EmailServiceGrpc.EmailServiceImplBase {
    private List<EmailOuterClass.Email> emailStorage = new ArrayList<>();

    @Override
    public void sendEmail(EmailOuterClass.EmailRequest request, StreamObserver<EmailOuterClass.EmailResponse> responseObserver) {
        EmailOuterClass.Email email = request.getEmail();
        
        System.out.println("Recibiendo correo de: " + email.getRemitente().getNombreCompleto() + " - Asunto: " + email.getAsunto());
        
        for (EmailOuterClass.Email storedEmail : emailStorage) {
            if (storedEmail.getId().equals(email.getId())) {
                responseObserver.onNext(EmailOuterClass.EmailResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Este email ya fue enviado anteriormente.")
                        .build());
                responseObserver.onCompleted();
                return;
            }
        }

        emailStorage.add(email);
        
        EmailOuterClass.EmailResponse response = EmailOuterClass.EmailResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Email enviado correctamente")
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void receiveEmails(EmailOuterClass.ReceiveRequest request, StreamObserver<EmailOuterClass.Email> responseObserver) {
        for (EmailOuterClass.Email storedEmail : emailStorage) {
            responseObserver.onNext(storedEmail);
        }
        responseObserver.onCompleted();
    }
}
