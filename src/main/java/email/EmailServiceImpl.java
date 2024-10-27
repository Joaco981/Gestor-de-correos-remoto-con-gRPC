package email;

import email.EmailOuterClass;
import email.EmailServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class EmailServiceImpl extends EmailServiceGrpc.EmailServiceImplBase {
    private final List<EmailOuterClass.Email> emailStorage = new ArrayList<>();

    @Override
    public void sendEmail(EmailOuterClass.EmailRequest request, StreamObserver<EmailOuterClass.EmailResponse> responseObserver) {
        EmailOuterClass.Email email = request.getEmail();

        // Validar que el email tenga al menos un destinatario
        if (email.getDestinatariosCount() == 0) {
            EmailOuterClass.EmailResponse response = EmailOuterClass.EmailResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error: El email debe tener al menos un destinatario.")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        // Almacenar el email
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
        String emailAddress = request.getEmail();
        boolean foundEmail = false; // Para verificar si se encontraron correos

        for (EmailOuterClass.Email storedEmail : emailStorage) {
            for (EmailOuterClass.Contacto destinatario : storedEmail.getDestinatariosList()) {
                if (destinatario.getEmail().equals(emailAddress)) {
                    responseObserver.onNext(storedEmail);
                    foundEmail = true; // Se encontró al menos un correo
                }
            }
        }

        if (!foundEmail) {
            // Si no se encontró ningún correo, puedes enviar un aviso o simplemente cerrar la conexión
            System.out.println("No se encontraron correos para: " + emailAddress);
        }

        responseObserver.onCompleted();
    }
}
