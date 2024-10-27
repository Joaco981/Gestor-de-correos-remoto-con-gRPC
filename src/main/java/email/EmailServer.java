package email;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class EmailServer {
    public static void main(String[] args) throws Exception {
        // Crear e iniciar el servidor en el puerto 50051
        Server server = ServerBuilder.forPort(50051)
                .addService(new EmailServiceImpl())
                .build()
                .start();

        System.out.println("Servidor iniciado en el puerto 50051");

        // Mantener el servidor en ejecución
        server.awaitTermination();
    }  
}
