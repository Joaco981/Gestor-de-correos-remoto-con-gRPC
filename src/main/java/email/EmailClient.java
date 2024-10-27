package email;

import email.EmailOuterClass;
import email.EmailServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

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
        EmailOuterClass.EmailRequest request = EmailOuterClass.EmailRequest.newBuilder()
                .setEmail(email)
                .build();
        EmailOuterClass.EmailResponse response = blockingStub.sendEmail(request);
        
        // Imprimir el resultado del envío
        if (response.getSuccess()) {
            System.out.println("Email enviado correctamente: " + response.getMessage());
        } else {
            System.out.println("Error al enviar el email: " + response.getMessage());
        }
    }

    public void receiveEmails(String emailAddress) {
        EmailOuterClass.ReceiveRequest request = EmailOuterClass.ReceiveRequest.newBuilder()
                .setEmail(emailAddress)
                .build();
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

    // Método para cerrar el canal gRPC
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            System.out.println("El canal se cerró correctamente.");
        }
    }

    public static void main(String[] args) {
        EmailClient client = new EmailClient("localhost", 50051);

        // Crear y enviar un email
        EmailOuterClass.Contacto remitente = EmailOuterClass.Contacto.newBuilder()
                .setNombreCompleto("Joaco Flores")
                .setEmail("joaco@gmail.com")
                .build();
        EmailOuterClass.Contacto destinatario = EmailOuterClass.Contacto.newBuilder()
                .setNombreCompleto("Cande Cano")
                .setEmail("cande@gmail.com")
                .build();
        EmailOuterClass.Email email = EmailOuterClass.Email.newBuilder()
                .setAsunto("Hola")
                .setContenido("Esto es un email de prueba")
                .setRemitente(remitente)
                .addDestinatarios(destinatario)
                .build();
        
        // Enviar el email
        client.sendEmail(email);

        // Recibir emails para una dirección específica
        client.receiveEmails("cande@gmail.com");

        // Cerrar el canal al final de la ejecución
        client.shutdown();
    }
}
