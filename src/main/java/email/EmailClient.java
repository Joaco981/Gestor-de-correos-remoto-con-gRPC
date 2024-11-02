package email;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;
import com.example.gestordecorreo.GrupoDeUsuarios;

import java.util.ArrayList;
import java.util.UUID;

public class EmailClient {
    private final ManagedChannel channel;
    private final EmailServiceGrpc.EmailServiceBlockingStub blockingStub;
    private final EmailServiceGrpc.EmailServiceStub asyncStub;
    private final String clienteEmail;

    public EmailClient(String host, int port, String clienteEmail) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = EmailServiceGrpc.newBlockingStub(channel);
        asyncStub = EmailServiceGrpc.newStub(channel);
        this.clienteEmail = clienteEmail;
    }

    public void enviarEmail(Email email) {
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

        System.out.println("Correo enviado a los destinatarios:");
        for (EmailOuterClass.Contacto destinatario : emailProto.getDestinatariosList()) {
            System.out.println(destinatario.getNombreCompleto() + " (" + destinatario.getEmail() + ")");
        }
        System.out.println(response.getMessage());
    }

    public void recibirEmails() {
        EmailOuterClass.ReceiveRequest request = EmailOuterClass.ReceiveRequest.newBuilder().build();
        final boolean[] recibioCorreo = {false}; // Indicador para saber si se recibió al menos un correo
    
        asyncStub.recibirEmails(request, new StreamObserver<EmailOuterClass.Email>() {
            @Override
            public void onNext(EmailOuterClass.Email email) {
                boolean esDestinatario = email.getDestinatariosList().stream()
                        .anyMatch(destinatario -> destinatario.getEmail().equals(clienteEmail));
    
                if (esDestinatario && !clienteEmail.equals("carla@gmail.com")) {
                    recibioCorreo[0] = true; // Se recibió al menos un correo
                    System.out.println("Usted tiene un correo pendiente:");
                    System.out.println("----------------------------------------");
                    System.out.println("Asunto: " + email.getAsunto());
                    System.out.println("  De: " + email.getRemitente().getNombreCompleto() + " (" + email.getRemitente().getEmail() + ")");
                    System.out.println("  Contenido: " + email.getContenido());
                    System.out.println("----------------------------------------");
                } else if (clienteEmail.equals("carla@gmail.com")) {
                    System.out.println("No hay correos nuevos");
                }
            }
    
            @Override
            public void onError(Throwable t) {
                System.err.println("Error al recibir correos: " + t.getMessage());
            }
    
            @Override
            public void onCompleted() {
                if (!recibioCorreo[0]) {
                    System.out.println("No hay correos nuevos para usted.");
                } else {
                    System.out.println("Se recibieron todos los emails!");
                }
            }
        });
    }
    

    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2 || (args[0].equals("send") && args.length < 4)) {
            if (args[0].equals("send")) {
                System.out.println("Uso para enviar: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"send <email> <nombreGrupo> <excluirEmail>\"");
            } else {
                System.out.println("Uso para recibir: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"receive <contacto>\"");
            }
            return;
        }
    
        String mode = args[0];
        String clienteEmail = args[1];
        EmailClient client = new EmailClient("localhost", 50051, clienteEmail);
    
        if (mode.equals("send")) {
            String nombreGrupo = args[2];
            String excluirEmail = args[3];
    
            Contacto remitente = new Contacto("Joaquin Flores", "joaquin@gmail.com");
            GrupoDeUsuarios grupo = new GrupoDeUsuarios(nombreGrupo);
            grupo.agregarAlGrupo(new Contacto("Candela Cano", "cande@gmail.com"));
            grupo.agregarAlGrupo(new Contacto("Carla Marturet", "carla@gmail.com"));
    
            Email email = new Email();
            email.setAsunto("Hola a todos");
            email.setContenido("Este es un correo de prueba para el grupo.");
            email.setRemitente(remitente);
    
            for (Contacto contacto : grupo.getContactos()) {
                if (!contacto.getEmail().equals(excluirEmail)) {
                    email.agregarDestinatario(contacto);
                }
            }
    
            client.enviarEmail(email);
        } else if (mode.equals("receive")) {
            client.recibirEmails();
        }
    
        client.shutdown();
    }
    
}
