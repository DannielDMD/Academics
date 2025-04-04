import java.io.*;
import java.net.*;
import java.sql.*;



public class Server {

    public static void main(String[] args) {
        try {
            // CONFIGURACIÓN DEL SOCKET
            ServerSocket serverSocket = new ServerSocket(9595);
            System.out.println("Servidor esperando conexiones...");

            // CONEXION A LA BASE DE DATOS DE MYSQL CON XAMPP
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sistema_telefonico", "root", "");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado");

                // BUFFER PARA LEER EL NUMERO DE TELEFONO DEL CLIENTE
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String numeroTelefono = input.readLine();

                // CONSULTA A LA BASE DE DATOS
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT dir_nombre, dir_direccion, ciudad_nombre FROM personas " +
                    		"INNER JOIN ciudades ON personas.dir_ciudad_id = ciudades.ciudad_id " +
                    "WHERE dir_tel = ?"
                );
                preparedStatement.setString(1, numeroTelefono);
                ResultSet resultSet = preparedStatement.executeQuery();

                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                if (resultSet.next()) {
                    String nombre = resultSet.getString("dir_nombre");
                    String direccion = resultSet.getString("dir_direccion");
                    String ciudad = resultSet.getString("ciudad_nombre");
                    output.println("Nombre: " + nombre + ", Dirección: " + direccion + ", Ciudad: " + ciudad);
                } else {
                    output.println("Persona dueña de ese número telefónico no existe.");
                }

                socket.close();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
