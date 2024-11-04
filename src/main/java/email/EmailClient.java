package email;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;
import com.example.gestordecorreo.GrupoDeUsuarios;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class EmailClient {
    private final ManagedChannel channel;
    private final EmailServiceGrpc.EmailServiceBlockingStub blockingStub;
    private final EmailServiceGrpc.EmailServiceStub asyncStub;
    private final String clienteEmail;

    // Definir contactos iniciales
    private final Contacto contactoJoaquin;
    private final Contacto contactoCandela;
    private final Contacto contactoCarla;

    public EmailClient(String host, int port, String clienteEmail) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = EmailServiceGrpc.newBlockingStub(channel);
        asyncStub = EmailServiceGrpc.newStub(channel);
        this.clienteEmail = clienteEmail;

        // Inicializar contactos
        contactoJoaquin = new Contacto("Joaquin Flores", "joaquin@gmail.com");
        contactoCandela = new Contacto("Candela Cano", "cande@gmail.com");
        contactoCarla = new Contacto("Carla Marturet", "carla@gmail.com");
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

        // Agregar el email a la bandeja de enviados del remitente
        email.getRemitente().bandeja.getBandejaEnviados().add(email);
    }

    public void verBandeja() {
        try {
            EmailOuterClass.Contacto clientContacto = EmailOuterClass.Contacto.newBuilder()
                .setNombreCompleto("") // Puedes ajustar según sea necesario
                .setEmail(clienteEmail)
                .build();

            EmailOuterClass.BandejaRequest request = EmailOuterClass.BandejaRequest.newBuilder()
                .setClientEmail(clientContacto)
                .build();

            EmailOuterClass.BandejaResponse response = blockingStub.verBandeja(request);

            // Mostrar Bandeja de Entrada
            System.out.println("Bandeja de Entrada de " + clienteEmail + ":");
            System.out.println();
            for (EmailOuterClass.Email email : response.getBandejaEntradaList()) {
                System.out.println("----------------------------------------");
                System.out.println("Asunto: " + email.getAsunto());
                System.out.println("De: " + email.getRemitente().getNombreCompleto() + " (" + email.getRemitente().getEmail() + ")");
                System.out.println("Contenido: " + email.getContenido());
                System.out.println("----------------------------------------");
                System.out.println();
            }

            // Mostrar Bandeja de Enviados
            System.out.println();
            System.out.println("Bandeja de Enviados de " + clienteEmail + ":");
            for (EmailOuterClass.Email email : response.getBandejaEnviadosList()) {
                System.out.println("----------------------------------------");
                System.out.println("Asunto: " + email.getAsunto());
                System.out.println("De: " + email.getRemitente().getNombreCompleto() + " (" + email.getRemitente().getEmail() + ")");
                System.out.println("Contenido: " + email.getContenido());
                System.out.println("----------------------------------------");
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener la bandeja: " + e.getMessage());
        }
    }

    public Email convertirEmail(EmailOuterClass.Email emailOuter) {
        // Verificar y asignar el contacto existente como remitente
        Contacto remitente;
        String remitenteEmail = emailOuter.getRemitente().getEmail();

        if (remitenteEmail.equals(contactoJoaquin.getEmail())) {
            remitente = contactoJoaquin;
        } else if (remitenteEmail.equals(contactoCandela.getEmail())) {
            remitente = contactoCandela;
        } else if (remitenteEmail.equals(contactoCarla.getEmail())) {
            remitente = contactoCarla;
        } else {
            remitente = new Contacto(emailOuter.getRemitente().getNombreCompleto(), remitenteEmail);
        }

        // Crear el objeto Email y configurar sus campos
        Email email = new Email();
        email.setAsunto(emailOuter.getAsunto());
        email.setContenido(emailOuter.getContenido());
        email.setRemitente(remitente);

        // Asignar los destinatarios existentes a partir de los contactos predefinidos
        for (EmailOuterClass.Contacto destinatarioOuter : emailOuter.getDestinatariosList()) {
            Contacto destinatario;
            String destinatarioEmail = destinatarioOuter.getEmail();

            if (destinatarioEmail.equals(contactoJoaquin.getEmail())) {
                destinatario = contactoJoaquin;
            } else if (destinatarioEmail.equals(contactoCandela.getEmail())) {
                destinatario = contactoCandela;
            } else if (destinatarioEmail.equals(contactoCarla.getEmail())) {
                destinatario = contactoCarla;
            } else {
                destinatario = new Contacto(destinatarioOuter.getNombreCompleto(), destinatarioEmail);
            }

            email.agregarDestinatario(destinatario);
        }

        return email;
    }

    public void recibirEmails() {
        EmailOuterClass.ReceiveRequest request = EmailOuterClass.ReceiveRequest.newBuilder().build();
        final boolean[] recibioCorreo = {false};

        asyncStub.recibirEmails(request, new StreamObserver<EmailOuterClass.Email>() {
            @Override
            public void onNext(EmailOuterClass.Email emailOuter) {
                boolean esDestinatario = emailOuter.getDestinatariosList().stream()
                        .anyMatch(destinatario -> destinatario.getEmail().equals(clienteEmail));

                if (esDestinatario) {
                    recibioCorreo[0] = true;

                    System.out.println("Usted tiene un correo pendiente:");
                    System.out.println("----------------------------------------");
                    System.out.println("Asunto: " + emailOuter.getAsunto());
                    System.out.println("  De: " + emailOuter.getRemitente().getNombreCompleto() + " (" + emailOuter.getRemitente().getEmail() + ")");
                    System.out.println("  Contenido: " + emailOuter.getContenido());
                    System.out.println("----------------------------------------");

                    // Convertir EmailOuterClass.Email a Email y agregarlo a la bandeja de entrada del destinatario
                    Email email = convertirEmail(emailOuter);

                    if (clienteEmail.equals(contactoJoaquin.getEmail())) {
                        contactoJoaquin.bandeja.getBandejaEntrada().add(email);
                    } else if (clienteEmail.equals(contactoCandela.getEmail())) {
                        contactoCandela.bandeja.getBandejaEntrada().add(email);
                    } else if (clienteEmail.equals(contactoCarla.getEmail())) {
                        contactoCarla.bandeja.getBandejaEntrada().add(email);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error al recibir correos: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                if (!recibioCorreo[0]) {
                    System.out.println("No hay correos nuevos.");
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
        if (args.length < 2 || (!args[0].equals("send") && !args[0].equals("receive") && !args[0].equals("show"))) {
            System.out.println("Uso para enviar: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"send <email> <nombreGrupo> <excluirEmail>\"");
            System.out.println("Uso para recibir: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"receive <email>\"");
            System.out.println("Uso para mostrar bandejas: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"show <email>\"");
            return;
        }

        String mode = args[0];
        String clienteEmail = args[1];
        EmailClient client = new EmailClient("localhost", 50051, clienteEmail);

        if (mode.equals("send")) {
            String nombreGrupo = args[2];
            String excluirEmail = args[3];

            Email email = new Email();
            email.setAsunto("Hola a todos");
            email.setContenido("Este es un correo de prueba para el grupo.");
            email.setRemitente(client.contactoJoaquin);

            GrupoDeUsuarios grupo = new GrupoDeUsuarios(nombreGrupo);
            grupo.agregarAlGrupo(client.contactoCandela);
            grupo.agregarAlGrupo(client.contactoCarla);

            for (Contacto contacto : grupo.getContactos()) {
                if (!contacto.getEmail().equals(excluirEmail)) {
                    email.agregarDestinatario(contacto);
                }
            }

            client.enviarEmail(email);
        } else if (mode.equals("receive")) {
            client.recibirEmails();
        } else if (mode.equals("show")) {
            client.verBandeja();
        }

        client.shutdown();
    }
}
