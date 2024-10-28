package email;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;

import java.util.UUID;

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

    public void enviarEmail(Email email) {
        // Convertir com.example.gestordecorreo.Email a EmailOuterClass.Email
        EmailOuterClass.Email.Builder emailProtoBuilder = EmailOuterClass.Email.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setAsunto(email.getAsunto())
                .setContenido(email.getContenido())
                .setRemitente(EmailOuterClass.Contacto.newBuilder()
                        .setNombreCompleto(email.getRemitente().getNombre())
                        .setEmail(email.getRemitente().getEmail())
                        .build());

        for (Contacto destinatario : email.getDestinatarios()) {
            emailProtoBuilder.addDestinatarios(EmailOuterClass.Contacto.newBuilder()
                    .setNombreCompleto(destinatario.getNombre())
                    .setEmail(destinatario.getEmail())
                    .build());
        }

        EmailOuterClass.Email emailProto = emailProtoBuilder.build();
        EmailOuterClass.EmailRequest request = EmailOuterClass.EmailRequest.newBuilder().setEmail(emailProto).build();
        EmailOuterClass.EmailResponse response = blockingStub.enviarEmail(request);
        System.out.println("Enviando correo a: ");
        for (EmailOuterClass.Contacto destinatario : emailProto.getDestinatariosList()) {
            System.out.println(destinatario.getNombreCompleto() + " (" + destinatario.getEmail() + ")");
        }
        System.out.println(response.getMessage());
    }

    public void recibirEmails() {
        EmailOuterClass.ReceiveRequest request = EmailOuterClass.ReceiveRequest.newBuilder().build(); // Crear la solicitud vacía
        asyncStub.recibirEmails(request, new StreamObserver<EmailOuterClass.Email>() {
            @Override
            public void onNext(EmailOuterClass.Email email) {
                System.out.println("Usted tiene un correo pendiente:");
                System.out.println("----------------------------------------");
                System.out.println("Asunto: " + email.getAsunto());
                System.out.println("  De: " + email.getRemitente().getNombreCompleto() + " (" + email.getRemitente().getEmail() + ")");
                System.out.println("  Contenido: " + email.getContenido());
                System.out.println("----------------------------------------");
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
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"<send|receive>\"");
            return;
        }

        String mode = args[0];
        EmailClient client = new EmailClient("localhost", 50051);

        if (mode.equals("send")) {
            Contacto remitente = new Contacto("Joaco Flores", "joaco@gmail.com");
            Contacto destinatario = new Contacto("Cande Cano", "cande@gmail.com");
            Contacto destinatario2 = new Contacto("Carla Marturet", "carla@gmail.com");

            Email email = new Email();
            email.setAsunto("Hola");
            email.setContenido("Esto es un email de prueba");
            email.setRemitente(remitente);
            email.agregarDestinatario(destinatario);
            email.agregarDestinatario(destinatario2);

            client.enviarEmail(email);
        } else if (mode.equals("receive")) {
            client.recibirEmails();
        }

        client.shutdown();
    }
}