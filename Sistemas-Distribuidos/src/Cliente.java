import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) {
        try {
            // Conectar al servidor
            Socket socket = new Socket("localhost", 9595);

            // Leer el número de teléfono desde la consola
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese el número de teléfono: ");
            String numeroTelefono = consoleInput.readLine();

            // Enviar el número de teléfono al servidor
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println(numeroTelefono);

            // Recibir la respuesta del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String respuesta = input.readLine();
            System.out.println("Respuesta del servidor: " + respuesta);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
