package email;

import email.EmailOuterClass;
import email.EmailServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.UUID;  // Importar UUID

public class EmailClient {
    private final ManagedChannel channel;
    private final EmailServiceGrpc.EmailServiceBlockingStub blockingStub;
    private final EmailServiceGrpc.EmailServiceStub asyncStub;

    public EmailClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
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

    public void receiveEmails() {
        EmailOuterClass.ReceiveRequest request = EmailOuterClass.ReceiveRequest.newBuilder().build(); // Crear la solicitud vacía
        asyncStub.receiveEmails(request, new StreamObserver<EmailOuterClass.Email>() {
            @Override
            public void onNext(EmailOuterClass.Email email) {
                System.out.println("Asunto: " + email.getAsunto());
                System.out.println("  De: " + email.getRemitente().getNombreCompleto() + " (" + email.getRemitente().getEmail() + ")");
                System.out.println("  Contenido: " + email.getContenido());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Se recibieron todos los emails!");
            }
        });
    }

    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            System.out.println("El canal se cerro correctamente.");
        }
    }

    public static void main(String[] args) {
        EmailClient client = new EmailClient("localhost", 50051);

        // Crear un único email para pruebas
        EmailOuterClass.Contacto remitente = EmailOuterClass.Contacto.newBuilder()
            .setNombreCompleto("Joaco Flores")
            .setEmail("joaco@gmail.com")
            .build();
        EmailOuterClass.Contacto destinatario = EmailOuterClass.Contacto.newBuilder()
            .setNombreCompleto("Cande Cano")
            .setEmail("cande@gmail.com")
            .build();
        EmailOuterClass.Email email = EmailOuterClass.Email.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setAsunto("Hola")
            .setContenido("Esto es un email de prueba")
            .setRemitente(remitente)
            .addDestinatarios(destinatario)
            .build();

        client.sendEmail(email);
        // Cambiar aquí
        client.receiveEmails(); // Sin parámetros
        client.shutdown();
    }
}
