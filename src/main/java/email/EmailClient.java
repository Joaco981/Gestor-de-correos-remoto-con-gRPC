package email;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.example.gestordecorreo.Email;
import com.example.gestordecorreo.Contacto;
import com.example.gestordecorreo.GrupoDeUsuarios;
import java.util.UUID;

public class EmailClient {
    private final ManagedChannel channel;
    private final EmailServiceGrpc.EmailServiceBlockingStub blockingStub;
    private final EmailServiceGrpc.EmailServiceStub asyncStub;
    private final String clienteEmail;

    // Definir contactos iniciales
    private final Contacto contactoJoaquin;
    private final Contacto contactoCandela;
    private final Contacto contactoCarla;
    private final Contacto contactoRodri;
    private final Contacto contactoIvan;
    private final Contacto contactoAugusto;

    public EmailClient(String host, int port, String clienteEmail) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = EmailServiceGrpc.newBlockingStub(channel);
        asyncStub = EmailServiceGrpc.newStub(channel);
        this.clienteEmail = clienteEmail;

        contactoJoaquin = new Contacto("Joaquin Flores", "joaquin@gmail.com");
        contactoCandela = new Contacto("Candela Cano", "cande@gmail.com");
        contactoCarla = new Contacto("Carla Marturet", "carla@gmail.com");
        contactoRodri = new Contacto("Rodrigo Magallanes", "rodrigo@gmail.com");
        contactoIvan = new Contacto("Ivan Caceres", "ivan@gmail.com");
        contactoAugusto = new Contacto("Augusto", "augusto@gmail.com");
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
            System.out.println("-" + destinatario.getNombreCompleto() + " (" + destinatario.getEmail() + ")");
        }
        System.out.println(response.getMessage());

        email.getRemitente().bandeja.getBandejaEnviados().add(email);
    }

    public void verBandeja() {
        try {
            EmailOuterClass.Contacto clientContacto = EmailOuterClass.Contacto.newBuilder()
                .setNombreCompleto("") 
                .setEmail(clienteEmail)
                .build();

            EmailOuterClass.BandejaRequest request = EmailOuterClass.BandejaRequest.newBuilder()
                .setClientEmail(clientContacto)
                .build();

            EmailOuterClass.BandejaResponse response = blockingStub.verBandeja(request);

            
            System.out.println("BANDEJA DE ENTRADA DE " + clienteEmail + ":");
            for (EmailOuterClass.Email email : response.getBandejaEntradaList()) {
                System.out.println("----------------------------------------");
                System.out.println("Asunto: " + email.getAsunto());
                System.out.println("De: " + email.getRemitente().getNombreCompleto() + " (" + email.getRemitente().getEmail() + ")");
                System.out.println("Contenido: " + email.getContenido());
                System.out.println("----------------------------------------");
                System.out.println();
            }

            
            System.out.println();
            System.out.println("BANDEJA DE ENVIADOS DE " + clienteEmail + ":");
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
        if (args.length < 2 || (!args[0].equals("enviar") && !args[0].equals("recibir") && !args[0].equals("visualizar"))) {
            System.out.println("Uso para enviar a una persona: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"enviar <remitenteEmail> <destinatarioEmail>\"");
            System.out.println("Uso para enviar a un grupo: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"enviar <remitenteEmail> <nombreGrupo> <excluirEmail>\"");
            System.out.println("Uso para recibir correos: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"recibir <emailDelCliente>\"");
            System.out.println("Uso para mostrar bandeja: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"visualizar <emailDelCliente>\"");
            System.exit(0);
        }

        String clienteEmail = args[1];
        EmailClient client = new EmailClient("localhost", 50051, clienteEmail);

        if (args[0].equals("enviar")) {
            if (args.length == 4) { // EnvÃ­o a grupo
                String nombreGrupo = args[2];
                String excluirEmail = args[3];
                
                Email email = new Email();
                email.setAsunto("Hola a todos");
                email.setContenido("Este es un correo de prueba para el grupo.");

                // Cambiar el remitente al email ingresado por argumento
                email.setRemitente(client.obtenerContactoPorEmail(clienteEmail));

                GrupoDeUsuarios grupo = new GrupoDeUsuarios(nombreGrupo);
                grupo.agregarAlGrupo(client.contactoCandela);
                grupo.agregarAlGrupo(client.contactoCarla);
                grupo.agregarAlGrupo(client.contactoJoaquin);

                for (Contacto contacto : grupo.getContactos()) {
                    if (!contacto.getEmail().equals(excluirEmail)) {
                        email.agregarDestinatario(contacto);
                    }
                }

                client.enviarEmail(email);
            } else if (args.length == 3) { // EnvÃ­o a persona
                String destinatarioEmail = args[2];
                
                Email email = new Email();
                email.setAsunto("Hola");
                email.setContenido("Este es un correo de prueba para ti.");
            
                // Asignar remitente y destinatario
                email.setRemitente(client.obtenerContactoPorEmail(clienteEmail));
                email.agregarDestinatario(client.obtenerContactoPorEmail(destinatarioEmail));
            
                client.enviarEmail(email);
            } else {
                System.out.println("ERROR: Uso incorrecto");
                System.out.println("Uso para enviar: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"enviar <remitenteEmail> <destinatarioEmail>\" para una persona");
                System.out.println("Uso para enviar: mvn exec:java -Dexec.mainClass=\"email.EmailClient\" -Dexec.args=\"enviar <remitenteEmail> <nombreGrupo> <excluirEmail>\" para un grupo");
            }
        } else if (args[0].equals("recibir")) {
            client.recibirEmails();
        } else if (args[0].equals("visualizar")) {
            client.verBandeja();
        }

        client.shutdown();
    }

    private Contacto obtenerContactoPorEmail(String email) {
        if (email.equals(contactoJoaquin.getEmail())) return contactoJoaquin;
        if (email.equals(contactoCandela.getEmail())) return contactoCandela;
        if (email.equals(contactoCarla.getEmail())) return contactoCarla;
        if (email.equals(contactoRodri.getEmail())) return contactoRodri;
        if (email.equals(contactoIvan.getEmail())) return contactoIvan;
        if (email.equals(contactoAugusto.getEmail())) return contactoAugusto;
        return new Contacto("Desconocido", email); // Devuelve un contacto con "Desconocido" si no se encuentra.
    }
    
}