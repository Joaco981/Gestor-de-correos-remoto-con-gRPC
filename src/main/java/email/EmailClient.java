package email;

import email.EmailOuterClass;
import email.EmailServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class EmailClient {
     private final EmailServiceGrpc.EmailServiceBlockingStub blockingStub;
    private final EmailServiceGrpc.EmailServiceStub asyncStub;

    public EmailClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = EmailServiceGrpc.newBlockingStub(channel);
        asyncStub = EmailServiceGrpc.newStub(channel);
    }

    public void sendEmail(EmailOuterClass.Email email) {
        EmailOuterClass.EmailRequest request = EmailOuterClass.EmailRequest.newBuilder().setEmail(email).build();
        EmailOuterClass.EmailResponse response = blockingStub.sendEmail(request);
        System.out.println(response.getMessage());
    }

    public void receiveEmails(String email) {
        EmailOuterClass.ReceiveRequest request = EmailOuterClass.ReceiveRequest.newBuilder().setEmail(email).build();
        asyncStub.receiveEmails(request, new StreamObserver<EmailOuterClass.Email>() {
            @Override
            public void onNext(EmailOuterClass.Email email) {
                System.out.println("Received email: " + email.getAsunto());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("All emails received");
            }
        });
    }

    public static void main(String[] args) {
        EmailClient client = new EmailClient("localhost", 50051);

        // Create and send an email
        EmailOuterClass.Contacto remitente = EmailOuterClass.Contacto.newBuilder().setNombreCompleto("John Doe").setEmail("john@example.com").build();
        EmailOuterClass.Contacto destinatario = EmailOuterClass.Contacto.newBuilder().setNombreCompleto("Jane Doe").setEmail("jane@example.com").build();
        EmailOuterClass.Email email = EmailOuterClass.Email.newBuilder()
                .setAsunto("Hello")
                .setContenido("This is a test email")
                .setRemitente(remitente)
                .addDestinatarios(destinatario)
                .build();
        client.sendEmail(email);

        // Receive emails for a specific email address
        client.receiveEmails("jane@example.com");
    }
}
